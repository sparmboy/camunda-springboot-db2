package com.example.camundapoc.rules;

import com.example.camundapoc.BaseRulesIntegrationTest;
import com.example.camundapoc.helpers.CamundaDecisionTableHelper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.repository.DecisionRequirementsDefinition;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Example test suite for testing a decision table definition file using parameterized tests
 *
 * Created by spencer on 14/03/2017.
 */
@RunWith(JUnitParamsRunner.class)
public class RulesIntegrationTest extends BaseRulesIntegrationTest {

    public final static String DECISION_TABLE_FILE = "src/test/resources/rules/decision-table.dmn";
    private static final String DECISION_TABLE_KEY = "decisionTableKey";

    @Override
    public File getDecisionTableFile() {
        return new File(DECISION_TABLE_FILE);
    }

    public Collection<Object[]> parametersForTestRule() {
        return Arrays.asList(new Object[][] {
                { 3, "Value is smaller than 10" },
                { 10, "Value is equal to 10" },
                { 12, "Value is bigger than 10" }
        });
    }

    @Override
    protected String getDecisionTableKey() {
        return DECISION_TABLE_KEY;
    }
}
