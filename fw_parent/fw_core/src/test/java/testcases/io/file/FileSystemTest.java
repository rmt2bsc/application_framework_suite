package testcases.io.file;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;

import testcases.TestCaseConstants;

import com.NotFoundException;
import com.SystemException;
import com.util.RMT2File;
import com.util.RMT2FileDirectoryFilter;

/**
 * Test file manipulation and MIME type identification
 * 
 * @author Roy Terrell
 *
 */
public class FileSystemTest {

    @Test
    public void testFile() {
        File file = new File(TestCaseConstants.DATA_DIR);
        Assert.assertEquals(RMT2File.FILE_IO_NOT_FILE,
                RMT2File.verifyFile(file));

        file = new File(TestCaseConstants.DATA_DIR + "FileNotExist.mp3");
        Assert.assertEquals(RMT2File.FILE_IO_NOTEXIST,
                RMT2File.verifyFile(file));

        file = new File(TestCaseConstants.DATA_DIR + "VikingHorn_vbr.mp3");
        Assert.assertEquals(RMT2File.FILE_IO_EXIST, RMT2File.verifyFile(file));
    }

    @Test
    public void testDir() {
        File dir = new File(TestCaseConstants.DATA_DIR);
        Assert.assertEquals(RMT2File.FILE_IO_EXIST,
                RMT2File.verifyDirectory(dir));

        dir = new File(TestCaseConstants.DATA_DIR + "NonExistingDirectory");
        Assert.assertEquals(RMT2File.FILE_IO_NOTEXIST,
                RMT2File.verifyDirectory(dir));

        dir = new File(TestCaseConstants.DATA_DIR + "VikingHorn_vbr.mp3");
        Assert.assertEquals(RMT2File.FILE_IO_NOT_DIR,
                RMT2File.verifyDirectory(dir));

        boolean result = RMT2File
                .isNetworkShareAccessible(TestCaseConstants.SERVER_NAME
                        + "/download");
        Assert.assertTrue(result);

        try {
            result = true;
            result = RMT2File.isNetworkShareAccessible(null);
        } catch (Exception e) {
            result = false;
        }
        Assert.assertFalse(result);

        return;
    }

    @Test
    public void testListFilesUsingEmptyWildcard() {
        File folder = new File(TestCaseConstants.DATA_DIR);
        RMT2FileDirectoryFilter filter = new RMT2FileDirectoryFilter();
        File[] listOfFiles = folder.listFiles(filter);
        Assert.assertNotNull(listOfFiles);
        Assert.assertTrue(listOfFiles.length >= 18);
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Assert.assertNotNull(listOfFiles[i].getName());
            }
            else if (listOfFiles[i].isDirectory()) {
                Assert.assertNotNull(listOfFiles[i].getName());
            }
        }
    }

    @Test
    public void testFileListUtilityWithWildcards() {
        List<String> listing = RMT2File.getDirectoryListing(
                TestCaseConstants.DATA_DIR, "acct_??_*.*;*.xml");
        Assert.assertTrue(listing.size() > 8);
    }

    @Test
    public void testFileListUtility() {
        List<String> listing = RMT2File.getDirectoryListing(
                TestCaseConstants.DATA_DIR, null);
        Assert.assertTrue(listing.size() > 3);
    }

    @Test
    public void testFileListUtilityDirectoryNotFound() {
        try {
            RMT2File.getDirectoryListing(TestCaseConstants.DATA_DIR
                    + "NonExistingDirectory", null);
        } catch (NotFoundException e) {
            Assert.assertEquals(RMT2File.FILE_IO_NOTEXIST, e.getErrorCode());
            return;
        }
        Assert.fail("Test failed due to an exception should have occurred regarding directory not found");
    }

    @Test
    public void testFileListUtilityDirectoryAsFile() {
        try {
            RMT2File.getDirectoryListing(TestCaseConstants.DATA_DIR
                    + "NewMFXGirls.txt", null);
        } catch (SystemException e) {
            Assert.assertEquals(RMT2File.FILE_IO_NOT_DIR, e.getErrorCode());
            return;
        }
        Assert.fail("Test failed due to an exception should have occurred regarding a file was loaded instead of a directory");
    }

    @Test
    public void testPropertisFileLoadByClassPath() {
        Properties props;
        String result;
        try {
            props = RMT2File.loadPropertiesObject(TestCaseConstants.DATA_DIR
                    + "persist.properties");
            result = props.getProperty("persist");
            Assert.assertEquals("FILE", result);
        } catch (Exception e) {
            // Error
        }

        ResourceBundle rb;
        rb = RMT2File.loadAppConfigProperties("data.persist");
        result = rb.getString("persist");
        Assert.assertEquals("FILE", result);
    }

    @Test
    public void testMimeTypeIdentification() {
        String mimeType = RMT2File.getMimeType(TestCaseConstants.DATA_DIR
                + "acct_cd_11783.jpg");
        Assert.assertEquals("image/jpeg", mimeType);
        mimeType = RMT2File.getMimeType(TestCaseConstants.DATA_DIR
                + "Camera.wav");
        Assert.assertEquals("audio/x-wav", mimeType);
        mimeType = RMT2File.getMimeType(TestCaseConstants.DATA_DIR
                + "timesheet_20091220.pdf");
        Assert.assertEquals("application/pdf", mimeType);
        mimeType = RMT2File.getMimeType(TestCaseConstants.DATA_DIR
                + "VikingHorn_vbr.mp3");
        Assert.assertEquals("audio/mpeg", mimeType);
        mimeType = RMT2File.getMimeType(TestCaseConstants.DATA_DIR
                + "word_test.doc");
        Assert.assertEquals("application/msword", mimeType);
        mimeType = RMT2File
                .getMimeType(TestCaseConstants.DATA_DIR + "test.xml");
        Assert.assertEquals("application/xml", mimeType);
    }

    @Test
    public void testLocateFilenameExtension() {
        String ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR
                + "acct_cd_11783.jpg");
        Assert.assertEquals("jpg", ext);
        ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR + "Camera.wav");
        Assert.assertEquals("wav", ext);
        ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR
                + "timesheet_20091220.pdf");
        Assert.assertEquals("pdf", ext);
        ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR
                + "VikingHorn_vbr.mp3");
        Assert.assertEquals("mp3", ext);
        ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR + "word_test.doc");
        Assert.assertEquals("doc", ext);
        ext = RMT2File.getFileExt(TestCaseConstants.DATA_DIR + "test.xml");
        Assert.assertEquals("xml", ext);
    }

}
