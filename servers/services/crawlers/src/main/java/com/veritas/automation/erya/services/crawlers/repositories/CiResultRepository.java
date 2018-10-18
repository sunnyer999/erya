package com.veritas.automation.erya.services.crawlers.repositories;

import com.veritas.automation.erya.services.crawlers.entities.CiResult;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


/**
 * Created by Dean.Zou on 2/8/2017.
 */


@RepositoryRestResource(collectionResourceRel = Constants.REST_SEG_CI_STATS, path = Constants.REST_SEG_CI_STATS)
public interface CiResultRepository extends MongoRepository<CiResult,String> {
    @Override
    @RestResource(exported = false)
    void delete(String s);

    @Override
    @RestResource(exported = false)
    void delete(CiResult entity);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends CiResult> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @RestResource(path="pipeline")
    Page<CiResult> findByPipelineNameIgnoreCase(@Param("name") String name, Pageable p);
}
