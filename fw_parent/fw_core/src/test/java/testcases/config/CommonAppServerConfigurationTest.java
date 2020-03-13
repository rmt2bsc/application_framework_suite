package testcases.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.api.config.ConfigConstants;
import com.api.config.jaxb.AppServerConfig;
import com.api.util.RMT2File;
import com.api.util.RMT2String2;
import com.api.xml.jaxb.JaxbUtil;

/**
 * Tests the availability and functionality of application configuration.
 * 
 * @author Roy Terrell
 *
 */
public class CommonAppServerConfigurationTest {
    protected static String CONFIG_FILENAME = "xml/RMT2AppServerConfig.xml";

    @Before
    public void setUp() throws Exception {
        // Be sure to set CONFIG_FILENAME variable here at the descendent...
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfigExistence() {
        Assert.assertNotNull(CONFIG_FILENAME);
        String xmlDoc = null;
        try {
            xmlDoc = RMT2File.getFileContentsAsString(CONFIG_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error loading application configuration file, " + CONFIG_FILENAME);
        }
        Assert.assertNotNull(xmlDoc);
        Assert.assertTrue(RMT2String2.isNotEmpty(xmlDoc));
        System.out.print(xmlDoc);
    }

    @Test
    public void testUnmarshallXml() {
        String xmlDoc = null;
        try {
            xmlDoc = RMT2File.getFileContentsAsString(CONFIG_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error loading application configuration file, " + CONFIG_FILENAME);
        }
        JaxbUtil jaxb = new JaxbUtil(ConfigConstants.JAXB_CONFIG_PKG);
        AppServerConfig appConfig = null;
        try {
            appConfig = (AppServerConfig) jaxb.unMarshalMessage(xmlDoc);
        } catch (Exception e) {
            Assert.fail("Error unmarshalling XML document to JAXB object");
        }
        Assert.assertNotNull(appConfig.getSystemProperties().getEnvironment());
        Assert.assertEquals("org.apache.xerces.parsers.SAXParser", appConfig.getSystemProperties().getSaxDriver());
        Assert.assertNotNull(appConfig.getCompanyProperties());
        Assert.assertEquals("1343", appConfig.getCompanyProperties().getContactId());
        Assert.assertEquals("RMT2 Business Systems Corp.", appConfig.getCompanyProperties().getCompanyName());
        Assert.assertEquals("rmt2bsc@gmail.com", appConfig.getCompanyProperties().getContactEmail());
        Assert.assertEquals("rmt2.net", appConfig.getCompanyProperties().getWebsite());
        Assert.assertEquals("net.sf.saxon.TransformerFactoryImpl", appConfig.getSystemProperties().getXsltTransformer());

    }
}
