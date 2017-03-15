package com.example.camundapoc.listeners;

import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by spencer on 14/03/2017.
 */
public class RulesCompleteListener implements CaseExecutionListener {

    private static Logger LOGGER = LoggerFactory.getLogger(RulesCompleteListener.class);

    @Override
    public void notify(DelegateCaseExecution caseExecution) throws Exception {
        LOGGER.info("Rules " + caseExecution.getActivityName() + " executed");
    }
}
