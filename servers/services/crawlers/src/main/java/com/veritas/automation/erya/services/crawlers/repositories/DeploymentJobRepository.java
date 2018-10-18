package com.veritas.automation.erya.services.crawlers.repositories;

import com.veritas.automation.erya.services.crawlers.entities.CiResult;
import com.veritas.automation.erya.services.crawlers.entities.DeploymentJobResult;
import com.veritas.automation.erya.services.crawlers.util.Constants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by Dean.Zou on 2/9/2017.
 */

@RepositoryRestResource(collectionResourceRel = Constants.REST_SEG_CI_DEPLOYMENTS, path = Constants.REST_SEG_CI_DEPLOYMENTS)
public interface DeploymentJobRepository extends MongoRepository<DeploymentJobResult,String> {
    @Override
    @RestResource(exported = false)
    void delete(String s);

    @Override
    @RestResource(exported = false)
    void delete(DeploymentJobResult entity);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends DeploymentJobResult> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
