package com.raxware.healthCheck;

import java.io.File;
import java.util.logging.Logger;

/**
 * Abstraction of the Environment settings for our application.
 */
public class Environment {
    private static final Logger logger = Logger.getLogger("env");

    private Environment() {}

    /**
     * Where the pull the configuration for the health checks ( defaults to config/checks.properties )
     */
    public static final String HEALTH_CHECK_CONFIG = getString("HEALTH_CHECK_CONFIG", new File("config/checks.properties").getAbsolutePath());

    /**
     * What port to listen on for the health checks ( defaults to 80 )
     */
    public static final int HEALTH_CHECK_WEB_PORT = getInteger("HEALTH_CHECK_WEB_PORT", 80);

    /**
     * How often to execute the health checks in milliseconds ( defaults to 30 seconds )
     */
    public static final long HEALTH_CHECK_INTERVAL = getLong("HEALTH_CHECK_INTERVAL", 30_000L);

    /**
     * Simple utility method to get a String environment variable or a default value if the environment variable
     * is not set.
     *
     * @param envVarName The name of the environment variable
     * @param defaultValue The value return if the environment variable is not set
     * @return The value of the environment variable, or the defaultValue is not set
     */
    private static String getString(String envVarName, String defaultValue) {
        String value = System.getenv(envVarName);
        return value == null ? defaultValue : value;
    }

    /**
     * Simple utility method to get an integer value from an environment variable. It will return the default value
     * if either the environment variable is not set, or the set value is not a valid number.
     *
     * @param envVarName The name of the environment variable
     * @param defaultValue The default value if it is not set or invalid format
     * @return The parsed integer value if set and valid, the defaultValue otherwise
     */
    private static int getInteger(String envVarName, int defaultValue) {
        String value = null;
        try {
            value = System.getenv(envVarName);
            if (value == null)
                return defaultValue;
            else
                return Integer.parseInt(value);
        } catch (Exception e) {
            logger.warning("Invalid integer: " + envVarName + " - " + value);
            return defaultValue;
        }
    }

    /**
     * Simple utility method to get an long value from an environment variable. It will return the default value if
     * either the environment variable is not set or the set value is invalid
     *
     * @param envVarName The name of the environment variable
     * @param defaultValue The default value if it is not set or invalid
     * @return The parsed long value if set and valid, the defaultValue otherwise
     */
    private static long getLong(String envVarName, long defaultValue) {
        String value = null;
        try {
            value = System.getenv(envVarName);
            if(value == null)
                return defaultValue;
            else
                return Long.parseLong(value);
        }catch (Exception e) {
            logger.warning("Invalid long: " + envVarName + " - " + value);
            return defaultValue;
        }
    }
}
