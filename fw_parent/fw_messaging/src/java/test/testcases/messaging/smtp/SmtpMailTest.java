package testcases.messaging.smtp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.messaging.MessageException;

import com.api.messaging.email.EmailMessageBean;
import com.api.messaging.email.smtp.SmtpApi;
import com.api.messaging.email.smtp.SmtpFactory;

/**
 * @author appdev
 * 
 */
public class SmtpMailTest {

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
    public void testSmtp() {
        EmailMessageBean msg = new EmailMessageBean();
        msg.setFromAddress("rmt2bsc2@verizon.net");
        // msg.setToAddress("rmt2bsc2@verizon.net");
        msg.setToAddress("roy_terrell@neimanmarcus.com");
        msg.setSubject("SMTP Email Test");
        msg.setBody(
                "This is a mock that represents the body of the Email message",
                EmailMessageBean.HTML_CONTENT);
        // msg.setBody("This is a mock that represents the body of the Email message",
        // EmailMessageBean.TEXT_CONTENT);

        // Use this block of code when system properties are not set
        String serverName = "outgoing.verizon.net";
        boolean auth = true;
        String userId = "rmt2bsc2";
        String password = "drum7777";
        SmtpApi api = SmtpFactory.getSmtpInstance(serverName, auth, userId,
                password);

        // use this verison when system properties are setup
        // SmtpApi api = SmtpFactory.getSmtpInstance();

        if (api == null) {
            return;
        }
        try {
            api.sendMessage(msg);
            api.close();
        } catch (MessageException e) {
            e.printStackTrace();
        }
    }
}
