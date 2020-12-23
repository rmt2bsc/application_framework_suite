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
        FtpApi api = FtpFactory.getInstance("192.168.0.5", "21", "royterrell", "hoover");
        Assert.assertNotNull(api);
    }


}