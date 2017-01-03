package testcases.messaging.rmi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.config.old.ProviderConfig;
import com.api.messaging.MessageException;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.rmi.RmiMessageHandlerInput;
import com.api.messaging.rmi.client.RmiClient;
import com.api.messaging.rmi.client.RmiClientException;
import com.api.messaging.rmi.client.RmiClientFactory;
import com.api.messaging.rmi.engine.RmiEngine;

public class RmiTest {

    private RmiEngine engine;

    @Before
    public void setUp() throws Exception {
        RmiClientFactory f = new RmiClientFactory();
        ProviderConfig rmiConfig = f.createProviderConfigFromPropertiesFile();
        this.engine = new RmiEngine(rmiConfig);
        Thread t = new Thread(this.engine);
        t.start();
        return;
    }

    @After
    public void tearDown() throws Exception {
        this.engine.clearRegistry();
        this.engine = null;
    }

    @Test
    public void invokeCommandObject() {
        RmiClient client = null;
        try {
            RmiClientFactory f = new RmiClientFactory();
            client = f.getRmiClient();
            RmiMessageHandlerInput input = new RmiMessageHandlerInput();

            input.setRemoteObjectName("TestCommand1");
            input.setMessageId("RQ_TEST_1");
            input.setCommand("payload_1");
            MessageHandlerResults r = client.sendMessage("TestCommand1",
                    "RQ_TEST_1", "payload_1", input);
            System.out.println("Return Code: " + r.getReturnCode());
            System.out.println("Result Message: " + r.getErrorMsg());
            System.out.println("Result Data: " + r.getPayload());

            input.setRemoteObjectName("TestCommand2");
            input.setMessageId("RQ_TEST_2");
            input.setCommand("payload_2");
            MessageHandlerResults r2 = client.sendMessage("TestCommand2",
                    "RQ_TEST_2", "payload_2", input);
            System.out.println("Return Code: " + r2.getReturnCode());
            System.out.println("Result Message: " + r2.getErrorMsg());
            System.out.println("Result Data: " + r2.getPayload());
        } catch (RmiClientException e) {
            e.printStackTrace();
        } catch (MessageException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return;
    }
}
