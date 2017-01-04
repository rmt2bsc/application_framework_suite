package testcases.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import testcases.TestCaseConstants;

import com.api.xml.RMT2XmlUtility;

/**
 * Parses and prints XML documents using XMLUtility
 * 
 * @author Roy Terrell
 * 
 */
public class JdomApiTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseAndPrintPrettyDocument() {
        String strOutput = "";
        Document document;
        String fileName = TestCaseConstants.DATA_DIR + "sales_order.xml";
        try {
            // The JAXP way of parsing
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = f.newDocumentBuilder();
            document = parser.parse(fileName);

            strOutput = RMT2XmlUtility.printDocumentWithJdom(document, true,
                    false);
            System.out.println(strOutput);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void parseAndPrintCompactDocument() {
        String strOutput = "";
        Document document;
        String fileName = TestCaseConstants.DATA_DIR + "sales_order.xml";
        try {
            // The JAXP way of parsing
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = f.newDocumentBuilder();
            document = parser.parse(fileName);

            strOutput = RMT2XmlUtility.printDocumentWithJdom(document, false,
                    true);
            System.out.println(strOutput);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
