package com.raxware.healthCheck.checks;

import com.raxware.healthCheck.utils.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Logger;

public class SimpleTCPHealthCheck extends AbstractCheck {

    private static final Logger logger = Logger.getLogger("check-tcp");

    private String host = null;
    private int port = -1;

    public SimpleTCPHealthCheck() {
    }

    @Override
    public void loadConfiguration(String input) throws Exception {
        String[] parts = input.split(":");
        if(parts.length != 2) {
            throw new IllegalArgumentException("Invalid syntax: "+input);
        }

        host = parts[0];
        try {
            port = Integer.parseInt(parts[1]);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port: "+e.getMessage());
        }

        logger.info("Loading TCP check for "+host + ":"+ port);
    }

    @Override
    public boolean doCheck() throws Exception {
        Objects.requireNonNull(host, "null host");
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port: " + port);

        logger.finest("Checking "+host + ":" + port);
        OutputStream outputStream = null;
        InputStream inputStream = null;
        Socket socket = null;
        boolean result = false;
        try {
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            result = socket.isConnected();

        } catch (Exception e) {
            logger.warning("Health check failed on "+host+":"+port+" " + e.getClass().getSimpleName() + " " + e.getMessage());
            throw e;
        } finally {
            IOUtils.quietClose(outputStream);
            IOUtils.quietClose(inputStream);
            IOUtils.quietClose(socket);
        }
        return result;
    }
}
