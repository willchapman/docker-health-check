package com.raxware.healthCheck.health;

import java.time.OffsetDateTime;

public class HealthCheckReport {
    public static final HealthCheckReport NO_REPORTS = new HealthCheckReport(0,0);
    private final OffsetDateTime reportTime;

    private int success = 0;
    private int failed = 0;

    public HealthCheckReport(int success, int failed) {
        this.success = success;
        this.failed = failed;
        this.reportTime = OffsetDateTime.now();
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }

    public OffsetDateTime getTimestamp() {
        return reportTime;
    }

    @Override
    public String toString() {
        return "HealthCheckReport{" +
                "success=" + success +
                ", failed=" + failed +
                '}';
    }
}
