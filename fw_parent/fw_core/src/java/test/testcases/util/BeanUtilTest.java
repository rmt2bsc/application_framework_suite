package testcases.util;

import java.util.Date;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import testcases.bean.Xact;

import com.util.RMT2BeanUtility;

/**
 * Test dynamic bean creattion
 * 
 * @author Roy Terrell
 * 
 */
public class BeanUtilTest {

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
    public void testBeanToProperties() {
        Xact x = new Xact();
        x.setXactId(100);
        x.setXactAmount(1234.99);
        x.setXactDate(new Date());
        x.setConfirmNo("94329-43289438");
        x.setTenderId(3);
        x.setReason("This is a test");
        x.setUserId("rterrell");

        RMT2BeanUtility util = new RMT2BeanUtility(x);
        Properties props = util.toProperties();
        Assert.assertNotNull(props.getProperty("XactDate"));
        return;
    }
}
