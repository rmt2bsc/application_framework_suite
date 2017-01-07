package testcases.io.file;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.util.RMT2File;
import com.util.RMT2String;

import testcases.TestCaseConstants;

/**
 * Test directory manipulation
 * 
 * @author Roy Terrell
 * 
 */
public class DirecotryCreationTest {
    private String existingDir;
    private String newSingleDir;
    private String newMultiDir;
    private String multiDirDelete;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        newSingleDir = TestCaseConstants.DATA_DIR + "/tmp2/";
        existingDir = TestCaseConstants.DATA_DIR + "/tmp2";
        newMultiDir = TestCaseConstants.DATA_DIR
                + "/tmp2/new_multi_dir/sub_dir_1/sub_dir_2/sub_dir_3/";
        multiDirDelete = TestCaseConstants.DATA_DIR + "/tmp2/";
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        File file1 = new File(this.newSingleDir);
        File file2 = new File(this.multiDirDelete);
        file1.delete();
        int rc = RMT2File.deleteFile(file2);
        // Assert.assertTrue(rc > 0);
        System.out
                .println("total number of multiple directories deleted: " + rc);
    }

    @Test
    public void testCreateNewSingleDir() {
        boolean result = RMT2File.createDirectory(this.newSingleDir);
        Assert.assertTrue(result);
        result = RMT2File.createDirectory(this.existingDir);
        Assert.assertFalse(result);
    }

    // @Test
    // public void testCreateExistingSingleDir() {
    // boolean result = RMT2File.createDirectory(this.existingDir);
    // Assert.assertFalse(result);
    // }

    @Test
    public void testCreateNewDirWithSubDirectories() {
        boolean result = RMT2File.createDirectories(this.newSingleDir);
        Assert.assertTrue(result);
        result = RMT2File.createDirectories(this.newMultiDir);
        Assert.assertTrue(result);
    }

    @Test
    public void testFileParsingWithoutDrive() {
        String filename = TestCaseConstants.DATA_DIR + "CreditorView.xml";
        String result = RMT2File.getFullFilePathWithoutDrive(filename);
        List<String> tokens = RMT2String.getTokens(result, ":");
        Assert.assertNotNull(tokens);
        Assert.assertEquals(1, tokens.size());
    }
}
