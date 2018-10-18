package com.veritas.automation.erya.services.crawlers.entities;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
public class DeploymentStatus {
    private String appliance;
    private String status;
    private String type;

    public String getAppliance() {
        return appliance;
    }

    public void setAppliance(String appliance) {
        this.appliance = appliance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
