package com.api.ldap.beans;

import java.util.List;

/**
 * 
 * @author Roy Terrell
 * 
 */
public class LdapResource {

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

    /**
     * 
     */
    public LdapResource() {
        super();
    }

    /**
     * @return the isSecured
     */
    public String getIsSecured() {
        return isSecured;
    }

    /**
     * @param isSecured
     *            the isSecured to set
     */
    public void setIsSecured(String isSecured) {
        this.isSecured = isSecured;
    }

    /**
     * @return the requestUrl
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * @param requestUrl
     *            the requestUrl to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @return the replyMsgId
     */
    public String getReplyMsgId() {
        return replyMsgId;
    }

    /**
     * @param replyMsgId
     *            the replyMsgId to set
     */
    public void setReplyMsgId(String replyMsgId) {
        this.replyMsgId = replyMsgId;
    }

    /**
     * @return the rsrcTypeName
     */
    public String getRsrcTypeName() {
        return rsrcTypeName;
    }

    /**
     * @param rsrcTypeName
     *            the rsrcTypeName to set
     */
    public void setRsrcTypeName(String rsrcTypeName) {
        this.rsrcTypeName = rsrcTypeName;
    }

    /**
     * @return the rsrcSubTypeName
     */
    public String getRsrcSubTypeName() {
        return rsrcSubTypeName;
    }

    /**
     * @param rsrcSubTypeName
     *            the rsrcSubTypeName to set
     */
    public void setRsrcSubTypeName(String rsrcSubTypeName) {
        this.rsrcSubTypeName = rsrcSubTypeName;
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
     * @return the cn
     */
    public List<String> getCn() {
        return cn;
    }

    /**
     * @param cn
     *            the cn to set
     */
    public void setCn(List<String> cn) {
        this.cn = cn;
    }

    /**
     * @return the description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * @return the ou
     */
    public List<String> getOu() {
        return ou;
    }

    /**
     * @param ou
     *            the ou to set
     */
    public void setOu(List<String> ou) {
        this.ou = ou;
    }

}
