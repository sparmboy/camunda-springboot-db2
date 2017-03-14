package com.example.camundapoc.processes;

import com.example.camundapoc.BaseIntegrationTest;
import com.example.camundapoc.domain.ResultEntity;
import com.example.camundapoc.service.ResultEntityService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.AbstractMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for proving Camunda Process Instance can call out to start a Case
 * and that Case in turn can call out to a Process instance
 *
 * Created by spencer on 14/03/2017.
 */
public class CaseToBpmToCaseIntegrationTest extends BaseIntegrationTest {


    private static final String TEST_PROCESS_KEY = "TestProcess";
    private static final String SIMPLE_PROCESS_KEY = "SimpleProcess";
    private static final String TEST_CASE_KEY = "TestCasePlan";
    private static final String START_VARIABLE = "startVariable";

    @Autowired
    private ResultEntityService resultEntityService;

    @Test
    public void shouldStartTestProcess() {

        // Init test variables
        Object testValue = 5;

        // Start the process
        ProcessInstance procInst = startProcessByKey(
                TEST_PROCESS_KEY,
                new AbstractMap.SimpleEntry<>(START_VARIABLE,testValue)
                );
        assertNotNull( procInst );

        // Check that the case was started.
        assertEquals(1, getActiveCaseInstanceCountByKey(TEST_CASE_KEY));
        CaseInstance caseInst = getActiveCaseInstanceByKey(TEST_CASE_KEY);

        // Check that the simple BPM process was called from the case. Complete the task inside
        assertEquals(1,getProcessInstanceCountByKey(SIMPLE_PROCESS_KEY));
        Task task = getActiveTaskInProcessByProcDefKey(SIMPLE_PROCESS_KEY);
        assertNotNull(task);

        // Complete the task in the simple process and that should trigger the rules in the case
        completeTask(task.getId());

        // Complete the verify rule result task and check the case has ended
        task = getActiveTaskInCaseByCaseDefKey(TEST_CASE_KEY);
        assertNotNull(task);
        completeTask(task.getId());
        assertCaseEndedByCaseInstanceId(caseInst.getId());

        // Finally the service in the original process should now have been called
        assertEquals(1,resultEntityRepo.count());
        ResultEntity resultEntity = resultEntityRepo.findAll().iterator().next();
        assertEquals("Value is smaller than 10" , resultEntity.getResult());
    }



}
