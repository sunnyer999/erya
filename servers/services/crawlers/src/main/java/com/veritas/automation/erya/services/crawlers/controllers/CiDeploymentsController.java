package com.veritas.automation.erya.services.crawlers.controllers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.veritas.automation.erya.services.crawlers.entities.DeploymentJobResult;
import com.veritas.automation.erya.services.crawlers.repositories.DeploymentJobRepository;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Dean.Zou on 2/6/2017.
 */
@RepositoryRestController
@RequestMapping(value = Constants.REST_SEG_CI_DEPLOYMENTS)
public class CiDeploymentsController extends CrawlerController {


    private final MongoOperations mongoOps;

    private final DeploymentJobRepository deploymentResults;

    @Autowired
    public CiDeploymentsController(MongoOperations ops, DeploymentJobRepository repo) {
        this.mongoOps = ops;
        this.deploymentResults = repo;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/stats/failures")
    @ResponseBody
    public Object searchStatsSuccessRate(
            @RequestParam(value = "limit", defaultValue = "200") int limit,
            @RequestParam(value = "pipeline", defaultValue = ".*") String pipeline,
            @RequestParam(value = "name", defaultValue = ".*") String name ) {

        Aggregation aggregation = newAggregation(
                match(
                        where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))
                        .andOperator(
                        where("name").regex(Pattern.compile(name.trim(),Pattern.CASE_INSENSITIVE)))
                ),
                project("pipelineName","triggerId","name","status","uri")
                        .andExclude("_id")
                        .and("deployStatus").size().as("totalDeploymentCount")
                        .and("deployStatus")
                        .filter("status",context ->  new BasicDBObject("$eq",Arrays.asList("$$status.status","failed")))
                            .as("failedDeployments"),
                group("triggerId","pipelineName").push("$$ROOT").as("deployments"),
                sort(Sort.Direction.DESC,"pipelineName","triggerId"),
                limit(limit)
        );
        AggregationResults<BasicDBObject> results =
                mongoOps.aggregate(aggregation, DeploymentJobResult.class,BasicDBObject.class);
        results.getMappedResults().forEach(obj->{
            ((BasicDBList) obj.get("deployments")).forEach(deploy->{
                BasicDBObject detail =(BasicDBObject)deploy;
                if(detail.get("status")==null){
                    detail.put("status","UNKNOWN");
                }
                String status = detail.get("status").toString().trim().toLowerCase();
                int failedCount = ((BasicDBList) detail.get("failedDeployments")).size();
                int totalCount = Integer.parseInt( detail.get("totalDeploymentCount").toString());
                if(status.contains("success")) {
                    detail.put("successRate", 1);
                }else if(totalCount>0 && failedCount>0){
                    detail.put("successRate",1-failedCount*1.0/totalCount*1.0f);
                }else {
                    detail.put("successRate",0);
                }
            });

        });
        return  results.getMappedResults();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/failures/by")
    @ResponseBody
    public Object searchFailureSuccessRate(
            @RequestParam(value = "limit", defaultValue = "200") int limit,
            @RequestParam(value = "pipeline", defaultValue = ".*") String pipeline,
            @RequestParam(value = "name", defaultValue = ".*") String name,
            @RequestParam(value = "category",defaultValue = "triggerId") String cate,
            @RequestParam(value = "start", defaultValue = "2016-12-31")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam(value = "end", defaultValue = "2016-12-31")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {

        Instant startTime = start.toInstant().isBefore(Instant.parse("2017-01-01T00:00:00Z"))?
                Instant.now().minus(Duration.ofDays(30)):start.toInstant();
        Instant endTime = start.toInstant().isBefore(Instant.parse("2017-01-01T00:00:00Z"))?
                Instant.now().plus(Duration.ofDays(1)):end.toInstant().plus(Duration.ofDays(1));

        String category = cate.trim().toLowerCase();
        List<String> units = Arrays.asList("year","month","week","triggerid");
        if (!units.contains(category)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only "+String.join(",",units)+ "are valid units");
        }
        String[] id = new String[]{"pipelineName","year"};
        if(!(category.equals("year") || category.equals("triggerid"))){
            id = new String[]{"pipelineName","year",category};
        }
        if(category.equals("triggerid")){
            id = new String[] {"pipelineName","year","triggerId"};
        }
        Aggregation aggregation = newAggregation(
                match(where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))
                        .and("name").regex(Pattern.compile(name.trim(),Pattern.CASE_INSENSITIVE))
                        .and("startTime").gt(startTime).lte(endTime)
                ),
                unwind("deployStatus"),
                project("startTime","triggerId","pipelineName",
                        "deployStatus.status","deployStatus.appliance", "deployStatus.type")
                        .andExpression("year(startTime)").as("year")
                        .andExpression("month(startTime)").as("month")
                        .andExpression("week(startTime)").as("week")
                        .andExclude("_id"),
                group(id).push("$$ROOT").as("deployments").count().as("totalDeploymentCount"),
                sort(Sort.Direction.ASC,id),
                limit(limit)
        );

        AggregationResults<BasicDBObject> results =
                mongoOps.aggregate(aggregation, DeploymentJobResult.class,BasicDBObject.class);
        results.getMappedResults().forEach(result->{
            List<Object> failedDeployments = ((BasicDBList) result.get("deployments"))
                    .stream().filter(deployment->{
                BasicDBObject deploy =(BasicDBObject)deployment;
                String status = deploy.getString("status");
                if(status==null||status.isEmpty()){
                    return true;
                }
                return !status.trim().toLowerCase().contains("installed");
            }).map(deployment->{
                BasicDBObject deploy = (BasicDBObject) deployment;
                return new BasicDBObject()
                        .append("triggerId",deploy.get("triggerId"))
                        .append("appliance",deploy.get("appliance"))
                        .append("startTime",deploy.get("startTime"));
            }).collect(toList());

            result.put("failedDeploymentCount",failedDeployments.size());
            result.put("successRate",1-failedDeployments.size() * 1.0/(Integer) result.get("totalDeploymentCount"));
            if(category.equals("year")){
                result.put("category",result.get("year"));
            }else if(category.equals("triggerid")){
                result.put("category",result.get("triggerId"));
                result.remove("triggerId");
            }else if(category.equals("week")){
                result.put("category",result.get("year").toString()+"-WK"+result.get(category));
            }
            else{
                result.put("category",result.get("year").toString()+"-"+result.get(category));
            }
            result.remove("year");
            result.remove(category);
            result.remove("deployments");

        });
        return results.getMappedResults();
    }
}


