package testcases;

import org.junit.After;
import org.junit.Before;

import com.api.config.ConfigConstants;

/**
 * Common test class for all API modules.
 * 
 * @author Roy Terrell
 * 
 */
public class StandAloneApiTest {
    protected static final String TEST_SOAP_HOST = "http://localhost:8080/ServiceDispatch/soapEngine";

    protected static final String LOCAL_PARAM_FILE = "config.TestAppParms";

    protected String prevSoaphost;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("env", "TEST");
        StandAloneApiConfigTestDelegate cfg = new StandAloneApiConfigTestDelegate(
                StandAloneApiTest.LOCAL_PARAM_FILE);
        cfg.doConfig();
        this.prevSoaphost = System.setProperty(ConfigConstants.SOAP_HOST,
                StandAloneApiTest.TEST_SOAP_HOST);
        return;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (this.prevSoaphost != null) {
            this.prevSoaphost = System.setProperty(ConfigConstants.SOAP_HOST,
                    this.prevSoaphost);
        }
    }

    protected void createData() {
        return;
    }

    protected void deleteData() {
        return;
    }
}
