package testcases.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.api.config.ConfigConstants;
import com.api.config.jaxb.AppServerConfig;
import com.api.config.jaxb.AppServerConfig.ApiConfigurators;
import com.api.config.jaxb.AppServerConfig.ApiConfigurators.ApiConfigurator;
import com.api.config.jaxb.AppServerConfig.ClientUserAuthenticators;
import com.api.config.jaxb.AppServerConfig.ClientUserAuthenticators.Authenticator;
import com.api.config.jaxb.AppServerConfig.CompanyProperties;
import com.api.config.jaxb.AppServerConfig.DestinationMappings;
import com.api.config.jaxb.AppServerConfig.DestinationMappings.Destination;
import com.api.config.jaxb.AppServerConfig.JaxbContexts;
import com.api.config.jaxb.AppServerConfig.JaxbContexts.JaxbContext;
import com.api.config.jaxb.AppServerConfig.MessagingSystemsInfo;
import com.api.config.jaxb.AppServerConfig.ServiceRegistry;
import com.api.config.jaxb.AppServerConfig.ServiceRegistry.Service;
import com.api.config.jaxb.AppServerConfig.SystemProperties;
import com.api.config.jaxb.AppServerConfig.SystemProperties.EmailConfig;
import com.api.config.jaxb.ObjectFactory;
import com.api.util.RMT2File;
import com.api.util.RMT2String2;
import com.api.xml.jaxb.JaxbUtil;

/**
 * Tests the availability and functionality of application configuration.
 * 
 * @author Roy Terrell
 *
 */
public class CommonAppServerConfigurationTest {
    protected static String CONFIG_FILENAME = "xml/RMT2AppServerConfig.xml";
    ObjectFactory f = null;

