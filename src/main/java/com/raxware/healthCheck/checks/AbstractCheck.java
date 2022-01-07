package com.raxware.healthCheck.checks;

public abstract class AbstractCheck {
    /**
     * Performs a simple 'health check' and will respond accordingly depending on this method. A failed
     * health check will result if this method returns 'false' or any exception is thrown.
     *
     * Return true considers this check 'healthy'
     *
     * @return true if the health check succeeds, false otherwise
     * @throws Exception if something really bad happens. This fails the health check
     */
    public abstract boolean doCheck() throws Exception;

    /**
     *
     * @param input
     * @throws Exception
     */
    public abstract void loadConfiguration(String input) throws Exception;
}
