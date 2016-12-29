package com.api.messaging;

import com.RMT2Base;

/**
 * Helper class that manages various properties of a service record such as
 * service name, URL of the service, and its security indicator.
 * 
 * @author appdev
 */
public class MessageRoutingInfo extends RMT2Base {

    /**
     * The server where the service resides.
     */
    private String host;

    /**
     * The name of the web service.
     */
    private String messageId;

    /**
     * The namd of the command the service that is used to identify the business
     * API handler to choose for invocation.
     */
    private String command;

    /**
     * This can be a URL, remote object class name, JMS destination name, or
     * etc.
     */
    private String destination;

    /**
     * Indicats whether or not the service requires authentication.
     */
    private boolean secured;

    /**
     * This is equivalent to resource sub type name
     */
    private String routerType;

    /**
     * The class name of the data handler that will manage the incoming message
     * once the message has reached its destination.
     * <p>
     * This is usually needed for JMS type message processors.
     */
    private String handler;

    /**
     * The id of the reply message.
     */
    private String replyMessageId;

    /**
     * 
     */
    public MessageRoutingInfo() {
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
    public void setMessageId(String name) {
        this.messageId = name;
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

    /**
     * @return the secured
     */
    public boolean isSecured() {
        return secured;
    }

    /**
     * @param secured
     *            the secured to set
     */
    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    /**
     * 
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination
     *            the destination to set
     */
    public void setDestination(String dest) {
        this.destination = dest;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the routerType
     */
    public String getRouterType() {
        return routerType;
    }

    /**
     * @param routerType
     *            the routerType to set
     */
    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    /**
     * @return the handler
     */
    public String getHandler() {
        return handler;
    }

    /**
     * @param handler
     *            the handler to set
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * @return the replyMessageId
     */
    public String getReplyMessageId() {
        return replyMessageId;
    }

    /**
     * @param replyMessageId
     *            the replyMessageId to set
     */
    public void setReplyMessageId(String replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

}
