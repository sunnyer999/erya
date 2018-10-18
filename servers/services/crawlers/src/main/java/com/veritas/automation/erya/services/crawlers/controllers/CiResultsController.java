package com.veritas.automation.erya.services.crawlers.controllers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.veritas.automation.erya.services.crawlers.entities.CiResult;
import com.veritas.automation.erya.services.crawlers.repositories.CiResultRepository;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Dean.Zou on 2/6/2017.
 *
 */

@RepositoryRestController()
@RequestMapping(value = Constants.REST_SEG_CI_STATS)
public class CiResultsController extends CrawlerController {

    private final MongoOperations mongoOps;

    private final CiResultRepository ciResults;

    @Autowired
    public CiResultsController(MongoOperations ops, CiResultRepository repo){
        this.mongoOps=ops;
        this.ciResults=repo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overall")
    @ResponseBody
    public Object searchStatsSuccessRate(
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam(value = "pipeline", defaultValue = ".*") String pipeline) {

        Aggregation aggregation = newAggregation(
                match(where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))),
                sort(Sort.Direction.DESC,"triggerId"),
                limit(limit),
                unwind("testJobs"),
                project(from(field("triggerId"),field("pipelineName"),
                        field("jobName","testJobs.name"),field("jobDuration","testJobs.duration"),
                        field("jobPassRate","testJobs.passRate")
                )).andExclude("_id"),
                group("jobName","pipelineName").push("$$ROOT").as("tests"),
                sort(Sort.Direction.DESC,"jobName")
        );
        AggregationResults<BasicDBObject> results =
                mongoOps.aggregate(aggregation, CiResult.class,BasicDBObject.class);
        return  results.getMappedResults();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test-jobs/{pipeline}")
    @ResponseBody
    public Object searchJobStats(
            @RequestParam(value = "limit", required = false, defaultValue = "6") int limit,
            @PathVariable String pipeline){
        Aggregation aggregation = newAggregation(
                match(where("pipelineName").regex(Pattern.compile(pipeline.trim(),Pattern.CASE_INSENSITIVE))),
                sort(Sort.Direction.DESC,"triggerId"),
                limit(200),
                unwind("testJobs"),
                project(from(
                        field("jobName","testJobs.name"),
                        field("elapse","testJobs.duration"),
                        field("testJobs.passRate"),field("triggerId")
                        )).andExclude("_id"),
                group("jobName").push("$$ROOT").as("jobs")
        );
        List<BasicDBObject> results =
                mongoOps.aggregate(aggregation, CiResult.class,BasicDBObject.class).getMappedResults();

        return results.stream().map(dbObj->{
            List<BasicDBObject> jobs = ((BasicDBList)dbObj.get("jobs")).stream()
                    .map(job->{
                        ((BasicDBObject) job).remove("jobName");
                        return (BasicDBObject)job;
                    }).limit(limit).collect(toList());
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


}
