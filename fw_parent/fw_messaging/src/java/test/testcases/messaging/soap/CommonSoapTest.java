package testcases.messaging.soap;

import org.junit.After;
import org.junit.Before;

import com.api.config.old.ProviderConfig;
import com.api.messaging.webservice.soap.client.SoapClientFactory;

/**
 * @author appdev
 * 
 */
public class CommonSoapTest {
    private static final String SOAP_HOST = "http://localhost:8080/ServiceRouter/soapEngine";

    private ProviderConfig config;

    /**
     * Get SOAP Provider configuration information from stand alone context
     * since configuration data is loaded at server start up and this test does
     * not execute within the same JVM as the Web Server
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.config = SoapClientFactory.getSoapConfigInstance();
        if (this.config.getHost() == null) {
            this.config.setHost(CommonSoapTest.SOAP_HOST);
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * @return the config
     */
    public ProviderConfig getConfig() {
        return config;
    }

}
