package com.api.messaging.jms.handler;

import java.io.Serializable;

import com.api.messaging.handler.MessageHandlerResults;

/**
 * This interface describes what a command can do. It only defines one method,
 * execute, that performs the bulk of the job.
 */
public interface MessageHandlerCommand {
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
