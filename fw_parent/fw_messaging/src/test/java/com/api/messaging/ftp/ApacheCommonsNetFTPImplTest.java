package com.api.messaging.ftp;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.api.util.RMT2File;
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
    private static final String TEST_DIR_NONRECURSIVE = "multimedia" + File.separator + "audio" + File.separator + "non_ripped" + File.separator + "813" + File.separator + "Recolor";
    private static final String TEST_DIR_RECURSIVE = "multimedia/audio/non_ripped/Afterlife";
    private static final String TEST_DIR_RECURSIVE_ALL = "multimedia/audio";
    private static final String TEST_SINGLE_FILE = TEST_DIR_NONRECURSIVE + File.separator + "813-Recolor-06-256 Colors.mp3";
    private static final String TEST_DOWNLOADED_FILE_PATH = RMT2File.createUserWorkArea() + File.separator + TEST_USER_SESSION_ID + File.separator
            + "word_test.doc";
    private static final int TEST_FILE_COUNT = 8;
    private static final String TEST_DOWNLOAD_FILE = "data" + File.separator + "word_test.doc";


    private FTPClient mockFtp;
    private FTPFile[] mockDataFTPFiles;
    private String[] mockDataFileNames;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("SerialPath", "temp");
        
        this.mockFtp = Mockito.mock(FTPClient.class);
        PowerMockito.whenNew(FTPClient.class).withNoArguments().thenReturn(mockFtp);

        doNothing().when(this.mockFtp).connect(isA(String.class),
                isA(Integer.class));
        when(this.mockFtp.getReplyCode()).thenReturn(220);
        when(this.mockFtp.login(isA(String.class),
                isA(String.class))).thenReturn(true);
        
        // Setup mock data
        this.mockDataFTPFiles = new FTPFile[TEST_FILE_COUNT];
        this.mockDataFTPFiles[0] = this.createFTPFileObject("File1.txt", true);
        this.mockDataFTPFiles[1] = this.createFTPFileObject("File2.txt", true);
        this.mockDataFTPFiles[2] = this.createFTPFileObject("File3.txt", true);
        this.mockDataFTPFiles[3] = this.createFTPFileObject("File4.txt", true);
        this.mockDataFTPFiles[4] = this.createFTPFileObject("File5.txt", true);
        this.mockDataFTPFiles[5] = this.createFTPFileObject("File6.txt", true);
        this.mockDataFTPFiles[6] = this.createFTPFileObject("File7.txt", true);
        this.mockDataFTPFiles[7] = this.createFTPFileObject("File8.txt", true);

        this.mockDataFileNames = new String[TEST_FILE_COUNT];
        this.mockDataFileNames[0] = this.mockDataFTPFiles[0].getName();
        this.mockDataFileNames[1] = this.mockDataFTPFiles[1].getName();
        this.mockDataFileNames[2] = this.mockDataFTPFiles[2].getName();
        this.mockDataFileNames[3] = this.mockDataFTPFiles[3].getName();
        this.mockDataFileNames[4] = this.mockDataFTPFiles[4].getName();
        this.mockDataFileNames[5] = this.mockDataFTPFiles[5].getName();
        this.mockDataFileNames[6] = this.mockDataFTPFiles[6].getName();
        this.mockDataFileNames[7] = this.mockDataFTPFiles[7].getName();

        return;
    }
    
    private FTPFile createFTPFileObject(String fileName, boolean isFile) {
        FTPFile file = new FTPFile();
        file.setName(fileName);
        if (isFile) {
            file.setType(FTPFile.FILE_TYPE);
        }
        else {
            file.setType(FTPFile.DIRECTORY_TYPE);
        }
        return file;
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
        try {
            doThrow(new IOException("Expected Test Error: invalid host")).when(this.mockFtp).connect(isA(String.class),
                    isA(Integer.class));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpApi api = FtpFactory.getInstance("rmtdaldev77", TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Port() {
        try {
            doThrow(new IOException("Expected Test Error: invalid port")).when(this.mockFtp).connect(isA(String.class),
                    isA(Integer.class));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "100", TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_NonNumeric_Port() {
        try {
            doThrow(new IOException("Expected Test Error: invalid port")).when(this.mockFtp).connect(isA(String.class),
                    isA(Integer.class));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "ABC", TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testValidation_Connection_With_Null_User() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, null, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_User() {
        try {
            when(this.mockFtp.login(isA(String.class), isA(String.class))).thenReturn(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, "testuser", TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testValidation_Connection_With_Null_Password() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, null, TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Password() {
        try {
            when(this.mockFtp.login(isA(String.class), isA(String.class))).thenReturn(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, "test123", TEST_USER_SESSION_ID);
        Assert.assertNull(api);
    }

    @Test
    public void testSuccess_Directory_Listing_NonRecursive() {
        try {
            when(this.mockFtp.listNames(isA(String.class))).thenReturn(this.mockDataFileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        List<String> files = api.listDirectory(TEST_DIR_NONRECURSIVE, false);
        Assert.assertNotNull(files);
        Assert.assertEquals(TEST_FILE_COUNT, files.size());
        for (int ndx = 0; ndx < files.size(); ndx++) {
            Assert.assertEquals("File" + (ndx + 1) + ".txt", files.get(ndx));
        }
    }

    @Test
    public void testSuccess_Directory_Listing_Recursive() {
        try {
            when(this.mockFtp.listFiles(isA(String.class))).thenReturn(this.mockDataFTPFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        List<String> files = api.listDirectory(TEST_DIR_RECURSIVE, true);
        Assert.assertNotNull(files);
        Assert.assertEquals(TEST_FILE_COUNT, files.size());
        for (int ndx = 0; ndx < files.size(); ndx++) {
            Assert.assertEquals(TEST_DIR_RECURSIVE + "/File" + (ndx + 1) + ".txt", files.get(ndx));
        }
    }

    @Test
    public void testSuccess_Fetch_Single_File() {
        try {
            when(this.mockFtp.retrieveFile(isA(String.class), isA(OutputStream.class))).thenReturn(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, TEST_USER_SESSION_ID);
        Assert.assertNotNull(api);

        String rc = api.downloadFile(TEST_DOWNLOAD_FILE);
        Assert.assertEquals(TEST_DOWNLOADED_FILE_PATH, rc);
    }
}