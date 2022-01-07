package com.raxware.healthCheck;

import com.raxware.healthCheck.health.HealthCheckReport;
import com.raxware.healthCheck.utils.FilterUtils;
import com.raxware.healthCheck.utils.IOUtils;
import com.raxware.healthCheck.utils.Random;

import java.io.*;
import java.net.Socket;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class ServerConnection implements Runnable {
    private final Socket socket;
    private final HealthCheckReport lastHealthCheck;
    private static final Logger logger = Logger.getLogger("health-check-connection");
    private final int connectionId;

    public ServerConnection(Socket socket, HealthCheckReport lastHealthCheck) {
        this.socket = socket;
        this.lastHealthCheck = lastHealthCheck;
        this.connectionId = Random.randomInt(1000,9999);
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        trace("New Connection from "+socket.getRemoteSocketAddress());

        // we read characters from the client via input stream on the socket
        long start = System.currentTimeMillis();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(socket.getOutputStream());

            String headerLine = in.readLine();
            String method = FilterUtils.filter(headerLine.substring(0, headerLine.indexOf(' ')), "GET");
            if(method.equalsIgnoreCase("GET")) {
                if(lastHealthCheck == HealthCheckReport.NO_REPORTS) {
                    writeError(out, "No health checks to perform");
                } else if(lastHealthCheck.getFailed() > 0) {
                    writeError(out, "Failed health checks: "+lastHealthCheck.getFailed());
                } else {
                    writeOk(out, "OK. Passed "+lastHealthCheck.getSuccess() + " health check(s). Last Check was "+lastHealthCheck.getTimestamp());
                }

            } else {
                severe("Invalid HTTP Method: "+method);
                writeError(out, "Invalid HTTP method");
            }

            writeOk(out, "Still working on it");
        } catch (IOException e) {
            severe("I/O Error in connection: "+e.getMessage());
            try {
                writeError(out, "I/O Error: "+e.getMessage());
            } catch (IOException ignored) {}
        } finally {
            IOUtils.quietClose(in);
            IOUtils.quietClose(out);
            trace("Processing took "+(System.currentTimeMillis()-start) + "ms");
        }
    }

    private void writeError(PrintWriter out, String message) throws IOException {
        // send HTTP Headers
        out.println("HTTP/1.1 500 Internal Server Error");
        out.println("Server: Docker Health Check Server/1.0");
        out.println("Date: " + OffsetDateTime.now().toString());
        out.println("Content-type: text/plain; charset=UTF-8");
        out.println("Connection: Close");
        out.println("Content-length: " + message.length());
        out.println(); // blank line between headers and content, very important !
        out.print(message);
        out.flush(); // flush character output stream buffer
    }

    private void writeOk(PrintWriter out, String message) throws IOException {
        // send HTTP Headers
        out.println("HTTP/1.1 200 OK");
        out.println("Server: Docker Health Check Server/1.0");
        out.println("Date: " + OffsetDateTime.now().toString());
        out.println("Content-type: text/plain; charset=UTF-8");
        out.println("Content-length: " + message.length());
        out.println("Connection: Close");
        out.println(); // blank line between headers and content, very important !
        out.print(message);
        out.flush(); // flush character output stream buffer
    }

    private void info(String message) {
        logger.info("["+connectionId+"] " + message);
    }

    private void trace(String message) {
        logger.fine("["+connectionId+"] " + message);
    }

    private void severe(String message) {
        logger.severe("["+connectionId+"] " + message);
    }
}
