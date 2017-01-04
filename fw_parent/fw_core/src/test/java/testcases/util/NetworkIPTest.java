package testcases.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.util.RMT2Utility;

/**
 * Test IP Address manipulation
 * 
 * @author rterrell
 * 
 */
public class NetworkIPTest {

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
    public void convertIpDecimalToDotted() {
        long addr = 3576916119L;

        String result = RMT2Utility.convertIp(addr);
        Assert.assertNotNull(result);
        String octets[] = result.split("\\.");
        Assert.assertNotNull(octets);
        Assert.assertEquals(4, octets.length);
    }
}
