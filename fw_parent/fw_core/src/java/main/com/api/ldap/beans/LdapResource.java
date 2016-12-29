package com.api.ldap.beans;

import com.api.ldap.beans.LdapCommonEntity;

/**
 * ORM bean for mapping resource related data coming from a LDAP server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapResource extends LdapCommonEntity {

    private String rsrcTypeName;

    private String rsrcSubTypeName;

    // private String isSecured;
    //
    // private String requestUrl;
    //
    // private String replyMsgId;
    //
    // private String host;
    //
    // private String routerType;

    /**
     * Creates a LdapResource object withou intializing any of its properties.
     */
    public LdapResource() {
        super();
    }

    // /**
    // * Get the security flag for resource
    // *
    // * @return
    // * the secured flag as a String
    // *
    // */
    // public String getIsSecured() {
    // return isSecured;
    // }
    //
    // /**
    // * Set the security flag for resource
    // *
    // * @param isSecured
    // * the flag to set
    // */
    // public void setIsSecured(String isSecured) {
    // this.isSecured = isSecured;
    // }
    //
    // /**
    // * Get the request URL
    // *
    // * @return
    // * the requestUrl
    // */
    // public String getRequestUrl() {
    // return requestUrl;
    // }
    //
    // /**
    // * Set the request URL
    // * @param requestUrl
    // * the request URL to set
    // */
    // public void setRequestUrl(String requestUrl) {
    // this.requestUrl = requestUrl;
    // }
    //
    // /**
    // * Get the reply message id
    // *
    // * @return
    // * the reply message id
    // */
    // public String getReplyMsgId() {
    // return replyMsgId;
    // }
    //
    // /**
    // * Set the reply message id
    // *
    // * @param replyMsgId
    // * the reply message id to set
    // */
    // public void setReplyMsgId(String replyMsgId) {
    // this.replyMsgId = replyMsgId;
    // }

    /**
     * Get the resource type name
     * 
     * @return the resource type name
     */
    public String getRsrcTypeName() {
        return rsrcTypeName;
    }

    /**
     * Set the resource type name
     * 
     * @param rsrcTypeName
     *            the rsrcTypeName to set
     */
    public void setRsrcTypeName(String rsrcTypeName) {
        this.rsrcTypeName = rsrcTypeName;
    }

    /**
     * Get the resource sub type name
     * 
     * @return the resource sub type name
     */
    public String getRsrcSubTypeName() {
        return rsrcSubTypeName;
    }

    /**
     * Set the resource sub type name
     * 
     * @param rsrcSubTypeName
     *            the resource sub type name
     */
    public void setRsrcSubTypeName(String rsrcSubTypeName) {
        this.rsrcSubTypeName = rsrcSubTypeName;
    }

    // /**
    // * Get the host name
    // *
    // * @return
    // * the host
    // */
    // public String getHost() {
    // return host;
    // }
    //
    // /**
    // * Set the host
    // *
    // * @param host
    // * the host to set
    // */
    // public void setHost(String host) {
    // this.host = host;
    // }
    //
    // /**
    // * Get the router type
    // *
    // * @return
    // * the router type
    // */
    // public String getRouterType() {
    // return routerType;
    // }
    //
    // /**
    // * Set the router type
    // *
    // * @param routerType
    // * the router type to set
    // */
    // public void setRouterType(String routerType) {
    // this.routerType = routerType;
    // }
}
