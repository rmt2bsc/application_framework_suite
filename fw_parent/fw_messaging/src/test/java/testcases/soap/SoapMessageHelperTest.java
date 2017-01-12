package testcases.soap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.engine.RMT2SoapEngine;
import com.api.web.Request;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.util.RMT2File;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpVariableScopeFactory.class, SoapMessageHelper.class,
        RMT2SoapEngine.class })
/**
 * Test the SoapMessageHelper class
 * 
 * @author appdev
 *
 */
public class SoapMessageHelperTest {

    @Before
    public void setUp() throws Exception {
        // PowerMockito.mockStatic(HttpVariableScopeFactory.class);
        // String serviceId = "test service id";
        // HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        // HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        // Request mockGenericRequest = mock(Request.class);
        // SOAPMessage mockSoapMsg = mock(SOAPMessage.class);
        //
        // when(HttpVariableScopeFactory.createHttpRequest(mockRequest))
        // .thenReturn(mockGenericRequest);
        //
        // when(mockRequest.getRequestURL()).thenReturn(
        // new StringBuffer("http://test/url"));
        //
        // try {
        // MessageRouterHelper mockMsgRouterHelper =
        // mock(MessageRouterHelper.class);
        // whenNew(MessageRouterHelper.class).withNoArguments().thenReturn(
        // mockMsgRouterHelper);
        // when(mockMsgRouterHelper.routeSoapMessage(serviceId, mockSoapMsg))
        // .thenReturn(mockSoapMsg);
        // } catch (Exception e) {
        // e.printStackTrace();
        // Assert.fail("Unable to mock the construction of MessageRouterHelper class");
        // }
        //
        // try {
        // RMT2SoapEngine engine = new RMT2SoapEngine();
        // engine.processRequest(mockRequest, mockResponse);
        // } catch (StatelessControllerProcessingException e) {
        // e.printStackTrace();
        // Assert.fail("RMT2SoapEngine unit test operation failed");
        // }
    }

    @After
    public void tearDown() throws Exception {
    }

