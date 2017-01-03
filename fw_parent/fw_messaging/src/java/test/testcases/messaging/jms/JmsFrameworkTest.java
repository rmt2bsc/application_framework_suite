package testcases.messaging.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.RMT2RuntimeException;
import com.api.config.SystemConfigurator;
import com.api.messaging.jms.JmsClientManager;
import com.api.messaging.jms.JmsConnectionManager;
import com.api.messaging.jms.JmsConstants;

/**
 * NOTE =========================== Before executing this test, rename
 * 
 * @jndi.properties to jndi.properties which is located in the resources folder.
 * 
 * @author appdev
 * 
 */
public class JmsFrameworkTest {
    private static final String DESTINATION_ACCOUNTING = "Accounting";
    private static final String DESTINATION_CONTACTS = "Contacts";

    private JmsClientManager jms;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        SystemConfigurator config = new SystemConfigurator();
        try {
            config.start("/AppServer/config/RMT2AppServerConfig.xml");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        JmsConnectionManager jmsConMgr = JmsConnectionManager
                .getJmsConnectionManger();

        // Get the appropriate JMS connection based on the messaging
        // system specified in transaction's configuration.
        Connection con = jmsConMgr.getConnection(JmsConstants.DEFAUL_MSG_SYS);
        if (con == null) {
            throw new RMT2RuntimeException(
                    "Error obtaining a connection for JMS client");
        }

        // Associate JMS connection with the client
        this.jms = new JmsClientManager(con, jmsConMgr.getJndi());

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // JmsConnectionManager.getJmsConnectionManger().removeConnection();
    }

    @Test
    public void testSyncAccountingMessageConsumption() {
        Message response = null;
        try {
            TextMessage msg = this.jms
                    .createTextMessage(JmsFrameworkTest.DESTINATION_ACCOUNTING);
            msg.setText(this.getAccountingMessage());
            Destination replyToDest = this.jms.createReplyToDestination(
                    JmsFrameworkTest.DESTINATION_ACCOUNTING, msg);
            this.jms.send(JmsFrameworkTest.DESTINATION_ACCOUNTING, msg);
            response = this.jms.listen(replyToDest, 10000);
        } catch (Exception e) {
            throw new RMT2RuntimeException(e);
        } finally {
            this.jms.stop(JmsFrameworkTest.DESTINATION_ACCOUNTING);
        }

        Assert.assertNotNull(response);
        Assert.assertTrue(response instanceof TextMessage);
        try {
            System.out.println("Reply: " + ((TextMessage) response).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSyncContactsMessageConsumption() {
        Message response = null;
        try {
            TextMessage msg = this.jms
                    .createTextMessage(JmsFrameworkTest.DESTINATION_CONTACTS);
            msg.setText(this.getContactsMessage());
            Destination replyToDest = this.jms.createReplyToDestination(
                    JmsFrameworkTest.DESTINATION_CONTACTS, msg);
            this.jms.send(JmsFrameworkTest.DESTINATION_CONTACTS, msg);
            response = this.jms.listen(replyToDest, 10000);
        } catch (Exception e) {
            throw new RMT2RuntimeException(e);
        } finally {
            this.jms.stop(JmsFrameworkTest.DESTINATION_CONTACTS);
        }

        Assert.assertNotNull(response);
        Assert.assertTrue(response instanceof TextMessage);
        try {
            System.out.println("Reply: " + ((TextMessage) response).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String getContactsMessage() {
        StringBuilder b = new StringBuilder();

        b.append("<RQ_business_contact_search>\n");
        b.append("  <header>\n");
        b.append("      <message_id>RQ_business_contact_search</message_id>\n");
        b.append("      <application>contacts</application>\n");
        b.append("      <module>business</module>\n");
        b.append("      <transaction>getBusiness</transaction>\n");
        b.append("      <delivery_mode>SYNC</delivery_mode>\n");
        b.append("      <message_mode>REQUEST</message_mode>\n");
        b.append("      <delivery_date>02/13/2016 14:36:54</delivery_date>\n");
        b.append("      <user_id>admin</user_id>\n");
        b.append("  </header>\n");
        b.append("  <criteria_data>\n");
        b.append("      <business_id>1430</business_id>\n");
        b.append("      <entity_type xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/>\n");
        b.append("      <service_type xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/>\n");
        b.append("  </criteria_data>\n");
        b.append("</RQ_business_contact_search>\n");
        return b.toString();
    }

    private String getAccountingMessage() {
        StringBuilder b = new StringBuilder();

        b.append("<RQ_accounting_maintenance>\n");
        b.append("  <header>\n");
        b.append("      <message_id>RQ_accounting_maintenance</message_id>\n");
        b.append("      <application>accounting</application>\n");
        b.append("      <module>accounts</module>\n");
        b.append("      <transaction>getBusiness</transaction>\n");
        b.append("      <delivery_mode>SYNC</delivery_mode>\n");
        b.append("      <message_mode>REQUEST</message_mode>\n");
        b.append("      <delivery_date>02/13/2016 14:36:54</delivery_date>\n");
        b.append("      <user_id>admin</user_id>\n");
        b.append("  </header>\n");
        b.append("  <criteria_data>\n");
        b.append("      <business_id>1430</business_id>\n");
        b.append("      <entity_type xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/>\n");
        b.append("      <service_type xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/>\n");
        b.append("  </criteria_data>\n");
        b.append("</RQ_accounting_maintenance>\n");
        return b.toString();
    }

}
