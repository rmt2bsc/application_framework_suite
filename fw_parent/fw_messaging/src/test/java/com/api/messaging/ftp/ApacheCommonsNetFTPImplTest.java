package com.api.messaging.ftp;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.api.util.RMT2Money;

/**
 * Test the audio/video meta data query functionality in the Media API.
 * 
 * @author rterrell
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ApacheCommonsNetFTPImpl.class, FtpFactory.class, RMT2Money.class })
public class ApacheCommonsNetFTPImplTest {
    private static final String TEST_USER_SESSION_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String TEST_HOST = "192.168.0.5";
    private static final String TEST_PORT = "21";
    private static final String TEST_USER = "royterrell";
    private static final String TEST_PASSWORD = "hoover";
    private static final String TEST_DIR_NONRECURSIVE = "multimedia/audio/non_ripped/813/Recolor";
    private static final String TEST_DIR_RECURSIVE = "multimedia/audio/non_ripped/Afterlife";
    private static final String TEST_DIR_RECURSIVE_ALL = "multimedia/audio";
    private static final String TEST_SINGLE_FILE = TEST_DIR_NONRECURSIVE + "/813-Recolor-06-256 Colors.mp3";
    private static final String TEST_DOWNLOADED_FILE_PATH = "/temp/" + TEST_USER_SESSION_ID + "/"
            + "813-Recolor-06-256 Colors.mp3";
    private static final int TEST_NORECURSION_FILE_COUNT = 8;
    private static final int TEST_RECURSION_FILE_COUNT = 45;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("SerialPath", "/temp/");
        return;
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        System.setProperty("SerialPath", "");
        return;
    }

    @Test
    public void testSuccess_Connection() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);
    }

    @Test
    public void testSuccess_Connection_With_Default_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, null, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Host() {
        FtpApi api = FtpFactory.getInstance("rmtdaldev77", TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "100", TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_NonNumeric_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "ABC", TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Null_User() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, null, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_User() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, "testuser", TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Null_Password() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, null, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Password() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, "test123", TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testSuccess_Directory_Listing_NonRecursive() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        List<String> files = api.listDirectory(TEST_DIR_NONRECURSIVE, false);
        Assert.assertNotNull(files);
        Assert.assertEquals(TEST_NORECURSION_FILE_COUNT, files.size());
    }

    @Test
    public void testSuccess_Directory_Listing_Recursive() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        List<String> files = api.listDirectory(TEST_DIR_RECURSIVE, true);
        Assert.assertNotNull(files);
        Assert.assertEquals(TEST_RECURSION_FILE_COUNT, files.size());
    }

    @Test
    public void testSuccess_Directory_Listing_Recursive_All() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        List<String> files = api.listDirectory(TEST_DIR_RECURSIVE_ALL, true);
        Assert.assertNotNull(files);
        Assert.assertTrue(files.size() > 30000);
    }

    @Test
    public void testSuccess_Fetch_Single_File() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        String rc = api.downloadFile(TEST_SINGLE_FILE);
        Assert.assertEquals(TEST_DOWNLOADED_FILE_PATH, rc);
    }
}