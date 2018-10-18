package com.veritas.automation.erya.services.crawlers.entities;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
@Document( collection = "test_job_results")
@TypeAlias("test_job_results")
@CompoundIndexes({
        @CompoundIndex(unique = true, name="unique_id_idx",def = "{'name':1,'buildId':-1}")
})
public class TestJobResult extends CiJobResult {
    private List<RobotResult> robotResults = new ArrayList<>();
    private int failedCases = 0;
    private int passedCases = 0;
    private int totalCases = 0;
    private int executedCases =0;

    public int getExecutedCases() {
        return executedCases;
    }

    public void setExecutedCases(int executedCases) {
        this.executedCases = executedCases;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getPassedCases() {
        return passedCases;
    }

    public void setPassedCases(int passedCases) {
        this.passedCases = passedCases;
    }

    public int getFailedCases() {
        return failedCases;
    }

    public void setFailedCases(int failedCases) {
        this.failedCases = failedCases;
    }

    public List<RobotResult> getRobotResults() {
        return robotResults;
    }

    public void setRobotResults(List<RobotResult> robotResults) {
        this.robotResults = robotResults;
    }
}
