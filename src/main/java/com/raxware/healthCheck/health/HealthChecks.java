package com.raxware.healthCheck.health;

import com.raxware.healthCheck.checks.AbstractCheck;

import java.util.ArrayList;
import java.util.List;

public class HealthChecks {

    private List<AbstractCheck> checks = new ArrayList<>();

    public void addCheck(AbstractCheck check) {
        checks.add(check);
    }

    public synchronized HealthCheckReport doHealthCheck() {
        if(checks.size() == 0)
            return HealthCheckReport.NO_REPORTS;

        int success = 0;
        int failed = 0;
        for (AbstractCheck check : checks) {
            try {
                if(check.doCheck())
                    success++;
                else
                    failed++;
            }catch (Exception e) {
                failed++;
            }
        }
        return new HealthCheckReport(success, failed);

    }
}
