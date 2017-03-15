package com.example.camundapoc;

import com.example.camundapoc.database.hibernate.repositories.ResultEntityRepository;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Base class for all integration tests that require the entire app to be started up.
 * These tests are used to ensure the spring wiring and config is correct
 *
 * Created by spencer on 14/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CamundaApp.class)
@ActiveProfiles({"default", "test-resources"})
public abstract class BaseIntegrationTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected CaseService caseService;

    @Autowired
    protected DecisionService decisionService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected ResultEntityRepository resultEntityRepo;

    @Before
    public void deleteAllProcesses() {
        LOGGER.info("Deleting all process instances");
        List<ProcessInstance> procInsts = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance inst : procInsts) {
            runtimeService.deleteProcessInstance(inst.getId(), "testIniitialisation");
        }
    }

    @Before
    public void terminateAllCases() {
        LOGGER.info("Terminating all active cases");
        List<CaseInstance> caseInsts = caseService.createCaseInstanceQuery().active().list();
        for (CaseInstance inst : caseInsts) {
            if( !inst.isTerminated() ) {
                caseService.terminateCaseExecution(inst.getId());
            }
        }
    }

    @Before
    public void deleteAllEntities() {
        ManagementService ms;
        LOGGER.info("Deleting all entities for repositories");
        resultEntityRepo.deleteAll();
    }


    /**
     * Helper wrapper for starting a process specified by the process definition key and array of variable values. The created process intance is returned
     *
     * @param procDefKey
     * @param variableValues
     * @return The process instance created
     */
    protected ProcessInstance startProcessByKey(String procDefKey, AbstractMap.SimpleEntry<String, Object>... variableValues) {
        Map<String, Object> vars = new HashMap<>();
        for (AbstractMap.SimpleEntry<String, Object> var : variableValues) {
            vars.put(var.getKey(), var.getValue());
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procDefKey, vars);
        assertNotNull(processInstance);
        return processInstance;
    }

    /**
     * Helper wrapper for starting a case specified by the case definition key and array of variable values. The created case instance is returned
     *
     * @param caseDefKey
     * @param variableValues
     * @return The process instance created
     */
    protected CaseInstance startCaseByKey(String caseDefKey, AbstractMap.SimpleEntry<String, Object>... variableValues) {
        Map<String, Object> vars = new HashMap<>();
        for (AbstractMap.SimpleEntry<String, Object> var : variableValues) {
            vars.put(var.getKey(), var.getValue());
        }
        CaseInstance caseInstance = caseService.createCaseInstanceByKey(caseDefKey, vars);
        assertNotNull(caseInstance);
        return caseInstance;
    }

    /**
     * Completes the specified task with the passed in the variables
     * @param taskId
     * @param variableValues
     */
    protected void completeTask(String taskId, AbstractMap.SimpleEntry<String, Object>... variableValues) {
        Map<String, Object> vars = new HashMap<>();
        for (AbstractMap.SimpleEntry<String, Object> var : variableValues) {
            vars.put(var.getKey(), var.getValue());
        }
        taskService.complete(taskId, vars);
    }

    /**
     * Completes the specified task
     * @param taskId
     */
    protected void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * REturns the number of case instances created for the specified case key
     *
     * @param caseDefKey
     * @return
     */
    protected long getActiveCaseInstanceCountByKey(String caseDefKey) {
        return caseService.createCaseInstanceQuery().caseDefinitionKey(caseDefKey).active().count();
    }

    protected CaseInstance getActiveCaseInstanceByKey(String caseDefKey) {
        return caseService.createCaseInstanceQuery().caseDefinitionKey(caseDefKey).active().singleResult();
    }

    protected Task getActiveTaskInCaseByCaseDefKey(String caseDefKey) {
        Task task = taskService.createTaskQuery().active().caseDefinitionKey(caseDefKey).singleResult();
        assertNotNull(task);
        return task;
    }

    protected Task getActiveTaskByTaskDefKey(String taskDefKey) {
        Task task = taskService.createTaskQuery().active().taskDefinitionKey(taskDefKey).singleResult();
        assertNotNull(task);
        return task;
    }

    protected Task getActiveTaskInProcessByProcDefKey(String procDefKey) {
        Task task =  taskService.createTaskQuery().processDefinitionKey(procDefKey).active().singleResult();
        assertNotNull(task);
        return task;
    }

    protected void assertCaseEndedByCaseInstanceId(String caseInstanceId) {
        assertTrue(caseService.createCaseInstanceQuery().caseInstanceId(caseInstanceId).singleResult().isCompleted());
    }


    /**
     * Returns the number of process instances instances created for the specified case key
     *
     * @param procDefKey
     * @return
     */
    protected long getProcessInstanceCountByKey(String procDefKey) {
        return runtimeService.createProcessInstanceQuery().processDefinitionKey(procDefKey).active().count();
    }
}
