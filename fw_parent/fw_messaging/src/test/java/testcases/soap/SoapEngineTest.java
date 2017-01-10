package testcases.soap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.api.messaging.webservice.router.MessageRouterHelper;
import com.api.messaging.webservice.soap.SoapConstants;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.client.SoapProductBuilder;
import com.api.messaging.webservice.soap.engine.RMT2SoapEngine;
import com.api.web.Request;
import com.api.web.controller.StatelessControllerProcessingException;
import com.api.web.controller.scope.HttpVariableScopeFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpVariableScopeFactory.class, SoapMessageHelper.class,
        RMT2SoapEngine.class })
public class SoapEngineTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHappyPath() {
        PowerMockito.mockStatic(HttpVariableScopeFactory.class);
        String serviceId = "test service id";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        Request mockGenericRequest = mock(Request.class);
        SOAPMessage mockSoapMsg = mock(SOAPMessage.class);

        when(HttpVariableScopeFactory.createHttpRequest(mockRequest))
                .thenReturn(mockGenericRequest);

        try {
            SoapMessageHelper mockSoapHelper = mock(SoapMessageHelper.class);
            whenNew(SoapMessageHelper.class).withNoArguments().thenReturn(
                    mockSoapHelper);
            when(mockSoapHelper.getSoapInstance(mockGenericRequest))
                    .thenReturn(mockSoapMsg);
            when(
                    mockSoapHelper.getHeaderValue(SoapProductBuilder.HEADER_NS
                            + ":" + SoapConstants.SERVICEID_NAME, mockSoapMsg))
                    .thenReturn(serviceId);

        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail("Unable to mock the construction of SoapMessageHelper class");
        }

        when(mockRequest.getRequestURL()).thenReturn(
                new StringBuffer("http://test/url"));

        try {
            MessageRouterHelper mockMsgRouterHelper = mock(MessageRouterHelper.class);
            whenNew(MessageRouterHelper.class).withNoArguments().thenReturn(
                    mockMsgRouterHelper);
            when(mockMsgRouterHelper.routeSoapMessage(serviceId, mockSoapMsg))
                    .thenReturn(mockSoapMsg);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to mock the construction of MessageRouterHelper class");
        }

        try {
            RMT2SoapEngine engine = new RMT2SoapEngine();
            engine.processRequest(mockRequest, mockResponse);
        } catch (StatelessControllerProcessingException e) {
            e.printStackTrace();
            Assert.fail("RMT2SoapEngine unit test operation failed");
        }
    }
}
