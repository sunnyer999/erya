package com.veritas.automation.erya.services.crawlers.entities;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
public class DeploymentJob extends CiJob {

    private int triggerId;
    private String alias;

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
}
