package com.veritas.automation.erya.services.crawlers.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
/*
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbCollectionsTests {


    @Autowired
    private MongoOperations mongoOps;

    @Test
    public void insertCiResults(){
        CiResult result = new CiResult();
        result.setCreateTime(LocalDateTime.now());
        result.setTriggerId(2278);
        result.setTotalBuildTime(Duration.ofHours(2).toMillis());
        result.setBuildTag("nba-3.0.1-9726320170125090303");
        result.setPipelineName("nba_pipeline");
        result.setPipelineAlias("nba_pipeline");
        result.setStartTime(LocalDateTime.now());
        TestJob testJob = new TestJob();
        testJob.setUri("http://localhost:8080/#/mainline/detail/NBA%20Pipeline");
        testJob.setName("nba_main_regression_autosupport");
        testJob.setAlias("nba_main_regression_autosupport");
        testJob.setStartTime(LocalDateTime.now());
        testJob.setBuildTag("nba-3.0.1-9726320170125090303");
        testJob.setBuildId(2447);
        testJob.setTriggerId(2248);
        testJob.setDuration(Duration.ofHours(1).toMillis());
        testJob.setName("nba_main_regression_autosupport");
        testJob.setPassRate(98.5f);
        result.getTestJobs().add(testJob);
        DeploymentJob deploymentJob = new DeploymentJob();
        deploymentJob.setStartTime(LocalDateTime.now());
        deploymentJob.setUri("http://localhost:8080/#/mainline/detail/NBA%20Pipeline");
        deploymentJob.setBuildTag("nba-3.0.1-9726320170125090303");
        deploymentJob.setBuildId(2447);
        deploymentJob.setName("nba_main_cd_regression_deploy");
        deploymentJob.setAlias("nba_main_cd_regression_deploy");
        deploymentJob.setDuration(Duration.ofHours(2).toMillis());
        deploymentJob.setStatus("success");
        deploymentJob.setBuildId(2248);
        result.getDeploymentJobs().add(deploymentJob);

        mongoOps.insert(result);




    }

    @Test
    public void insertDeploymentJobResults(){
        DeploymentJobResult result = new DeploymentJobResult();
        result.setCreateTime(LocalDateTime.now());
        result.setPipelineName("NBA_LONG_REGRESSION_PIPELINE");
        result.setUri("http://jenkins-appliance.engba.veritas.com/job/nba_main_regression_d2c/11/robot/report/alan.yu/2017-1-25_12-7-37/D2C_Install_and_Set_Master_role_nbapp131/log.html");
        result.setStartTime(LocalDateTime.now());
        result.setTriggerId(2277);
        result.setBuildId(350);
        result.setBuildTag("3.0.1-9788520170206090249");
        result.setName("nba_main_regression_deploy");
        result.setAlias("nba_main_regression_deploy");
        DeploymentStatus status = new DeploymentStatus();
        status.setStatus("installed");
        status.setAppliance("nbur420-492-vm6");
        status.setType("dynamic");
        result.getDeployStatus().add(status);
        mongoOps.insert(result);
    }

    @Test
    public void insertTestJobResult(){
        TestJobResult result=new TestJobResult();
        result.setCreateTime(LocalDateTime.now());
        result.setPipelineName("NBA_PIPELINE");
        result.setStartTime(LocalDateTime.now());
        result.setAlias("nba_main_regression_autosupport");
        result.setName("nba_main_regression_autosupport");
        result.setUri("http://jenkins-appliance.engba.veritas.com/job/nba_main_regression_autosupport/1159/");
        result.setBuildTag("3.0.1-9788520170206090249");
        result.setBuildId(1159);
        result.setTriggerId(2277);
        RobotResult scenario = new RobotResult();
        scenario.setScenario("AutoSupport_Client_5220_Batch9");
        scenario.setAppliance("asesxb9k21-vm06");
        scenario.setDuration(Duration.ofHours(2).toMillis());
        scenario.setPassRate(98f);
        scenario.setGeneratedTime(LocalDateTime.now());
        scenario.setUri("http://jenkins-appliance.engba.veritas.com/job/nba_main_regression_autosupport/1159/robot/report/nilesh.pathak/2017-2-6_12-19-18/AutoSupport_Client_5220_Batch9_asesxb9k21-vm06/log.html");
        result.getRobotResults().add(scenario);
        mongoOps.insert(result);
    }

    @Test
    public void queryCiResults(){
        List<CiResult> results = mongoOps.findAll(CiResult.class);
        int count = results.size();


    }
}
*/