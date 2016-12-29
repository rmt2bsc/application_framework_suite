package com.api.messaging.handler;

/**
 * Model for managing the command data for an arbitrary message handler
 * invocation.
 * 
 * @author rterrell
 * 
 */
public class MessageHandlerInput extends MessageHandlerCommonData {

    private static final long serialVersionUID = -8055073158717964783L;

    /**
     * The target action of the web service invocation.
     * <p>
     * This piece of data is useful to the message receptor in a way to
     * determine the correct business API handler for processing the message.
     */
    protected String command;

    /**
     * Create a MessageHandlerResults object
     */
    public MessageHandlerInput() {
        super();
        return;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

}
