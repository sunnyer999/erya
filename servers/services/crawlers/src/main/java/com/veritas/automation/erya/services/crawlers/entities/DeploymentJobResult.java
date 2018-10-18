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
@Document(collection = "deployment_job_results")
@TypeAlias("deployment_job_results")
@CompoundIndexes({
        @CompoundIndex(unique = true, name="unique_id_idx",def = "{'name':1,'buildId':-1}")
})
public class DeploymentJobResult extends CiJobResult {

    private String status;

    private List<DeploymentStatus> deployStatus = new ArrayList<>();

    public float getSuccessRate(){
        if(status.trim().toLowerCase().contains("success")){
            return 1f;
        }
        else if(this.deployStatus==null||this.deployStatus.size()==0){
            return 0f;
        }
            return this.deployStatus.stream()
                    .filter(status -> status.getStatus().trim().toLowerCase()
                            .contains("failed")).count() / this.deployStatus.size();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DeploymentStatus> getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(List<DeploymentStatus> deployStatus) {
        this.deployStatus = deployStatus;
    }
}
