package testcases.io.file;

import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.api.util.RMT2File;

import testcases.TestCaseConstants;

/**
 * Test various ways of obtaining InputStreams from RMT2File class.
 * 
 * @author Roy Terrell
 * 
 */
public class FileResourceLoadTest {

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
    public void checkForResourceFromAllContexts() {
        String data = null;
        InputStream is = RMT2File.getFileInputStream(TestCaseConstants.DATA_DIR
                + "CreditorView.xml");
        Assert.assertNotNull(is);
        data = RMT2File.getStreamStringData(is);
        Assert.assertNotNull(data);
    }

    @Test
    public void checkForResourceFromInnerContexts() {
        String data = null;
        InputStream is = RMT2File
                .getFileInputStreamFromAppContext(TestCaseConstants.DATA_DIR
                        + "CreditorView.xml");
        Assert.assertNotNull(is);
        data = RMT2File.getStreamStringData(is);
        Assert.assertNotNull(data);
    }

    @Test
    public void checkForResourceFromOuterContexts() {
        String data = null;
        InputStream is = RMT2File
                .getFileInputStreamFromContextClassloader(TestCaseConstants.DATA_DIR
                        + "timesheet_20091220.pdf");
        Assert.assertNotNull(is);
        data = RMT2File.getStreamStringData(is);
        Assert.assertNotNull(data);
    }
}
