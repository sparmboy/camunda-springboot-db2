package com.example.camundapoc.service.impl;

import com.example.camundapoc.database.hibernate.repositories.ResultEntityRepository;
import com.example.camundapoc.domain.ResultEntity;
import com.example.camundapoc.service.ResultEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by spencer on 14/03/2017.
 */
@Service("resultEntityService")
@Transactional
public class ResultEntityServiceImpl implements ResultEntityService {

    @Autowired
    ResultEntityRepository resultRepo;

    @Override
    public ResultEntity createResultEntity(String result) {
        ResultEntity resultEntity = new ResultEntity(result);
        return resultRepo.save(resultEntity);
    }
}
