package com.veritas.automation.erya.services.crawlers.entities;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
@Document(collection = "ci_results")
@TypeAlias("ci_results")
@CompoundIndexes({
        @CompoundIndex(unique = true,name="unique_id_idx",def = "{'pipelineName':1,'triggerId':-1}")
})
public class CiResult extends BaseEntity {

    private long totalBuildTime;
    private String pipelineName;
    private String pipelineAlias;
    private String buildTag;
    private LocalDateTime startTime;
    private int triggerId;
    private List<TestJob> testJobs = new ArrayList<>();
    private List<DeploymentJob> deploymentJobs = new ArrayList<DeploymentJob>();
    private List<ReportJob> reportJobs = new ArrayList<>();
    private List<BuildJob> buildJobs = new ArrayList<>();

    public List<ReportJob> getReportJobs() {
        return reportJobs;
    }

    public void setReportJobs(List<ReportJob> reportJobs) {
        this.reportJobs = reportJobs;
    }

    public List<BuildJob> getBuildJobs() {
        return buildJobs;
    }

    public void setBuildJobs(List<BuildJob> buildJobs) {
        this.buildJobs = buildJobs;
    }

    public long getTotalBuildTime() {
        return totalBuildTime;
    }

    public void setTotalBuildTime(long totalBuildTime) {
        this.totalBuildTime = totalBuildTime;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(int triggerId) {
        this.triggerId = triggerId;
    }

    public String getPipelineAlias() {
        return pipelineAlias;
    }

    public void setPipelineAlias(String pipelineAlias) {
        this.pipelineAlias = pipelineAlias;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getBuildTag() {
        return buildTag;
    }

    public void setBuildTag(String buildTag) {
        this.buildTag = buildTag;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<TestJob> getTestJobs() {
        return testJobs;
    }

    public void setTestJobs(List<TestJob> testJobs) {
        this.testJobs = testJobs;
    }

    public List<DeploymentJob> getDeploymentJobs() {
        return deploymentJobs;
    }

    public void setDeploymentJobs(List<DeploymentJob> deploymentJobs) {
        this.deploymentJobs = deploymentJobs;
    }
}
