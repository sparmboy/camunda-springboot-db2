package com.example.camundapoc.database.hibernate.repositories;

import com.example.camundapoc.domain.ResultEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

/**
 * Created by spencer on 14/03/2017.
 */
@RepositoryRestResource(collectionResourceRel = "testEntities", path = "testEntities")
public interface ResultEntityRepository extends PagingAndSortingRepository<ResultEntity, String> {
}
