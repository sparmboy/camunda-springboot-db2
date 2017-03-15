package com.example.camundapoc.service;

import com.example.camundapoc.database.hibernate.repositories.ResultEntityRepository;
import com.example.camundapoc.domain.ResultEntity;
import com.example.camundapoc.service.impl.ResultEntityServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by spencer on 14/03/2017.
 */
public class ResultEntityServiceTest {

    @InjectMocks
    ResultEntityServiceImpl resultEntityService;

    @Mock
    ResultEntityRepository resultEntityRepo;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateResultEntity() throws Exception {
        // Given
        String result = "hello";
        ResultEntity entity = new ResultEntity(result);
        given(resultEntityRepo.save(Mockito.any(ResultEntity.class))).willReturn(new ResultEntity(result));

        // When
        ResultEntity resultEntity = resultEntityService.createResultEntity(result);

        // Then
        assertNotNull(resultEntity);
        assertEquals(result,resultEntity.getResult());
    }
}