    @Before
    public void setUp() throws Exception {
        // Be sure to set CONFIG_FILENAME variable here at the descendent...
        f = new ObjectFactory();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfigExistence() {
        Assert.assertNotNull(CONFIG_FILENAME);
        String xmlDoc = null;
        try {
            xmlDoc = RMT2File.getFileContentsAsString(CONFIG_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error loading application configuration file, " + CONFIG_FILENAME);
        }
        Assert.assertNotNull(xmlDoc);
        Assert.assertTrue(RMT2String2.isNotEmpty(xmlDoc));
        System.out.print(xmlDoc);
    }

    @Test
    public void testUnmarshall() {
        String xmlDoc = null;
        try {
            xmlDoc = RMT2File.getFileContentsAsString(CONFIG_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error loading application configuration file, " + CONFIG_FILENAME);
        }
        JaxbUtil jaxb = new JaxbUtil(ConfigConstants.JAXB_CONFIG_PKG);
        AppServerConfig appConfig = null;
        try {
            appConfig = (AppServerConfig) jaxb.unMarshalMessage(xmlDoc);
        } catch (Exception e) {
            Assert.fail("Error unmarshalling XML document to JAXB object");
        }
        Assert.assertNotNull(appConfig.getSystemProperties().getEnvironment());
        Assert.assertEquals("org.apache.xerces.parsers.SAXParser", appConfig.getSystemProperties().getSaxDriver());
        Assert.assertNotNull(appConfig.getCompanyProperties());
        Assert.assertEquals("1343", appConfig.getCompanyProperties().getContactId());
        Assert.assertEquals("RMT2 Business Systems Corp.", appConfig.getCompanyProperties().getCompanyName());
        Assert.assertEquals("rmt2bsc@gmail.com", appConfig.getCompanyProperties().getContactEmail());
        Assert.assertEquals("rmt2.net", appConfig.getCompanyProperties().getWebsite());
        Assert.assertEquals("net.sf.saxon.TransformerFactoryImpl", appConfig.getSystemProperties().getXsltTransformer());

    }
    
    /**
     * Generates the <b>RMT2AppServerConfig.xml</b> XML document in its String
     * representation.
     * <p>
     * This test can be used to verify XML elements that have been added to the
     * associated schema.
     */
    @Test
    public void testMmarshall() {
        String xmlDoc = null;
        
        JaxbUtil jaxb = new JaxbUtil(ConfigConstants.JAXB_CONFIG_PKG);
        AppServerConfig appCofig = f.createAppServerConfig();
        try {
            this.setupAppConfigData(appCofig);
            xmlDoc = jaxb.marshalMessage(appCofig);
            System.out.println(xmlDoc);
            Assert.assertTrue(xmlDoc.contains("/AppServer/config/log4j.properties"));
            Assert.assertTrue(xmlDoc.contains("org.apache.xerces.parsers.SAXParser"));
        } catch (Exception e) {
            Assert.fail("Error marshalling JAXB object to XML");
        }
    }
    
    
    private void setupAppConfigData(AppServerConfig a) {
        // Set AppServer context path to represent the new generation of MacOS
        // systems
        a.setAppConfigContextPath("/System/Volumes/Data/");
        a.setLoggerConfigPath("/AppServer/config/log4j.properties");
        a.setContainerManagedPool("true");
        a.setPageLinkMax((byte) 10);
        a.setPaginationPageSize((byte) 20);
        a.setPollingPage("/polling_wait_page.jsp");
        a.setProtocolInformation("http");
        a.setRemoteSrvcApp("ServiceDispatch");
        a.setRptFileExt(".xsl");
        a.setRptXsltPath("/reports");
        a.setSerialDrive("c:");
        a.setSerialExt(".txt");
        a.setSerialPath("\\temp");
        a.setTimeoutInterval((short) 1800);
        a.setWebAppMapping("AppComandMappings");
        a.setWebAppsDir("/Program Files/Apache Software Foundation/Tomcat 6.0/webapps");
        a.setWebAppsDrive("c\\:");
        a.setDbmsVendor("ASA");
        
        
        CompanyProperties cp = f.createAppServerConfigCompanyProperties();
        cp.setContactId("1343");
        cp.setCompanyName("RMT2 Business Systems Corp");
        cp.setContactFirstname("Roy");
        cp.setContactLastname("Terrell");
        cp.setContactPhone("(214); 498-3935");
        cp.setContactEmail("rmt2bsc@gmail.com");
        cp.setTaxId("752722282");
        cp.setWebsite("rmt2.net");
        a.setCompanyProperties(cp);
        
        SystemProperties sp = f.createAppServerConfigSystemProperties();
        sp.setEnvironment("DEV");
        sp.setSoaphost("http://localhost:8080/ServiceDispatch/soapEngine");
        sp.setSaxDriver("org.apache.xerces.parsers.SAXParser");
        sp.setAuthenticator("com.api.jsp.security.SoapUserAuthenticator");
        sp.setConsumerMsgHandlerMappingsKey("msg_handler_reg_pkg");
        sp.setConsumerMsgHandlerMappingsLocation("config.transactions");
        sp.setXsltTransformer("net.sf.saxon.TransformerFactoryImpl");
        EmailConfig ec = f.createAppServerConfigSystemPropertiesEmailConfig();
        ec.setAuthentication("true");
        ec.setDefaultcontent("text/html");
        ec.setHostPop3("pop.gmail.com");
        ec.setHostSmtp("smtp.gmail.com");
        ec.setInternalSmtpDomain("verizon.net");
        ec.setUserId("rmt2bsd@gmail.com");
        ec.setPassword("Drum8888");
        ec.setResourcetype("file");
        ec.setTemplatePath("\\source\\dotcom5\\aviall\\resources\\templates\\");
        
        // Generates "false" if not included
        ec.setGenerateEmailConfirmation(true);
        sp.setEmailConfig(ec);
        a.setSystemProperties(sp);
        
        ClientUserAuthenticators cua = f.createAppServerConfigClientUserAuthenticators();
        Authenticator auth = f.createAppServerConfigClientUserAuthenticatorsAuthenticator();
        auth.setApplication("authentication");
        auth.setClassName("com.security.JspClientUserAuthenticatorImpl");
        cua.getAuthenticator().add(auth);
        a.setClientUserAuthenticators(cua);

        ApiConfigurators ac = f.createAppServerConfigApiConfigurators();
        ApiConfigurator a1 = f.createAppServerConfigApiConfiguratorsApiConfigurator();
        a1.setClassName("org.modules.AddressBookConfigurator");
        ApiConfigurator a2 = f.createAppServerConfigApiConfiguratorsApiConfigurator();
        a2.setClassName("org.modules.AccountingConfigurator");
        ApiConfigurator a3 = f.createAppServerConfigApiConfiguratorsApiConfigurator();
        a3.setClassName("org.modules.ProjectTrackerConfigurator");
        ApiConfigurator a4 = f.createAppServerConfigApiConfiguratorsApiConfigurator();
        a4.setClassName("org.modules.MediaConfigurator");
        ApiConfigurator a5 = f.createAppServerConfigApiConfiguratorsApiConfigurator();
        a5.setClassName("org.modules.SecurityConfigurator");
        ac.getApiConfigurator().add(a1);
        ac.getApiConfigurator().add(a2);
        ac.getApiConfigurator().add(a3);
        ac.getApiConfigurator().add(a4);
        ac.getApiConfigurator().add(a5);
        a.setApiConfigurators(ac);
        
        
        JaxbContexts jc = f.createAppServerConfigJaxbContexts();
        JaxbContext jc1 = f.createAppServerConfigJaxbContextsJaxbContext();
        jc1.setName("RMT2");
        jc1.setValue("org.rmt2.jaxb");
        JaxbContext jc2 = f.createAppServerConfigJaxbContextsJaxbContext();
        jc2.setName("CONFIG");
        jc2.setValue("com.api.config.jaxb");
        a.setJaxbContexts(jc);
        
        
        MessagingSystemsInfo msi = f.createAppServerConfigMessagingSystemsInfo();
        com.api.config.jaxb.AppServerConfig.MessagingSystemsInfo.System s = f.createAppServerConfigMessagingSystemsInfoSystem();
        s.setName("RMT2");
        s.setUseJndi("true");
        s.setUseAuthentication("false");
        s.setInitialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        s.setConnectionFactory("ConnectionFactory");
        s.setProviderUrl("tcp://localhost:61616");
        s.setUserId("admin");
        s.setPassword("admin");
        s.setMaxConnectAttemps((byte) 30);
        s.setConnectAttemptInterval((short) 30000);
        msi.getSystem().add(s);
        a.setMessagingSystemsInfo(msi);
        
        
        DestinationMappings d = f.createAppServerConfigDestinationMappings();
        Destination d1 = f.createAppServerConfigDestinationMappingsDestination();
        d1.setRmt2Name("JMS_ADDRESSBOOK_QUEUE");
        d1.setJndiName("rmt2.queue.addressbook");
        d1.setContainSuffix("false");
        Destination d2 = f.createAppServerConfigDestinationMappingsDestination();
        d2.setRmt2Name("JMS_ACCOUNTING_QUEUE");
        d2.setJndiName("rmt2.queue.accounting");
        d2.setContainSuffix("false");
        Destination d3 = f.createAppServerConfigDestinationMappingsDestination();
        d3.setRmt2Name("JMS_PROJECTTRACKER_QUEUE");
        d3.setJndiName("rmt2.queue.projecttracker");
        d3.setContainSuffix("false");
        Destination d4 = f.createAppServerConfigDestinationMappingsDestination();
        d4.setRmt2Name("JMS_MEDIA_QUEUE");
        d4.setJndiName("rmt2.queue.media");
        d4.setContainSuffix("false");
        Destination d5 = f.createAppServerConfigDestinationMappingsDestination();
        d5.setRmt2Name("JMS_MEDIA_TOPIC");
        d5.setJndiName("rmt2.topic.media");
        d5.setContainSuffix("false");
        Destination d6 = f.createAppServerConfigDestinationMappingsDestination();
        d6.setRmt2Name("JMS_SECRITY_QUEUE");
        d6.setJndiName("rmt2.queue.authentication");
        d6.setContainSuffix("false");
        d.getDestination().add(d1);
        d.getDestination().add(d2);
        d.getDestination().add(d3);
        d.getDestination().add(d4);
        d.getDestination().add(d5);
        d.getDestination().add(d6);
        a.setDestinationMappings(d);
        
        
        ServiceRegistry sr = f.createAppServerConfigServiceRegistry();
        Service sv1 = f.createAppServerConfigServiceRegistryService();
        sv1.setName("Attach media content to application");
        sv1.setServiceType("JMS");
        sv1.setApplication("media");
        sv1.setModule("attach");
        sv1.setTransaction("LINK_CONTENT_TO_APPLICATION");
        sv1.setDestination("JMS_MEDIA_TOPIC");
        sv1.setDescription("Operaton for linking or attaching media content to the home application");
        sv1.setSecure("false");
        sv1.setHost("unknown");
        Service sv2 = f.createAppServerConfigServiceRegistryService();
        sv2.setName("Query genre");
        sv2.setServiceType("JMS");
        sv2.setApplication("media");
        sv2.setModule("lookup");
        sv2.setTransaction("GET_GENRE");
        sv2.setDestination("JMS_MEDIA_QUEUE");
        sv2.setDescription("Operaton for fetching genres");
        sv2.setSecure("false");
        sv2.setHost("unknown");
        sr.getService().add(sv1);
        sr.getService().add(sv2);
        a.setServiceRegistry(sr);
        
        
        // TODO:  Add more items as needed...
        return;
        
    }
}
