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
     * The name of the service
     */
    private String name;

    /**
     * This is equivalent to resource sub type name which represents the service
     * transport type
     */
    private String routerType;

    /**
     * Application code
     */
    private String applicatoinId;

    /**
     * Module code
     */
    private String moduleId;

    /**
     * The name of the web service also known as the transaction id.
     */
    private String messageId;

    /**
     * This can be a URL, remote object class name, JMS destination name, or
     * etc.
     */
    private String destination;

    /**
     * The description of the web service route.
     */
    private String description;

    /**
     * Indicats whether or not the service requires authentication.
     */
    private boolean secured;

    /**
     * The server where the service resides.
     */
    private String host;

    /**
     * The namd of the command the service that is used to identify the business
     * API handler to choose for invocation.
     */
    private String command;

    /**
     * The mode in which the service is to be transported. Valid values are
     * ASYNC and SYNC.
     */
    private String deliveryMode;

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the applicatoinId
     */
    public String getApplicatoinId() {
        return applicatoinId;
    }

    /**
     * @param applicatoinId
     *            the applicatoinId to set
     */
    public void setApplicatoinId(String applicatoinId) {
        this.applicatoinId = applicatoinId;
    }

    /**
     * @return the moduleId
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     *            the moduleId to set
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