    private String buildSoapRequest() {
        StringBuilder soapMsg = new StringBuilder();
        soapMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        soapMsg.append("<soapenv:Envelope ");
        soapMsg.append("xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapMsg.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        soapMsg.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
        // soapMsg.append("<soapenv:Header ");
        // soapMsg.append("  <header1>abc</header1> ");
        // soapMsg.append("  <header2>def</header2> ");
        // soapMsg.append("  <header3>ghi</header3> ");
        // soapMsg.append("</soapenv:Header> ");
        soapMsg.append("<soapenv:Body>");
        soapMsg.append("<payload soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        soapMsg.append("  <username xsi:type=\"xsd:string\">rterrell</username>");
        soapMsg.append("  <password xsi:type=\"xsd:string\">test1234</password>");
        soapMsg.append("</payload>");
        soapMsg.append("</soapenv:Body>");
        soapMsg.append("</soapenv:Envelope>");
        return soapMsg.toString();
    }

    private Enumeration<Object> buildRequestHeaders() {
        Set<Object> headerNames = new HashSet<Object>();
        headerNames.add("header1");
        headerNames.add("header2");
        headerNames.add("header3");
        return Collections.enumeration(headerNames);
    }

    private void assertRequest(SOAPMessage obj) {
        Assert.assertNotNull(obj);
        try {
            SOAPBody body = obj.getSOAPBody();
            NodeList nodeList = body.getElementsByTagName("payload");
            Assert.assertNotNull(nodeList);
            Assert.assertEquals(1, nodeList.getLength());
            Node node = nodeList.item(0);
            Assert.assertNotNull(node);
            String nodeName = node.getNodeName();
            Assert.assertEquals("payload", nodeName);

            nodeList = body.getElementsByTagName("username");
            Assert.assertNotNull(nodeList);
            Assert.assertEquals(1, nodeList.getLength());
            node = nodeList.item(0);
            Assert.assertNotNull(node);
            nodeName = node.getNodeName();
            Assert.assertEquals("username", nodeName);
            String val = node.getTextContent();
            Assert.assertNotNull(val);
            Assert.assertEquals("rterrell", val);

            nodeList = body.getElementsByTagName("password");
            Assert.assertNotNull(nodeList);
            Assert.assertEquals(1, nodeList.getLength());
            node = nodeList.item(0);
            Assert.assertNotNull(node);
            nodeName = node.getNodeName();
            Assert.assertEquals("password", nodeName);
            val = node.getTextContent();
            Assert.assertNotNull(val);
            Assert.assertEquals("test1234", val);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test toInstance method call.
     */
    @Test
    public void testBuildSoapMessageInstanceFromXmlString() {
        String soapRequest = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.toInstance(soapRequest);
        this.assertRequest(obj);
    }

    /**
     * Test toString(SOAPMessage) call.
     */
    @Test
    public void testConvertSoapMessageToXmlString() {
        String soapRequest = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.toInstance(soapRequest);
        Assert.assertNotNull(obj);

        try {
            String xml = h.toString(obj);
            Assert.assertNotNull(xml);
            Assert.assertTrue(xml.length() > 20);
        } catch (Exception e) {
            Assert.fail("SOAP object to String failed..." + e.getMessage());
        }
    }

    /**
     * Test toBytes(SOAPMessage) call.
     */
    @Test
    public void testConvertSoapMessageToByteArray() {
        String soapRequest = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.toInstance(soapRequest);
        Assert.assertNotNull(obj);

        try {
            byte[] bytes = h.toBytes(obj);
            Assert.assertNotNull(bytes);
            Assert.assertTrue(bytes.length > 20);
        } catch (Exception e) {
            Assert.fail("SOAP object to byte array failed..." + e.getMessage());
        }
    }

    /**
     * Test getSoapInstance using XML String as SOAP message
     */
    @Test
    public void testGetSoapInstanceUsingXmlString() {
        String soapXml = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        this.assertRequest(obj);
    }

    /**
     * Test getSoapInstance using Request object
     */
    @Test
    public void testGetSoapInstanceUsingRequestObject() {
        Request mockRequest = Mockito.mock(Request.class);
        InputStream mockRequestInputStream = RMT2File.createInputStream(this
                .buildSoapRequest());
        Mockito.when(mockRequest.getHeaderNames()).thenReturn(
                this.buildRequestHeaders());
        try {
            Mockito.when(mockRequest.getInputStream()).thenReturn(
                    mockRequestInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Unable to mock rquest's InputStream responsible for containing the SOAP message object");
        }
        Mockito.when(mockRequest.getHeader("header1")).thenReturn("abc");
        Mockito.when(mockRequest.getHeader("header2")).thenReturn("def");
        Mockito.when(mockRequest.getHeader("header3")).thenReturn("ghi");
        Mockito.when(mockRequest.getContentType())
                .thenReturn("application/xml");

        SoapMessageHelper helper = new SoapMessageHelper();
        SOAPMessage results = helper.getSoapInstance(mockRequest);
        this.assertRequest(results);
    }

    /**
     * Test getSoapInstance using XML String as SOAP message and 2 attachments
     */
    @Test
    public void testGetSoapInstanceUsingXMLStringAndAttachments() {
        String soapXml = this.buildSoapRequest();
        InputStream attachment1 = ClassLoader
                .getSystemResourceAsStream("data/receipt.jpg");
        InputStream attachment2 = ClassLoader
                .getSystemResourceAsStream("data/word_test.doc");
        List<Object> attachments = new ArrayList<Object>();
        attachments.add(attachment1);
        attachments.add(attachment2);
        SoapMessageHelper helper = new SoapMessageHelper();
        SOAPMessage results = helper.getSoapInstance(soapXml, attachments);
        this.assertRequest(results);
        Iterator<Object> iter = results.getAttachments();
        Assert.assertNotNull(iter);
        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        Assert.assertEquals(2, count);
    }
}
