package testcases.soap;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
import org.rmt2.jaxb.AddressType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.PersonType;
import org.rmt2.jaxb.ZipcodeType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.engine.RMT2SoapEngine;
import com.api.web.Request;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.api.xml.jaxb.JaxbUtil;
import com.util.RMT2File;

import testcases.TestCaseConstants;

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
        // Assert.fail("Unable to mock the construction of MessageRouterHelper
        // class");
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

    private String buildInvalidSoapRequest() {
        StringBuilder soapMsg = new StringBuilder();
        soapMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        soapMsg.append(
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapMsg.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        soapMsg.append(
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
        soapMsg.append("<soapenv:Body>");
        soapMsg.append(
                "<payload soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        soapMsg.append(
                "  <username xsi:type=\"xsd:string\">rterrell</username>");
        soapMsg.append(
                "  <password xsi:type=\"xsd:string\">test1234</password>");
        soapMsg.append("</payload>");
        soapMsg.append("</soapenv:Body>");
        return soapMsg.toString();
    }

    private String buildSoapRequest() {
        StringBuilder soapMsg = new StringBuilder();
        soapMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        soapMsg.append("<soapenv:Envelope ");
        soapMsg.append(
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapMsg.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        soapMsg.append(
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
        soapMsg.append("<soapenv:Header> ");
        soapMsg.append("  <header1>abc</header1> ");
        soapMsg.append("  <header2>def</header2> ");
        soapMsg.append("  <header3>ghi</header3> ");
        soapMsg.append("</soapenv:Header> ");
        soapMsg.append("<soapenv:Body>");
        soapMsg.append(
                "<payload soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        soapMsg.append(
                "  <username xsi:type=\"xsd:string\">rterrell</username>");
        soapMsg.append(
                "  <password xsi:type=\"xsd:string\">test1234</password>");
        soapMsg.append("</payload>");
        soapMsg.append("</soapenv:Body>");
        soapMsg.append("</soapenv:Envelope>");
        return soapMsg.toString();
    }

    private String buildSoapResponseWithFault() {
        StringBuilder soapMsg = new StringBuilder();
        soapMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        soapMsg.append("<soapenv:Envelope ");
        soapMsg.append(
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapMsg.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        soapMsg.append(
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
        soapMsg.append("<soapenv:Header> ");
        soapMsg.append("  <header1>abc</header1> ");
        soapMsg.append("  <header2>def</header2> ");
        soapMsg.append("  <header3>ghi</header3> ");
        soapMsg.append("</soapenv:Header> ");
        soapMsg.append("<soapenv:Body>");
        soapMsg.append("<soapenv:Fault>");
        soapMsg.append(
                "<faultcode xsi:type=\"xsd:string\">soapenv:Client</faultcode>");
        soapMsg.append("  <faultstring xsi:type=\"xsd:string\">");
        soapMsg.append("This is an example SOAP fault message");
        soapMsg.append("  </faultstring>");
        soapMsg.append("</soapenv:Fault>");
        soapMsg.append("</soapenv:Body>");
        soapMsg.append("</soapenv:Envelope>");
        return soapMsg.toString();

    }

    private String buildJaxbSoapRequest() {
        ObjectFactory f = new ObjectFactory();
        ZipcodeType z = f.createZipcodeType();
        z.setCity("Sheveport");
        z.setState("LA");
        z.setZipcode(BigInteger.valueOf(71106));

        AddressType at = f.createAddressType();
        at.setAddr1("4329 Harbor St");
        at.setAddr2("P.O. Box 1234");
        at.setAddr3("Building 324-a");
        at.setAddr4("Room 432");
        at.setAddrId(BigInteger.valueOf(32));
        at.setPhoneHome("318-321-5432");
        at.setZip(z);

        PersonType pt = f.createPersonType();
        pt.setBirthDate("1966-2-23");
        pt.setFirstName("Roy");
        pt.setLastName("Terrell");
        pt.setSsn("444-55-5432");
        pt.setAddress(at);

        JaxbUtil jaxb = new JaxbUtil("org.rmt2.jaxb");
        String bodyXml = jaxb.marshalMessage(pt);

        StringBuilder soapMsg = new StringBuilder();
        soapMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        soapMsg.append("<soapenv:Envelope ");
        soapMsg.append(
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapMsg.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        soapMsg.append(
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
        soapMsg.append("<soapenv:Header> ");
        soapMsg.append("  <header1>person object</header1> ");
        soapMsg.append("  <header2>address object</header2> ");
        soapMsg.append("  <header3>zip code object</header3> ");
        soapMsg.append("</soapenv:Header> ");
        soapMsg.append("<soapenv:Body>");
        soapMsg.append(bodyXml);
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
     * Test converting SOAP object using invalid SOAP XML
     */
    @Test
    public void testInvalidSoapString() {
        String soapRequest = this.buildInvalidSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.toInstance(soapRequest);
        try {
            obj.getSOAPBody();
            Assert.fail(
                    "Test failed since an exception was not thrown attempting to extract the body from an invalid SOAP message object");
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
        InputStream mockRequestInputStream = RMT2File
                .createInputStream(this.buildSoapRequest());
        Mockito.when(mockRequest.getHeaderNames())
                .thenReturn(this.buildRequestHeaders());
        try {
            Mockito.when(mockRequest.getInputStream())
                    .thenReturn(mockRequestInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(
                    "Unable to mock rquest's InputStream responsible for containing the SOAP message object");
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
        InputStream attachment1 = RMT2File
                .getFileInputStream(TestCaseConstants.DATA_DIR + "receipt.jpg");
        InputStream attachment2 = RMT2File.getFileInputStream(
                TestCaseConstants.DATA_DIR + "word_test.doc");
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

    /**
     * Test getHeaderValue(String)
     */
    @Test
    public void testGetHeaderValue() {
        String soapXml = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        this.assertRequest(obj);

        try {
            String val = h.getHeaderValue("header1", obj);
            Assert.assertNotNull(val);
            Assert.assertEquals("abc", val);
            val = h.getHeaderValue("header2", obj);
            Assert.assertNotNull(val);
            Assert.assertEquals("def", val);
            val = h.getHeaderValue("header3", obj);
            Assert.assertNotNull(val);
            Assert.assertEquals("ghi", val);
        } catch (SOAPException e) {
            e.printStackTrace();
            Assert.fail(
                    "A server error occurred while testing SoapMessageHelper.getHeaderValue() method");
        }
    }

    /**
     * Test getBody(SOAPMessage), convert to JAXB object, and verify data.
     */
    @Test
    public void testGetSoapBody() {
        String soapXml = this.buildJaxbSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        Assert.assertNotNull(obj);
        String jaxbXml = h.getBody(obj);
        Assert.assertNotNull(jaxbXml);

        JaxbUtil jaxb = new JaxbUtil("org.rmt2.jaxb");
        Object jaxbObj = jaxb.unMarshalMessage(jaxbXml);
        Assert.assertNotNull(jaxbObj);
        Assert.assertTrue(jaxbObj instanceof PersonType);
        PersonType person = (PersonType) jaxbObj;
        Assert.assertEquals("Roy", person.getFirstName());
        Assert.assertEquals("Terrell", person.getLastName());
        Assert.assertEquals("1966-2-23", person.getBirthDate());
        Assert.assertEquals("444-55-5432", person.getSsn());

        Assert.assertNotNull(person.getAddress());
        AddressType at = person.getAddress();
        Assert.assertEquals(BigInteger.valueOf(32), at.getAddrId());
        Assert.assertEquals("4329 Harbor St", at.getAddr1());
        Assert.assertEquals("318-321-5432", at.getPhoneHome());

        Assert.assertNotNull(person.getAddress().getZip());
        ZipcodeType zt = person.getAddress().getZip();
        Assert.assertEquals("Sheveport", zt.getCity());
        Assert.assertEquals("LA", zt.getState());
        Assert.assertEquals(BigInteger.valueOf(71106), zt.getZipcode());
    }

    /**
     * Test getErrorMessage(SOAPMessage) with a valid SOAP Fault.
     */
    @Test
    public void testGetSoapFaultMessage() {
        String soapXml = this.buildSoapResponseWithFault();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        Assert.assertNotNull(obj);

        String msg = h.getErrorMessage(obj);
        Assert.assertNotNull(msg);
        Assert.assertEquals("This is an example SOAP fault message",
                msg.trim());
    }

    /**
     * Test getErrorMessage(SOAPMessage) with no SOAP Fault.
     */
    @Test
    public void testGetEmptySoapFaultMessage() {
        String soapXml = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        Assert.assertNotNull(obj);

        String msg = h.getErrorMessage(obj);
        Assert.assertNull(msg);
    }

    /**
     * Test SOAP message contains a fault.
     */
    @Test
    public void testSoapMessageIsError() {
        String soapXml = this.buildSoapResponseWithFault();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        Assert.assertNotNull(obj);

        boolean error = h.isError(obj);
        Assert.assertTrue(error);
    }

    /**
     * Test getErrorMessage(SOAPMessage) with no SOAP Fault.
     */
    @Test
    public void testSoapMessageIsNotError() {
        String soapXml = this.buildSoapRequest();
        SoapMessageHelper h = new SoapMessageHelper();
        SOAPMessage obj = h.getSoapInstance(soapXml);
        Assert.assertNotNull(obj);

        boolean error = h.isError(obj);
        Assert.assertFalse(error);
    }
}
