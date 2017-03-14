package com.example.camundapoc;

import com.example.camundapoc.helpers.CamundaDecisionTableHelper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Base class to be extended when testing a DMN decision table
 *
 * Created by spencer on 14/03/2017.
 */
@RunWith(JUnitParamsRunner.class)
public abstract class BaseRulesIntegrationTest extends BaseIntegrationTest {

    private static final String START_VARIABLE = "startVariable";
    private static final String OUTPUT_VAR_NAME = "rulesResult";

    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    /**
     * Return the DMN decision table file to be tested
     * @return
     */
    public abstract File getDecisionTableFile();

    /**
     * Define the input parameter values and expected results. An example could be:
     *
     * <code>return Arrays.asList(new Object[][] {
     { 3, "Value is smaller than 10" },
     { 10, "Value is equal to 10" },
     { 12, "Value is bigger than 10" }
     });</code>
     * @return
     */
    public abstract Collection<Object[]> parametersForTestRule();

    /**
     * Define the key of the decision table to test in the DMN file
     * @return
     */
    protected abstract String getDecisionTableKey();

    /**
     * This tests a basic input value and String expected value. This should be overridden to produce more complex testing
     * @param inputValue
     * @param expectedValue
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    @Test
    @Parameters
    public void testRule(int inputValue, String expectedValue) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        Map<String, Object> vars = new HashMap<>();
        vars.put(START_VARIABLE,inputValue);
        DmnDecisionTableResult result = decisionService.evaluateDecisionTableByKey(getDecisionTableKey(),vars);
        assertEquals(expectedValue, result.getFirstResult().get(OUTPUT_VAR_NAME));
    }

    /**
     * Introspects the specified DMN decision table file to count the defined rules and asserts that
     * against the number of input values specified.
     *
     * TODO: Might be more diligent to actually check which rules were executed in the engine somehow?
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    @Test
    public void shouldHaveInputForEveryRule() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        File decisionTableFileToTest = getDecisionTableFile();
        String decisionTableKey = getDecisionTableKey();
        int rulesExecuted =  parametersForTestRule().size();
        int rulesDefined = CamundaDecisionTableHelper.getRuleCountInDecisionTable(decisionTableFileToTest,decisionTableKey);
        assertEquals(String.format("Only %d rules were tested out of the %d that are defined in %s",rulesExecuted,rulesDefined,decisionTableFileToTest),rulesExecuted, rulesDefined );
    }

}
