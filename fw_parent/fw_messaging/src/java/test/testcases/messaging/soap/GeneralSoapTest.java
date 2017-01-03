package testcases.messaging.soap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.config.old.ProviderConfig;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.soap.client.SoapClientFactory;
import com.api.messaging.webservice.soap.client.SoapClientWrapper;
import com.util.RMT2File;

/**
 * @author appdev
 * 
 */
public class GeneralSoapTest {
    private static final String SOAP_HOST = "http://localhost:8080/ServiceDispatch/soapEngine";

    String soapXml;

    String soapFaultXml;

    String soapBodyXml;

    String errorXml;

    String rmt2WsXml;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header xmlns:rmt2-hns=\"http://www.rmt2.net/soapheader\"><rmt2-hns:serviceId SOAP-ENV:actor=\"http://www.rmt2.net\" SOAP-ENV:encodingStyle=\"http://www.theterrells.info\" SOAP-ENV:mustUnderstand=\"1\">checkapp</rmt2-hns:serviceId></SOAP-ENV:Header><SOAP-ENV:Body><SalesOrderExtTest><beanClassName>testcases.bean.SalesOrderExtTest</beanClassName><className/><customerId>1010</customerId><dateCreated>3-12-2008</dateCreated><dateUpdated>4-1-2008</dateUpdated><id>444</id><invoiced>0</invoiced><methodName/><orderTotal>777.77</orderTotal><userId/></SalesOrderExtTest></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        this.soapFaultXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header xmlns:rmt2-hns=\"http://www.rmt2.net/soapheader\"><rmt2-hns:serviceId SOAP-ENV:actor=\"http://www.rmt2.net\" SOAP-ENV:encodingStyle=\"http://www.theterrells.info\" SOAP-ENV:mustUnderstand=\"1\">checkapp</rmt2-hns:serviceId></SOAP-ENV:Header><SOAP-ENV:Body><SOAP-ENV:Fault><faultcode>SOAP-ENV:Client</faultcode><faultstring>Fault Text message</faultstring><detail><subMessage>I used JUnit to test this snippet of code</subMessage><errorMessage>This is test of the Fault Detail message</errorMessage><errorCode>10</errorCode></detail></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        this.soapBodyXml = "<SalesOrderExtTest><beanClassName>testcases.bean.SalesOrderExtTest</beanClassName><className /><customerId>1010</customerId><dateCreated>3-12-2008</dateCreated><dateUpdated>4-1-2008</dateUpdated><id>444</id><invoiced>0</invoiced><methodName /><orderTotal>3240.0</orderTotal><userId /></SalesOrderExtTest>";
        this.errorXml = "<errorcode>1234</errorcode><message>This is a test error message in the detail section</message>";
        // this.rmt2WsXml =
        // "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header><rmt2-hns:serviceId xmlns:rmt2-hns=\"http://www.rmt2.net/soapheader\" SOAP-ENV:actor=\"\" SOAP-ENV:mustUnderstand=\"1\">RQ_business_contact_search</rmt2-hns:serviceId></SOAP-ENV:Header><SOAP-ENV:Body><RQ_business_contact_search><header><message_id>RQ_business_contact_search</message_id><delivery_mode>SYNC</delivery_mode><message_mode>REQUEST</message_mode><delivery_date>08/08/2010 07:44:28</delivery_date><user_id>admin</user_id></header><criteria_data><business_id>1461</business_id></criteria_data></RQ_business_contact_search></SOAP-ENV:Body></SOAP-ENV:Envelope>";

