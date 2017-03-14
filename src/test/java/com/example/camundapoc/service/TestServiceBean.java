package com.example.camundapoc.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Simple test service bean to be called inline by a Camunda process instance
 *
 * Created by spencer on 14/03/2017.
 */
@Service("testServiceBean")
public class TestServiceBean {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(List<Map<String,Object>> resultList) {
        this.result = ""+ resultList.get(0).get("rulesResult");
    }
}
