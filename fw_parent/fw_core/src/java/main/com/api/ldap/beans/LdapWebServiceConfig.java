package com.api.ldap.beans;

import java.util.List;

/**
 * ORM bean for mapping web service resource related data to and from a LDAP
 * server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapWebServiceConfig {

    private List<String> cn;

    private List<String> description;

    private List<String> ou;

    private String rsrcTypeName;

    private String rsrcSubTypeName;

    private String isSecured;

    private String requestUrl;

    private String replyMsgId;

    private String host;

    private String routerType;

    private String routerMode;

    /**
     * Construct a LdapWebServiceConfig object
     */
    public LdapWebServiceConfig() {
        super();
    }

    /**
     * Get the security flag for resource
     * 
     * @return the secured flag as a String
     * 
     */
    public String getIsSecured() {
        return isSecured;
    }

    /**
     * Set the security flag for resource
     * 
     * @param isSecured
     *            the flag to set
     */
    public void setIsSecured(String isSecured) {
        this.isSecured = isSecured;
    }

    /**
     * Get the request URL
     * 
     * @return the requestUrl
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Set the request URL
     * 
     * @param requestUrl
     *            the request URL to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * Get the reply message id
     * 
     * @return the reply message id
     */
    public String getReplyMsgId() {
        return replyMsgId;
    }

    /**
     * Set the reply message id
     * 
     * @param replyMsgId
     *            the reply message id to set
     */
    public void setReplyMsgId(String replyMsgId) {
        this.replyMsgId = replyMsgId;
    }

    /**
     * Get the host name
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the host
     * 
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get the router type
     * 
     * @return the router type
     */
    public String getRouterType() {
        return routerType;
    }

    /**
     * Set the router type
     * 
     * @param routerType
     *            the router type to set
     */
    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    /**
     * Return the resource type as web service
     * 
     * @return A String described as <i>Web Service</i>
     */
    public String getRsrcTypeName() {
        return rsrcTypeName;
    }

    /**
     * Set the resource type as web service
     * 
     * @param rsrcTypeName
     *            A String described as <i>Web Service</i>.
     */
    public void setRsrcTypeName(String rsrcTypeName) {
        this.rsrcTypeName = rsrcTypeName;
    }

    /**
     * Return the type of web service
     * 
     * @return the name of the web service type.
     */
    public String getRsrcSubTypeName() {
        return rsrcSubTypeName;
    }

    /**
     * Set the type of web service
     * 
     * @param rsrcSubTypeName
     *            the name of the web service type
     */
    public void setRsrcSubTypeName(String rsrcSubTypeName) {
        this.rsrcSubTypeName = rsrcSubTypeName;
    }

    /**
     * Return the actual name of the web service
     * 
     * @return the name of the web service
     */
    public List<String> getCn() {
        return cn;
    }

    /**
     * Set the actual name of the web service
     * 
     * @param cn
     *            the name of the web service
     */
    public void setCn(List<String> cn) {
        this.cn = cn;
    }

    /**
     * Return the description of the web service.
     * 
     * @return the description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Set the description of the web service.
     * 
     * @param description
     *            the description of the web service.
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * Set the name of the web service type.
     * 
     * @return String
     */
    public List<String> getOu() {
        return ou;
    }

    /**
     * Return the name of the web service type
     * 
     * @param ou
     *            String
     */
    public void setOu(List<String> ou) {
        this.ou = ou;
    }

    /**
     * Return value that indicates if message will be routed synchronously or
     * asynchronously
     * 
     * @return String
     */
    public String getRouterMode() {
        return routerMode;
    }

    /**
     * Set value that indicates if message will be routed synchronously or
     * asynchronously
     * 
     * @param routerMode
     *            the router mode
     */
    public void setRouterMode(String routerMode) {
        this.routerMode = routerMode;
    }
}
