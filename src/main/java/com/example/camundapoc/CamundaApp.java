package com.example.camundapoc;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;


@SpringBootApplication
public class CamundaApp extends SpringBootProcessApplication {
    private static final String DEMO_NAME = "demo";
    private static final String TEST_GROUP_ID = "test-group";
    private static final String TEST_GROUP_NAME = "Test Group";
    private static final String WORKFLOW_TYPE = "WORKFLOW";
    private static final String FILTER_DESCRIPTION = "description";
    private static final String FILTER_VARIABLES = "variables";

    public static void main(String... args) {
        SpringApplication.run(CamundaApp.class, args);
    }

    public final static Logger LOGGER = LoggerFactory.getLogger(CamundaApp.class);

    @Autowired
    IdentityService identityService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    TaskService taskService;

    @Autowired
    FilterService filterService;

    @PostDeploy
    public void init() {
        if (identityService.isReadOnly()) {
            LOGGER.info("Identitfy service provider is Read Only, not creating any demo users.");
            return;
        }

        User singleResult = identityService.createUserQuery().userId(DEMO_NAME).singleResult();
        if (singleResult != null) return;

        LOGGER.info("Generating demo data for invoice showcase");

        User user = identityService.newUser(DEMO_NAME);
        user.setFirstName(DEMO_NAME);
        user.setLastName(DEMO_NAME);
        user.setPassword(DEMO_NAME);
        user.setEmail(DEMO_NAME + "@example.com");
        identityService.saveUser(user);

        // Create the group
        if (identityService.createGroupQuery().groupId(Groups.CAMUNDA_ADMIN).count() == 0) {
            Group camundaAdminGroup = identityService.newGroup(Groups.CAMUNDA_ADMIN);
            camundaAdminGroup.setType(Groups.GROUP_TYPE_SYSTEM);
            identityService.saveGroup(camundaAdminGroup);
        }

        // create ADMIN authorizations on all built-in resouces
        for (Resource resource : Resources.values()) {
            if (authorizationService.createAuthorizationQuery().groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count() == 0) {
                AuthorizationEntity userAdminAuth = new AuthorizationEntity(Authorization.AUTH_TYPE_GRANT);
                userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN);
                userAdminAuth.setResource(resource);
                userAdminAuth.setResourceId(ANY);
                userAdminAuth.addPermission(Permissions.ALL);
                authorizationService.saveAuthorization(userAdminAuth);
            }
        }

        identityService.createMembership(DEMO_NAME,Groups.CAMUNDA_ADMIN);

        Group testGroup = identityService.newGroup(TEST_GROUP_ID);
        testGroup.setName(TEST_GROUP_NAME);
        testGroup.setType(WORKFLOW_TYPE);
        identityService.saveGroup(testGroup);

        identityService.createMembership(DEMO_NAME,TEST_GROUP_ID);

        Map<String,Object> filterProperties = new HashMap<>();
        filterProperties.put(FILTER_DESCRIPTION,TEST_GROUP_NAME);
        List<Object> vars = new ArrayList<>();
        filterProperties.put(FILTER_VARIABLES,vars);

        TaskQuery query = taskService.createTaskQuery().taskCandidateGroup(TEST_GROUP_ID);
        Filter myTasksFilter = filterService.newTaskFilter("Test Group Tasks").setProperties(filterProperties);
        filterService.saveFilter(myTasksFilter);
    }
}
