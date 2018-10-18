package com.veritas.automation.erya.services.crawlers.controllers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.veritas.automation.erya.services.crawlers.entities.RobotResult;
import com.veritas.automation.erya.services.crawlers.entities.TestJobResult;
import com.veritas.automation.erya.services.crawlers.repositories.TestJobRepository;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Dean.Zou on 2/6/2017.
 */

@RepositoryRestController
@RequestMapping(value = Constants.REST_SEG_CI_TESTS)
public class CiTestsController extends CrawlerController {

    private final MongoOperations mongoOps;

    private final TestJobRepository testsResults;

    @Autowired
    public CiTestsController(MongoOperations ops, TestJobRepository repo){
        this.mongoOps=ops;
        this.testsResults =repo;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/search/duration")
    @ResponseBody
    public List<TestJobResult> searchDuration(
            @RequestParam(value = "lt",defaultValue = "6000") int lt,
            @RequestParam(value = "gt",defaultValue = "0") int gt,
            @RequestParam(value = "limit",defaultValue = "200") int limit,
            @RequestParam(value = "pipeline",defaultValue = ".*") String pipeline){
        Query query = new Query(
                where("robotResults").elemMatch(where("duration")
                        .gt(Duration.ofMinutes(gt).toMillis()).lt(Duration.ofMinutes(lt).toMillis()))
                .orOperator(where("duration")
                        .gt(Duration.ofMinutes(gt).toMillis()).lt(Duration.ofMinutes(lt).toMillis()))
                .andOperator(where("pipelineName").regex(Pattern.compile(pipeline,Pattern.CASE_INSENSITIVE))))
                .with(new Sort( Sort.Direction.DESC,"pipelineName","triggerId") )
                .limit(limit);
        List<TestJobResult> results= mongoOps.find(query,TestJobResult.class);
        results.forEach(
                res->res.getRobotResults()
                .removeIf(rob->rob.getDuration()>Duration.ofMinutes(lt).toMillis()||
                        rob.getDuration()<Duration.ofMinutes(gt).toMillis()));
        return  results;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/elapse")
    @ResponseBody
    public Object statsElapse(
            @RequestParam(value = "limit", defaultValue = "200") int limit,
            @RequestParam(value = "pipeline", defaultValue = ".*") String pipeline) {

        Aggregation aggregation = newAggregation(
                match(where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))),
                unwind("robotResults"),
                sort(Sort.Direction.DESC,"buildId"),
                project(from(field("buildId"),field("pipelineName"),
                        field("jobUri","uri"),field("jobName","name"),
                        field("robotScenario","robotResults.scenario"),
                        field("robotDuration","robotResults.duration"),
                        field("robotResults.appliance"),
                        field("robotUri","robotResults.uri")
                        ))
                        .andExclude("_id"),
                group("robotScenario","pipelineName","jobName").push("$$ROOT").as("robots"),
                sort(Sort.Direction.DESC,"pipelineName","jobName","robotScenario"),
                limit(limit)
        );

        AggregationResults<BasicDBObject> results =
                mongoOps.aggregate(aggregation, TestJobResult.class,BasicDBObject.class);
        return  results.getMappedResults();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/robot")
    @ResponseBody
    public Object robotElapse(
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "job") String job) {

        Aggregation aggregation = newAggregation(
                match(where("name").regex(Pattern.compile(job.trim()+"$",Pattern.CASE_INSENSITIVE))),
                sort(Sort.Direction.DESC,"pipelineName","triggerId"),
                limit(limit)
        );

        AggregationResults<TestJobResult> results =
                mongoOps.aggregate(aggregation, TestJobResult.class, TestJobResult.class);
        return  results.getMappedResults().stream().map(result->{
            HashMap<String,Object> ret=new HashMap<>();
            ret.put("id",result.getId());
            ret.put("pipelineName",result.getPipelineName());
            ret.put("jobName",result.getName());
            ret.put("jobUri",result.getUri());
            ret.put("triggerId",result.getTriggerId());
            ret.put("robots",result.getRobotResults().stream()
                    .collect(Collectors.groupingBy(RobotResult::getScenario,
                            mapping(robot->{
                                HashMap<String,Object> map= new HashMap<>();
                                map.put("appliance",robot.getAppliance());
                                map.put("duration",robot.getDuration());
                                map.put("uri",robot.getUri());
                                map.put("generatedTime",robot.getGeneratedTime());
                                return map;
                            },toList()))));
            return ret;
        }).collect(toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/{pipeline}")
    @ResponseBody
    public Object searchTestStats(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @PathVariable String pipeline){
        Aggregation aggregation = newAggregation(

                match(where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))),
                group("triggerId").push(new BasicDBObject("name","$name")
                        .append("passRate","$passRate").append("elapse","$duration")
                        .append("failedCases","$failedCases").append("totalCases","$totalCases")
                        .append("triggerId","$triggerId"))
                        .as("testJobs"),
                sort(Sort.Direction.DESC,"_id"),
                limit(200),
                unwind("testJobs"),
                project(from(
                        field("jobName","testJobs.name"),
                        field("elapse","testJobs.elapse"),
                        field("testJobs.passRate"),field("triggerId","_id")
                )).andExclude("_id"),
                group("jobName").push(new BasicDBObject("triggerId","$triggerId").append("elapse","$elapse")
                    .append("passRate","$passRate")).as("jobs")
        );
        List<BasicDBObject> results =
                mongoOps.aggregate(aggregation, TestJobResult.class,BasicDBObject.class).getMappedResults();
        return results.stream().map(dbObj->{
            List<BasicDBObject> jobs = ((BasicDBList)dbObj.get("jobs")).stream()
                    .map(job-> (BasicDBObject)job).limit(limit).collect(toList());
            dbObj.put("avgElapse",  jobs.stream().filter(job-> ((Integer)job.get("elapse")).longValue()
                    > Duration.ofMinutes(5).toMillis())
                    .collect(averagingDouble(job->Float.parseFloat(job.get("elapse").toString()))));
            dbObj.put("avgPassRate",  jobs.stream()
                    .collect(averagingDouble(job->Float.parseFloat(job.get("passRate").toString()))));
            dbObj.put("minElapse",jobs.stream()
                    .mapToInt(job->Integer.parseInt(job.get("elapse").toString())).min().getAsInt());
            dbObj.put("maxElapse",jobs.stream()
                    .mapToInt(job->Integer.parseInt(job.get("elapse").toString())).max().getAsInt());
            dbObj.put("minPassRate",jobs.stream()
                    .mapToDouble(job->Double.parseDouble(job.get("passRate").toString())).min().getAsDouble());
            dbObj.put("maxPassRate",jobs.stream()
                    .mapToDouble(job->Double.parseDouble(job.get("passRate").toString())).max().getAsDouble());
            dbObj.put("jobName",dbObj.get("_id"));
            dbObj.putIfAbsent("histories",jobs);
            dbObj.remove("_id");
            dbObj.remove("jobs");
            return dbObj;
        }).collect(toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/{pipeline}/{triggerId}")
    @ResponseBody
    public Object searchTestStatsPerTriggerId(@PathVariable int triggerId, @PathVariable String pipeline){
        Criteria matchCriteria = where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE));
        LimitOperation limit = limit(1);
        if(triggerId > 0){
            matchCriteria.and("triggerId").is(triggerId);
        }else if(triggerId<0) {
            limit = limit(Math.abs(triggerId));
        }
        Aggregation aggregation = newAggregation(
                match(matchCriteria),
                group("triggerId").sum("$failedCases").as("totalFailedCases")
                        .sum("$passedCases").as("totalPassedCases")
                        .sum("$executedCases").as("totalExecutedCases")
                        .sum("$totalCases").as("totalExpectedCases")
                        .push(new BasicDBObject("name","$name")
                        .append("passRate","$passRate").append("elapse","$duration")
                        .append("buildId","$buildId").append("uri","$uri").append("totalCases","$totalCases")
                        .append("failedCases","$failedCases").append("executedCases","$executedCases")
                                .append("executedPassRate","$executedPassRate").append("passRate","$passRate"))
                        .as("scenarios"),
                sort(Sort.Direction.DESC,"_id"),
                limit,
                project(from(field("triggerId","_id"),field("totalFailedCases"),
                        field("totalPassedCases"),field("scenarios"),field("totalExecutedCases"),
                        field("totalExpectedCases")
                )).andExclude("_id")
        );
        List<BasicDBObject> results =
                mongoOps.aggregate(aggregation, TestJobResult.class,BasicDBObject.class).getMappedResults();
        results.forEach(result-> {
            int total = ((BasicDBList) result.get("scenarios")).stream().mapToInt(s -> {
                BasicDBObject scenario = (BasicDBObject) s;
                int totalCases = scenario.getInt("totalCases");
                int exCases = scenario.getInt("executedCases");
                if (totalCases < exCases) return exCases;
                return totalCases;
            }).sum();
            result.put("totalExpectedWithCoreDumpCases", total);
        });
        if(triggerId>=0){
            return results.get(0);
        }
        return results;

    }

}
