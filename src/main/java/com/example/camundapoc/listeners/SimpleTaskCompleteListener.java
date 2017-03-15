package com.example.camundapoc.listeners;

import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

/**
 * Created by spencer on 14/03/2017.
 */
public class SimpleTaskCompleteListener implements TaskListener{
    public static final String DO_RULE_EXECUTION = "doRuleExecution";

    @Override
    public void notify(DelegateTask delegateTask) {
        ProcessEngineServices services = delegateTask.getExecution().getProcessEngineServices();
        String caseInstanceId = services.getRuntimeService().createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult().getCaseInstanceId();

        // Add the result of the Simple Task
        //services.getCaseService().setVariable(caseInstanceId,DO_RULE_EXECUTION,true);
        //services.getCaseService().manuallyStartCaseExecution(caseInstanceId);
    }
}
