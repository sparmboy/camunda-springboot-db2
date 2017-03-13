package com.example.camundapoc;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by spencer on 13/03/2017.
 */
@SpringBootApplication
@EnableProcessApplication
public class CamundaApp {
    public static void main(String... args) {
        SpringApplication.run(CamundaApp.class, args);
    }
}
