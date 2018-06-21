package testcases.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.api.util.RMT2File;
import com.api.util.RMT2String2;

/**
 * Tests the availability and functionality of application configuration.
 * 
 * @author Roy Terrell
 *
 */
public class CommonAppServerConfigurationTest {
    protected static String CONFIG_FILENAME;

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
            xmlDoc = RMT2File.getTextFileContents(CONFIG_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error loading application configuration file, " + CONFIG_FILENAME);
        }
        Assert.assertNotNull(xmlDoc);
        Assert.assertTrue(RMT2String2.isNotEmpty(xmlDoc));
        System.out.print(xmlDoc);
    }

}
