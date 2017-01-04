package testcases.xml;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import testcases.TestCaseConstants;

import com.api.xml.RMT2XmlUtility;
import com.util.RMT2File;

//import com.ibm.xml.parsers.DOMParser;

public class RMT2XmlApiTest {

    private String xml;
    private String saleOrderWithItemsXml;
    private String saleOrderWithOutItemsXml;
    private String personXml;

    @Before
    public void setUp() throws Exception {
        // Get application server configuration file
        try {
            this.xml = RMT2File.getTextFileContents(TestCaseConstants.DATA_DIR
                    + "payload_example2.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.saleOrderWithItemsXml = "<SalesOrderExtTest><beanClassName>testcases.bean.SalesOrderExtTest</beanClassName><className /><customerId>1010</customerId><dateCreated>3-12-2008</dateCreated><dateUpdated>4-1-2008</dateUpdated><salesOrderId>555</salesOrderId><invoiced>0</invoiced><items><SalesOrderItems><beanClassName>testcases.bean.SalesOrderItems</beanClassName><backOrderQty>0.0</backOrderQty><className /><criteriaAvailable>false</criteriaAvailable><customCriteriaAvailable>false</customCriteriaAvailable><dataSourceClassName>testcases.bean.SalesOrderItems</dataSourceClassName><dataSourceName>SalesOrderItemsView</dataSourceName><dataSourceRoot>SalesOrderItems</dataSourceRoot><fileName /><id>0</id><inClauseAvailable>false</inClauseAvailable><initMarkup>1.0</initMarkup><initUnitCost>1080.0</initUnitCost><itemMasterId>157</itemMasterId><itemNameOverride>Terrell, Roy for 47.0 hours</itemNameOverride><jspOrigin /><methodName /><orderByAvailable>false</orderByAvailable><orderQty>1.0</orderQty><resultsetType>0</resultsetType><salesOrderId>555</salesOrderId><serialPath /><serializeXml>false</serializeXml></SalesOrderItems><SalesOrderItems><beanClassName>testcases.bean.SalesOrderItems</beanClassName><backOrderQty>0.0</backOrderQty><className /><criteriaAvailable>false</criteriaAvailable><customCriteriaAvailable>false</customCriteriaAvailable><dataSourceClassName>testcases.bean.SalesOrderItems</dataSourceClassName><dataSourceName>SalesOrderItemsView</dataSourceName><dataSourceRoot>SalesOrderItems</dataSourceRoot><fileName /><id>0</id><inClauseAvailable>false</inClauseAvailable><initMarkup>1.0</initMarkup><initUnitCost>1200.0</initUnitCost><itemMasterId>157</itemMasterId><itemNameOverride>Terrell, Roy for 80.0 hours</itemNameOverride><jspOrigin /><methodName /><orderByAvailable>false</orderByAvailable><orderQty>15.0</orderQty><resultsetType>0</resultsetType><salesOrderId>555</salesOrderId><serialPath /><serializeXml>false</serializeXml></SalesOrderItems></items><methodName /><orderTotal>4440.0</orderTotal><userId /></SalesOrderExtTest>";
        this.saleOrderWithOutItemsXml = "<SalesOrderExtTest><beanClassName>testcases.bean.SalesOrderExtTest</beanClassName><className /><customerId>1010</customerId><dateCreated>3-12-2008</dateCreated><dateUpdated>4-1-2008</dateUpdated><id>444</id><invoiced>0</invoiced><methodName /><orderTotal>3240.0</orderTotal><userId /></SalesOrderExtTest>";
        this.personXml = "<profiles><person><firstname>Roy</firstname><lastname>Terrell</lastname><data><partno>94329</partno><description>Intel Dual Core Extreme 9000</description><price>432.33</price></data></person><person><firstname>Billy</firstname><lastname>Cobham</lastname><data><partno>43223</partno><description>Best Fusion Drummer</description><price>32.34</price></data></person><person><firstname>Dennis</firstname><lastname>Chambers</lastname><data><partno>83838</partno><description>Most in-demand drummer</description><price>843.48</price></data></person></profiles>";
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void queueApi() {
        String xml = "<RQ_business_contact_search><header><message_id>RQ_business_contact_search</message_id><application>contacts</application><module>business</module><transaction>getBusiness</transaction><delivery_mode>SYNC</delivery_mode><message_mode>REQUEST</message_mode><delivery_date>02/13/2016 14:36:54</delivery_date><user_id>admin</user_id></header><criteria_data><business_id>1430</business_id><entity_type/><service_type/></criteria_data></RQ_business_contact_search>";
        List<String> value = RMT2XmlUtility.getElementValue("application",
                "//header", xml);
        Assert.assertEquals(1, value.size());
    }

    @Test
    public void parseDocument() {

        // Extract single value
        List<String> value = RMT2XmlUtility.getElementValue(
                "InitialContextFactory", "//System", this.xml);
        Assert.assertNotNull(value);
        Assert.assertEquals(2, value.size());
        Assert.assertEquals(
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory",
                value.get(0));

        // Extact single value starting at the document root
        value = RMT2XmlUtility.getElementValue("TimeoutInterval",
                "//AppServerConfig", this.xml);
        Assert.assertNotNull(value);
        Assert.assertEquals(1, value.size());
        Assert.assertEquals("1800", value.get(0));

        // Extact single value where the element is empty
        value = RMT2XmlUtility.getElementValue("MaxConnectAttemps", "//System",
                this.xml);
        Assert.assertNotNull(value);
        Assert.assertEquals(2, value.size());
        Assert.assertEquals("", value.get(0));

        // Return XML document
        value = RMT2XmlUtility.getElementValue("EmailConfig",
                "//SystemProperties", this.xml);
        Assert.assertNotNull(value);
        Assert.assertEquals(1, value.size());
        Assert.assertNotNull(value.get(0));

        // Not Found
        value = RMT2XmlUtility.getElementValue("RoyRoy", "//System", this.xml);
        Assert.assertNull(value);

        // Not Found
        value = RMT2XmlUtility.getElementValue("InitialContextFactory",
                "//System/InitialContextFactory", this.xml);
        Assert.assertNull(value);
    }
}
