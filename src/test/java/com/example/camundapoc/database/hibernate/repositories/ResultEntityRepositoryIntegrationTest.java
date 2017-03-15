package com.example.camundapoc.database.hibernate.repositories;

import com.example.camundapoc.BaseIntegrationTest;
import com.example.camundapoc.domain.ResultEntity;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by spencer on 14/03/2017.
 */
public class ResultEntityRepositoryIntegrationTest extends BaseIntegrationTest {

    @Test
    public void shouldCreateResultEntity() {
        String result = "TEST";
        ResultEntity resultEntity = new ResultEntity(result);
        resultEntity = resultEntityRepo.save(resultEntity);
        assertNotNull(resultEntity);
        assertNotNull(resultEntity.getId());
        assertEquals(result,resultEntity.getResult());

        assertEquals(1,resultEntityRepo.count());
    }

}