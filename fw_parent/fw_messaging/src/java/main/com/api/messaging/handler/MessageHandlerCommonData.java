package com.api.messaging.handler;

import java.io.Serializable;
import java.util.List;

import com.RMT2Base;

/**
 * A common model for managing the payload and attachments of message in the
 * form of java objects.
 * 
 * @author rterrell
 * 
 */
public class MessageHandlerCommonData extends RMT2Base implements Serializable {

    private static final long serialVersionUID = -8055073158717964783L;

    protected String messageId;

    protected Serializable payload;

    protected List attachments;

    protected String deliveryMode;

    /**
     * Create a MessageHandlerResults object
     */
    public MessageHandlerCommonData() {
        super();
        return;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     *            the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Returns the payload produced by the message handler.
     * 
     * @return the payload
     */
    public Serializable getPayload() {
        return payload;
    }

    /**
     * Set the payload produced by the message handler.
     * 
     * @param payload
     *            the payload to set
     */
    public void setPayload(Serializable results) {
        this.payload = results;
    }

    /**
     * Return a List of generic objects that are to be attached to the message.
     * 
     * @return the attachments
     */
    public List getAttachments() {
        return attachments;
    }

    /**
     * Set List of generic objects that are to be attached to the message.
     * 
     * @param attachments
     *            the attachments to set
     */
    public void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the deliveryMode
     */
    public String getDeliveryMode() {
        return deliveryMode;
    }

    /**
     * @param deliveryMode
     *            the deliveryMode to set
     */
    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

}
