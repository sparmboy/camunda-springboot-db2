package com.example.camundapoc.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by spencer on 13/03/2017.
 */
@Configuration
@EnableTransactionManagement
public class CamundaConfig {

    @Value("${spring.datasource.driver-class-name}")String dbDriver;
    @Value("${spring.datasource.url}")String dbUrl;
    @Value("${spring.datasource.username}")String dbUsername;
    @Value("${spring.datasource.password}")String dbPassword;


    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();

        dataSource.setDriverClass(dbDriver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public StrongUuidGenerator uuidGenerator() {
        return new StrongUuidGenerator();
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();

        // Enable UUID generation
        processEngineConfiguration.setIdGenerator(uuidGenerator());

        processEngineConfiguration.setDataSource(dataSource());
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        processEngineConfiguration.setTransactionManager(transactionManager());

        // Specify where to look for process definitions
        processEngineConfiguration.setDeploymentResources(new ClassPathResource[]{new ClassPathResource("processes/example.bpmn")});

        return processEngineConfiguration;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
