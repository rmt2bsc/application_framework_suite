package testcases.messaging.rmi.objects;

import com.api.messaging.handler.MessageHandlerException;
import com.api.messaging.handler.MessageHandlerInput;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.rmi.service.RmiServiceCommand;

/**
 * @author rterrell
 * 
 */
public class TestCommandOneImpl implements RmiServiceCommand {

    /**
     * 
     */
    public TestCommandOneImpl() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.rmi.RemoteMessageHandlerCommand#processRequest(com.
     * api.messaging.rmi.RmiMessageHandlerInput)
     */
    public MessageHandlerResults processMessage(MessageHandlerInput req)
            throws MessageHandlerException {
        System.out.println("Command: " + req.getCommand());
        System.out.println("Pay Load: " + req.getPayload());
        MessageHandlerResults r = new MessageHandlerResults();
        r.setReturnCode(1);
        r.setPayload("this is a test results from TestCommandOneImpl...RMI call successful!");
        r.setErrorMsg("This is error message #1");
        return r;
    }

}
