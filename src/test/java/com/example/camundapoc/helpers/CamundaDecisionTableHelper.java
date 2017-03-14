package com.example.camundapoc.helpers;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Helper class to introspect camunda decision tables
 *
 * Created by spencer on 14/03/2017.
 */
public class CamundaDecisionTableHelper {

    private final static String RULES_XPATH = "/definitions/decision[@id=\"%s\"]/decisionTable/rule";

    @Test
    public void shouldFetchRuleCount() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        assertEquals(3,getRuleCountInDecisionTable(new File("src/test/resources/rules/decision-table.dmn"),"decisionTableKey"));
    }

    public static int getRuleCountInDecisionTable(File dmnFile, String decisionTableKey) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        Document doc = openDmnFileForReading(dmnFile);
        Object result = evalXpathOnXml(doc, String.format(RULES_XPATH,decisionTableKey ));
        return ((DTMNodeList) result).getLength();
    }

    private static Object evalXpathOnXml(Document xml, String xpathText) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathText);
        return expr.evaluate(xml,  XPathConstants.NODESET);
    }

    private static Document openDmnFileForReading(File dmnFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new FileInputStream(dmnFile));
    }

}
