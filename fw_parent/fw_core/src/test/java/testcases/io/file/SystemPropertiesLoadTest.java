package testcases.io.file;

import org.junit.Assert;
import org.junit.Test;

import testcases.TestCaseConstants;

import com.util.RMT2File;

/**
 * Test loading resources from file system and classpath
 * 
 * @author Roy Terrell
 * 
 */
public class SystemPropertiesLoadTest {

    @Test
    public void testLoadUsingClasspath() {
        RMT2File.loadSystemProperties("TestSystemParms");
        String parm1 = System.getProperty("SAXDriver");
        String parm2 = System.getProperty("apptitle");
        Assert.assertNotNull(parm1);
        Assert.assertNotNull(parm2);
    }

    @Test
    public void testLoadUsingFileSystem() {
        RMT2File.loadSystemProperties(TestCaseConstants.DATA_DIR
                + "persist.properties");
        String parm1 = System.getProperty("repository");
        String parm2 = System.getProperty("ns1");
        Assert.assertNotNull(parm1);
        Assert.assertNotNull(parm2);
    }

    @Test
    public void testLoadUsingClasspathError() {
        try {
            RMT2File.loadSystemProperties("/TestSystemParms");
            Assert.fail("Test failed for testLoadUsingClasspathError...an exception should have been thrown!");
        } catch (Exception e) {
            // ..properties file should not have been found.
        }
    }
}
