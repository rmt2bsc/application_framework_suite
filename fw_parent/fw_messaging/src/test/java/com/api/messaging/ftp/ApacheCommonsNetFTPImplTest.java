package com.api.messaging.ftp;

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
    private static final String TEST_HOST = "192.168.0.5";
    private static final String TEST_PORT = "21";
    private static final String TEST_USER = "royterrell";
    private static final String TEST_PASSWORD = "hoover";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        return;
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        return;
    }

    @Test
    public void testSuccess_Connection() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD);
        Assert.assertNotNull(api);
    }

    @Test
    public void testSuccess_Connection_With_Default_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, null, TEST_USER, TEST_PASSWORD);
        Assert.assertNotNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Host() {
        FtpApi api = FtpFactory.getInstance("rmtdaldev77", TEST_PORT, TEST_USER, TEST_PASSWORD);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "100", TEST_USER, TEST_PASSWORD);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_NonNumeric_Port() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, "ABC", TEST_USER, TEST_PASSWORD);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Null_User() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, null, TEST_PASSWORD);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_User() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, "testuser", TEST_PASSWORD);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Null_Password() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, null);
        Assert.assertNull(api);
    }

    @Test
    public void testError_Connection_With_Invalid_Password() {
        FtpApi api = FtpFactory.getInstance(TEST_HOST, TEST_PORT, TEST_USER, "test123");
        Assert.assertNull(api);
    }
}