package com.api.messaging.jms.handler;

import java.io.Serializable;

import com.api.messaging.handler.MessageHandlerResults;

/**
 * This interface describes what a command can do. It only defines one method,
 * execute, that performs the bulk of the job.
 * 
 * @author roy.terrell
 *
 * @param <T1> The request type to process
 * @param <T2> The response type to process
 * @param <P> The Payload type to process
 */
public interface MessageHandlerCommand<T1, T2, P> {
    /**
     * Processes the messaging command.
     * 
     * @param command
     *            A String in the format of <application>.<module>.<transaction>
     * @param payload
     *            The payload data in an unmarsahlled state.
     * @return MessageHandlerResults
     * @throws MessageHandlerCommandException
     */
    MessageHandlerResults processMessage(String command, Serializable payload)
            throws MessageHandlerCommandException;

}
