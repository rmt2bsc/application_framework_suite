package com.api.messaging.rmi.client;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.messaging.MessageException;
import com.api.messaging.handler.MessageHandlerResults;
import com.api.messaging.webservice.router.MessageRoutingException;

/**
 * A class for establishing a connection to a RMI server and invoking one or
 * more remote objects.
 * <p>
 * This class is designed to make repeated calls to the RMI server for a given
 * remote object within a session. Once the session is completed, it is
 * advisable to close the session by calling the <i>close</i> method.
 * 
 * @author Roy Terrell
 * 
 */
public class RmiClientSession extends RMT2Base {

    private static Logger logger;

    private RmiClient session;

    /**
     * Create a Remote Client Session which simply initializes the logger and
     * the internal RMI session.
     * <p>
     * If the RMI session is unable to be created then the internal session
     * instance will be set to null.
     */
    public RmiClientSession() {
        logger = Logger.getLogger(RmiClientSession.class);
        logger.info("logger initialized for RmiClientSession");
        RmiClientFactory f = new RmiClientFactory();
        try {
            this.session = f.getRmiClient();
        } catch (RmiClientException e) {
            logger.error(e);
            this.session = null;
        }
        return;
    }

    /**
     * Invokes a remote object with the message id, command, and data payload.
     * 
     * @param objName
     *            The remote object that is to be invoked.
     * @param messageId
     *            The message id. This is optional.
     * @param command
     *            The message command. This is optional
     * @param payload
     *            The payload. This is optional.
     * @return An instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     *             When the internal RMI session is not initialized, remote
     *             object reference could not be obtained, or the remote object
     *             rendered an error within itself.
     */
    public MessageHandlerResults callRmi(String objName, String messageId,
            String command, Serializable payload)
            throws MessageRoutingException {
        if (this.session == null) {
            throw new MessageRoutingException(
                    "Error sending RMI message due to the RMI session is not initialized");
        }

        MessageHandlerResults results = null;
        try {
            // Make RMI call
            results = this.session.sendMessage(objName, messageId, command,
                    payload);
            return results;
        } catch (RmiClientException e) {
            this.msg = "Error occurred obtaining RMI remote object, " + objName;
            throw new MessageRoutingException(this.msg, e);
        } catch (MessageException e) {
            this.msg = "Error occurred invoking RMI remote object, " + objName
                    + ", for message id[" + messageId + "] and command ["
                    + command + "]";
            throw new MessageRoutingException(this.msg, e);
        }
    }

    /**
     * Disposes of the RMI session object
     */
    public void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

}
