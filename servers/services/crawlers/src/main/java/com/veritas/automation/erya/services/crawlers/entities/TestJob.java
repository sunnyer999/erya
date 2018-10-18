package com.veritas.automation.erya.services.crawlers.entities;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
public class TestJob extends CiJob {
    private int triggerId;
    private String alias;
    private Float passRate=0.0f;
    private String pipelineName;

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public int getTriggerId() {

        return triggerId;
    }

    public void setTriggerId(int triggerId) {
        this.triggerId = triggerId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Float getPassRate() {
        return passRate;
    }

    public void setPassRate(Float passRate) {
        this.passRate = passRate;
    }
}
