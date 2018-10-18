package com.veritas.automation.erya.services.crawlers.repositories;

import com.veritas.automation.erya.services.crawlers.entities.TestJobResult;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by Dean.Zou on 2/9/2017.
 */

@RepositoryRestResource(collectionResourceRel =Constants.REST_SEG_CI_TESTS,  path = Constants.REST_SEG_CI_TESTS)
public interface TestJobRepository extends MongoRepository<TestJobResult,String> {
    @Override
    @RestResource(exported = false)
    void delete(String s);

    @Override
    @RestResource(exported = false)
    void delete(TestJobResult entity);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends TestJobResult> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
