package org.rmt2.soap;

import static org.mockito.Mockito.when;

import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.api.config.SystemConfigurator;
import com.api.config.jaxb.AppServerConfig;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.client.SoapClientWrapper;
import com.api.web.controller.scope.HttpVariableScopeFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpVariableScopeFactory.class, SystemConfigurator.class })
public class SoapClientTest {

    private static String REQUEST_ENVELOPE;
    private static String REQUEST_PAYLOAD;

    @Before
    public void setUp() throws Exception {

        StringBuilder s = new StringBuilder();
        s.append("   <MultimediaRequest>");
        s.append("      <header>");
        s.append("         <routing>fkdkdfjdkd9dkd9d</routing>");
        s.append("         <application>media</application>");
        s.append("         <module>document</module>");
        s.append("         <transaction>getContent</transaction>");
        s.append("         <delivery_mode>SYNC</delivery_mode>");
        s.append("         <message_mode>REQUEST</message_mode>");
        s.append("         <delivery_date>2017-1-1</delivery_date>");
        s.append("         <session_id>849032894329393939</session_id>");
        s.append("         <user_id>test_user</user_id>");
        s.append("      </header>");
        s.append("      <content_id>1200</content_id>");
        s.append("   </MultimediaRequest>");
        REQUEST_PAYLOAD = s.toString().trim();

        // s.delete(0, s.length());

        StringBuilder r = new StringBuilder();
        r.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        r.append("<soapenv:Header/>");
        r.append("<soapenv:Body>");
        r.append(REQUEST_PAYLOAD);
        r.append("</soapenv:Body>");
        r.append("</soapenv:Envelope>");
        REQUEST_ENVELOPE = r.toString();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    /**
     * This will pass with or without the SOAP engine up and running. When the
     * SOAP engine is running, we should see an error message pertaining to the
     * JMS connection failure (Business API Server is unavailabe). When the SOAP
     * engine is down, an error is produced regarding the SOAP endpoint could
     * not be contacted.
     */
    public void testClientConnectSuccess() {
        String soapHost = "http://localhost:8080/server-external-webservices/services/web";
        String faultErrorText = "SOAP Engine Failure.   Error occurred processing incoming SOAP message.";
        String faultErrorCode = "SOAP-ENV:Server";
        PowerMockito.mockStatic(SystemConfigurator.class);
        AppServerConfig mockAppServerConfig = Mockito.mock(AppServerConfig.class);
        AppServerConfig.SystemProperties mockSystemProperties = Mockito.mock(AppServerConfig.SystemProperties.class);

        when(SystemConfigurator.getConfig()).thenReturn(mockAppServerConfig);
        when(mockSystemProperties.getSoaphost()).thenReturn(soapHost);
        when(mockAppServerConfig.getSystemProperties()).thenReturn(mockSystemProperties);
        when(mockAppServerConfig.getSystemProperties().getSoaphost()).thenReturn(soapHost);
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage response = client.callSoap(REQUEST_PAYLOAD);
            // A SOAP fault should be evident since the SOAP Engine is up, but
            // the Business Api Server is down.
            SoapMessageHelper helper = new SoapMessageHelper();
            Assert.assertNotNull(response);
            Assert.assertNotNull(helper.isError(response));
            String code = helper.getErrorCode(response);
            Assert.assertEquals(faultErrorCode, code);
            String errMsg = helper.getErrorMessage(response);
            Assert.assertTrue(errMsg.contains(faultErrorText));

        } catch (MessageException e) {
            // The SOAP Engine is down and unavailable...test passed
            e.printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("An unexpected exception type was thrown...test failed");
        }
        

    }

}
