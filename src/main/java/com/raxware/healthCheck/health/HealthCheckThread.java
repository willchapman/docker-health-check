package com.raxware.healthCheck.health;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class HealthCheckThread extends Thread implements Runnable {
    private final long interval;
    private final HealthChecks healthChecks;
    private final Consumer<HealthCheckThread> doThreadIsActive;

    private static final Logger logger = Logger.getLogger("health-check-thread");
    private HealthCheckReport lastHealthCheck = HealthCheckReport.NO_REPORTS;
    private boolean active = false;

    public HealthCheckThread(long interval, HealthChecks healthChecks, Consumer<HealthCheckThread> doThreadIsActive) {
        super("HealthCheckThread");
        this.interval = Long.max(5000, interval);
        this.healthChecks = Objects.requireNonNull(healthChecks);
        this.doThreadIsActive = doThreadIsActive;
    }

    @Override
    public void run() {
        try {
            active = true;
            doThreadIsActive.accept(this);
            while(true) {
                synchronized (lastHealthCheck) {
                    lastHealthCheck = healthChecks.doHealthCheck();
                }
                Thread.sleep(interval);
            }
        }catch (Exception ex) {
            logger.severe("Health Check Thread Failed");
        } finally {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public HealthCheckReport getLastHealthCheck() {
        synchronized (lastHealthCheck) {
            return lastHealthCheck;
        }
    }
}
