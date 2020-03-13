package testcases.io.file;

import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.api.util.RMT2File;

import testcases.TestCaseConstants;

/**
 * Test loading file system resources
 * 
 * @author Roy Terrell
 * 
 */
public class OpenResourceBundleTest {

    private String resource;

    private String resourcePath;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.resource = TestCaseConstants.DATA_DIR + "persist.properties";
        this.resourcePath = "testcases.data.persist";
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadBundle() {
        ResourceBundle rb = RMT2File.loadAppConfigProperties("TestSystemParms");
        Assert.assertNotNull(rb);
        String driver = rb.getString("dbdriver");
        Assert.assertNotNull(driver);
    }

    @Test
    public void testFindResourceUnqualified() {
        Object rc1, rc2;
        rc1 = RMT2File.getFileInputStream(this.resource);
        Assert.assertNotNull(rc1);

        // This one works with framework/webapp/resources on the classpath
        rc2 = RMT2File.getFileInputStream("/email/" + this.resource);
        Assert.assertNull(rc2);

        rc1 = RMT2File.getFileInputStream(this.resourcePath);
        Assert.assertNull(rc1);
        rc2 = RMT2File.getFileInputStream("/" + this.resourcePath);
        Assert.assertNull(rc2);
        return;
    }
}