        String filename = "C:\\projects\\webservices\\databinding\\src\\xml\\test\\Raw_RQ_business_contact_search_single.xml";
        this.rmt2WsXml = RMT2File.inputFile(filename);

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRMT2WsClientWithAttachment() {
        SoapClientWrapper client = new SoapClientWrapper();
        // This condition is for the test environment since configuration data
        // is
        // loaded at server start up and this test does not execute within the
        // same
        // JVM as the Web Server
        ProviderConfig config = SoapClientFactory.getSoapConfigInstance();
        if (config.getHost() == null) {
            config.setHost(GeneralSoapTest.SOAP_HOST);
        }
        try {
            client.setConfig(config);
            File file = new File("c:/data/images/me.jpg");
            if (file.exists()) {
                List attachments = new ArrayList<Object>();
                attachments.add(file);
                client.callSoap(this.rmt2WsXml, attachments);
            }
            else {
                client.callSoap(this.rmt2WsXml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRMT2WsClientWithoutAttachment() {
        SoapClientWrapper client = new SoapClientWrapper();
        // This condition is for the test environment since configuration data
        // is
        // loaded at server start up and this test does not execute within the
        // same
        // JVM as the Web Server
        ProviderConfig config = SoapClientFactory.getSoapConfigInstance();
        if (config.getHost() == null) {
            config.setHost(GeneralSoapTest.SOAP_HOST);
        }
        try {
            client.setConfig(config);
            client.callSoap(this.rmt2WsXml);
        } catch (MessageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Test
    // public void testSoapFactory() {
    // try {
    // SOAPFactory factory = SOAPFactory.newInstance();
    // SOAPElement e1 = factory.createElement("TestElement");
    // SOAPElement e2 = factory.createElement("TestElement2", "ns1",
    // "www.rmt2.net/test1");
    // Name n1 = factory.createName("TestName1");
    // Name n2 = factory.createName("TestName2", "ns2", "www.rmt2.net/test2");
    // String nameVal = e2.getLocalName();
    // Name name2 = e2.getElementName();
    // nameVal = name2.getQualifiedName();
    // nameVal = n2.getQualifiedName();
    // return;
    // }
    // catch (SOAPException e1) {
    // e1.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testAttachment() {
    // SOAPMessage sm = null;
    // Object content = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // File file = new File("c:/data/images/me.jpg");
    // AttachmentPart ap = builder.createAttachment(file);
    // String contentType = ap.getContentType();
    // content = ap.getContent();
    // sm = (SOAPMessage) xmlProd.toObject();
    // Iterator<AttachmentPart> iter = sm.getAttachments();
    // if (iter.hasNext()) {
    // AttachmentPart ap2 = iter.next();
    // System.out.println(ap2.getContentType());
    // }
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    //
    // String xml2 = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(sm);
    // Product xmlProd = ProductDirector.deConstruct(builder);
    // xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(xml2);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // Iterator<AttachmentPart> iter = sm.getAttachments();
    // if (iter.hasNext()) {
    // AttachmentPart ap2 = iter.next();
    // System.out.println(ap2.getContentType());
    // }
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    // }
    //
    //
    // @Test
    // public void testAttachmentSend() {
    // SOAPConnection connection;
    // try {
    // SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
    // connection = sfc.createConnection();
    // }
    // catch (SOAPException e) {
    // throw new RuntimeException(e);
    // }
    //
    //
    // SOAPMessage sm = null;
    // Object content = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // File file = new File("c:/data/images/me.jpg");
    // AttachmentPart ap = builder.createAttachment(file);
    // String contentType = ap.getContentType();
    // content = ap.getContent();
    // sm = (SOAPMessage) xmlProd.toObject();
    //
    // SOAPMessage response = connection.call(sm, GeneralSoapTest.SOAP_HOST);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    // }
    //
    //
    // @Test
    // public void testCustomSoapBuild() {
    // SOAPMessage sm = null;
    // try {
    // SoapProductBuilder builder = SoapClientFactory.getSoapBuilderInstance();
    // builder.addHeaderElement("serviceId", "checkapp", "www.rmt2.net/actor",
    // true, "http://www.dell.com");
    // builder.addHeaderElement("queueName", "INPUT_QUEUE",
    // "www.rmt2.net/queue_actor", true, "http://www.officedepot.com");
    // builder.addBody(this.soapBodyXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    //
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(sm);
    // Product xmlProd = ProductDirector.deConstruct(builder);
    // String xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testSoapDeserialization() {
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // SOAPMessage sm = (SOAPMessage) xmlProd.toObject();
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    // }
    //
    // @Test
    // public void testCreateSoapObjectUsingParms() {
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance("getCustomers", null,
    // this.soapBodyXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // SOAPMessage sm = (SOAPMessage) xmlProd.toObject();
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testSoapSerialization() {
    // SOAPMessage sm = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(sm);
    // Product xmlProd = ProductDirector.deConstruct(builder);
    // String xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testBuildFaultMessage() {
    // try {
    // String serviceId = "serviceId";
    // String queue = "RMT2_CUSTOMER_QUEUE";
    // String faultCode = "Client";
    // String faultMsg = "Fault Text message";
    // String actor = "http://www.theterrells.info";
    // Map details = new HashMap();
    // details.put("errorCode", -100);
    // details.put("errorMessage", "This is test of the Fault Detail message");
    // details.put("subMessage", "I used JUnit to test this snippet of code");
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(serviceId, queue, faultCode,
    // faultMsg, actor, details);
    // Product xmlProd = ProductDirector.construct(builder);
    // SOAPMessage sm = (SOAPMessage) xmlProd.toObject();
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testBuildFaultMessageWithoutHeader() {
    // try {
    // String faultCode = "Client";
    // String faultMsg = "Fault Text message";
    // String actor = "http://www.theterrells.info";
    // Map details = new HashMap();
    // details.put("errorCode", 10);
    // details.put("errorMessage", "This is test of the Fault Detail message");
    // details.put("subMessage", "I used JUnit to test this snippet of code");
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(null, null, faultCode, faultMsg,
    // actor, details);
    // Product xmlProd = ProductDirector.construct(builder);
    // SOAPMessage sm = (SOAPMessage) xmlProd.toObject();
    // String strXml = xmlProd.toString();
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testSerializeFaultMessageWithoutHeader() {
    // SOAPMessage sm = null;
    // try {
    // String faultCode = "Client";
    // String faultMsg = "Fault Text message";
    // String actor = "http://www.theterrells.info";
    // Map details = new HashMap();
    // details.put("errorCode", 777);
    // details.put("errorMessage", "This is test of the Fault Detail message");
    // details.put("subMessage", "I used JUnit to test this snippet of code");
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(null, null, faultCode, faultMsg,
    // actor, details);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // xmlProd = ProductDirector.deConstruct(builder);
    // String xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testSoapFaultDeserialization() {
    // SOAPMessage sm = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapFaultXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // // Check results by deserializing.
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(sm);
    // Product xmlProd = ProductDirector.deConstruct(builder);
    // String xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // return;
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testSoapDao() {
    // String msg;
    //
    // // Service Id is required.
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(soapXml);
    // // Perform query
    // int rows = builder.locateXmlNode(SoapProductBuilder.SOAP_ENVELOPE_NS +
    // ":Header");
    // if (rows <= 0) {
    // System.out.println("Incoming SOAP message is invalid due to Header is not present");
    // return;
    // }
    // // Begin to retrieve the results
    // String qualifiedName = SoapProductBuilder.HEADER_NS + ":" +
    // GeneralConst.REQ_SERVICEID;
    // List<String> results;
    // try {
    // results = builder.getElementValue(qualifiedName);
    // String serviceId = null;
    // for (String item : results) {
    // serviceId = item;
    // }
    // System.out.println("Service Id: " + serviceId);
    // }
    // catch (SoapBuilderException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testStandaloneSoapServer() {
    // TestSoapServer server = new TestSoapServer();
    // String response = server.processRequest(this.soapXml);
    // // try {
    // // server.processRequest(this.soapXml);
    // // }
    // // catch (SoapServiceException e) {
    // // e.printStackTrace();
    // // }
    // System.out.println(response);
    // }
    //
    // @Test
    // public void testSendSoapMessage() {
    // // Create message object
    // SOAPMessage sm = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance("checkapp", null,
    // this.soapBodyXml);
    // Product xmlProd = ProductDirector.construct(builder);
    // sm = (SOAPMessage) xmlProd.toObject();
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // // Serialized message object.
    // String xml2 = null;
    // try {
    // SoapProductBuilder builder =
    // SoapClientFactory.getSoapBuilderInstance(sm);
    // Product xmlProd = ProductDirector.deConstruct(builder);
    // xml2 = xmlProd.toString();
    // System.out.println(xml2);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // // Send serialized message to web service.
    // SoapClient api = SoapClientFactory.getClient();
    // try {
    // ProviderConfig config = MessagingResourceFactory.getSoapConfigInstance();
    //
    // // This condition is for the test environment since configuration data is
    // // loaded at server start up and this test does not execute within the
    // same
    // // JVM as the Web Server
    // if (config.getHost() == null) {
    // config.setHost(GeneralSoapTest.SOAP_HOST);
    // }
    // api.connect(config);
    // api.sendMessage(xml2);
    // Serializable results = api.getMessage();
    // System.out.println("Results: " + results.toString());
    // }
    // catch (SoapBuilderException e) {
    // System.out.println("SoapBuilderException occurred");
    // e.printStackTrace();
    // return;
    // }
    // catch (ProviderConnectionException e) {
    // System.out.println("ProviderConnectionException occurred");
    // e.printStackTrace();
    // return;
    // }
    // catch (MessageException e) {
    // System.out.println("MessageException occurred");
    // e.printStackTrace();
    // }
    // finally {
    // api.close();
    // }
    // }

    //
    // @Test
    // public void testHttpClientSerialziedStringMessage() {
    // SoapSyncTransportImpl soap;
    // try {
    // soap = SoapSyncTransportImpl.getInstance();
    // soap.connect(null);
    // }
    // catch (SoapBuilderException e1) {
    // return;
    // }
    // catch (ProviderConnectionException e1) {
    // return;
    // }
    // try {
    // String msg =
    // "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header xmlns:rmt2-hns=\"header\"><rmt2-hns:serviceId SOAP-ENV:actor=\"http://www.rmt2.net\" SOAP-ENV:encodingStyle=\"http://www.theterrells.info\" SOAP-ENV:mustUnderstand=\"1\">RMT2BSC</rmt2-hns:serviceId></SOAP-ENV:Header><SOAP-ENV:Body><SalesOrderExtTest><beanClassName>testcases.bean.SalesOrderExtTest</beanClassName><className/><customerId>1010</customerId><dateCreated>3-12-2008</dateCreated><dateUpdated>4-1-2008</dateUpdated><id>444</id><invoiced>0</invoiced><methodName/><orderTotal>777.77</orderTotal><userId/></SalesOrderExtTest></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    // Object results = soap.sendStringMessage(msg);
    // System.out.println(results);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
