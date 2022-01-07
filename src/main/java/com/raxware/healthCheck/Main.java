package com.raxware.healthCheck;

import com.raxware.healthCheck.checks.AbstractCheck;
import com.raxware.healthCheck.health.HealthCheckThread;
import com.raxware.healthCheck.health.HealthChecks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger("health-check-server");

    public static void main(String[] args) {

        try {
            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Failed to read logging properties");
            System.exit(1);
        }



        HealthChecks healthChecks = null;
        try {
            logger.info("Loading configuration: " + Environment.HEALTH_CHECK_CONFIG);
            Properties config = new Properties();
            config.load(new FileReader(Environment.HEALTH_CHECK_CONFIG));
            healthChecks = loadHealthChecks(config);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

        HealthCheckThread healthCheckThread = new HealthCheckThread(Environment.HEALTH_CHECK_INTERVAL, healthChecks, Main::doRunServer);
        healthCheckThread.setDaemon(true);
        healthCheckThread.start();
        try {
            healthCheckThread.join();
        } catch (InterruptedException e) {
            logger.severe("Health check thread join failed");
        }

    }

    /**
     * Callback from the HealthCheckThread so we can safely start our web server. This method simply
     * builds a thread and starts it.
     *
     *
     * @param healthCheckThread
     */
    private static void doRunServer(HealthCheckThread healthCheckThread) {
        Thread serverThread = new Thread(() -> doRunWebServer(healthCheckThread));
        serverThread.start();
    }

    /**
     * Runs the main logic part of the web server, by accepting connections and passing a reference
     * to the health check report to each connection.
     *
     * @param healthCheckThread
     */
    private static void doRunWebServer(HealthCheckThread healthCheckThread) {
        try {
            int port = Environment.HEALTH_CHECK_WEB_PORT;
            ServerSocket serverConnect = new ServerSocket(port);
            logger.info("Starting health-check on port " + port);
            // we listen until user halts server execution
            while (true) {
                if(!healthCheckThread.isActive())
                    throw new IllegalStateException("Health check thread not active");

                ServerConnection myServer = new ServerConnection(serverConnect.accept(), healthCheckThread.getLastHealthCheck());

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
            System.exit(-4);
        }

    }

    private static HealthChecks loadHealthChecks(Properties config) {
        HealthChecks healthChecks = new HealthChecks();
        for (Object o : config.keySet()) {
            if(o instanceof String) {
                AbstractCheck healthCHeck = createHealthCHeck(o.toString(), config.getProperty(o.toString()));
                if(healthCHeck != null)
                    healthChecks.addCheck(healthCHeck);
            } else {
                logger.warning("Unknown health check config: ["+o.getClass().getName() + "] " + o);
            }
        }
        return healthChecks;
    }

    private static AbstractCheck createHealthCHeck(String clsName, String configString) {
        try {
            Class<? > aClass = Class.forName(clsName);
            Object o = aClass.newInstance();
            if(o instanceof AbstractCheck) {
                AbstractCheck abstractCheck = (AbstractCheck) o;
                abstractCheck.loadConfiguration(configString);
                return abstractCheck;
            } else {
                logger.warning("Invalid check type: "+clsName + ". Class must extend AbstractCheck");
                return null;
            }
        }catch (Exception e) {
            logger.warning("Failed to create health check: "+clsName + ": "+e.getMessage());
            return null;
        }
    }
}
