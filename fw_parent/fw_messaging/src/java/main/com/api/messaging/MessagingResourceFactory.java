package com.api.messaging;

import com.RMT2Base;
import com.api.config.old.ProviderConfig;

/**
 * Assists clients as a factory for creating various Messaging resources
 * necessary for connections, sessions, destinations and etc. Currently, this
 * api provide implementations for the following messaging resources: SMTP,
 * POP3, JMS, SOAP, RMI, and JAXB binding.
 * 
 * @author RTerrell
 * 
 */
public class MessagingResourceFactory extends RMT2Base {

    // private static Logger logger =
    // Logger.getLogger(MessagingResourceFactory.class);

    public static final String JAXB_DEFAULT_PKG = System
            .getProperty("jms.jaxb.defaultpackage");

    private static MessageBinder DEFAULT_BINDER;

    /**
     * Obtains an empty initialized ProviderConfig instance.
     * 
     * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
     */
    protected static ProviderConfig getConfigInstance() {
        ProviderConfig config = new ProviderConfig();
        return config;
    }

    /**
     * Obtains a reference to the default JAXB binder instance. If the binder
     * has not been initialized, then an instance is created. The default JAXB
     * context is considered to be xml.schema.bindings. This method manages the
     * JAXB binder instance as a Singleton.
     * 
     * @return {@link MessageBinder}
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public static MessageBinder getJaxbMessageBinder() {
        if (MessagingResourceFactory.DEFAULT_BINDER == null) {
            MessagingResourceFactory.DEFAULT_BINDER = new JaxbMessageBinderImpl();
        }
        return MessagingResourceFactory.DEFAULT_BINDER;
    }

    /**
     * Creates an instance of MessageBinder from a JAXB implementation which
     * initializes the JAXB context with the specified package name.
     * 
     * @param jaxbObj
     *            An object that is capable of manipulating XML from within the
     *            JAXP API.
     * @return {@link MessageBinder}
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public static MessageBinder getJaxbMessageBinder(Object jaxbObj) {
        MessageBinder api = new JaxbMessageBinderImpl(jaxbObj);
        return api;
    }

}

// /**
// * Builds HTTP URL provider configuration information from System properties
// *
// * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
// * @throws MessageException
// */
// public static ProviderConfig getHttpConfigInstance() throws MessageException
// {
// String url;
// String server;
// String servicesApp;
// String servicesServlet;
//
// // Build service URL
// server =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_SERVER);
// servicesApp =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_SERVICE_APP);
// servicesServlet =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_SERVICE_SERVLET);
// url = server + "/" + servicesApp + "/" + servicesServlet;
//
// // Validate syntax of URL
// try {
// new URL(url);
// ProviderConfig config = new ProviderConfig();
// config.setHost(url);
// return config;
// }
// catch (MalformedURLException e) {
// String msg = "HTTP Web Service URL, " + url +
// ", is syntactically incorrect.  Other Message: " + e.getMessage();
// logger.log(Level.ERROR, msg);
// throw new MessageException(msg);
// }
// }

// /**
// * Builds SMTP provider configuration information from System properties
// *
// * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
// */
// public static ProviderConfig getSmtpConfigInstance() {
// ProviderConfig config = new ProviderConfig();
// String temp = null;
// temp =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_SMTP_SERVER);
// config.setHost(temp);
//
// // Get Authentication value
// temp =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_MAIL_AUTH);
// boolean authRequired = temp == null ? false : new
// Boolean(temp).booleanValue();
// config.setAuthenticate(authRequired);
//
// String mailUser = null;
// String mailPassword = null;
// // Get user name and password for mail server if authentication is required
// if (authRequired) {
// // Get User Name
// mailUser =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_MAIL_UID);
// // Get Password
// mailPassword =
// System.getProperty(PropertyFileSystemResourceConfigImpl.PROPNAME_MAIL_PW);
// config.setUserId(mailUser);
// config.setPassword(mailPassword);
// }
// return config;
// }
//
// /**
// * Builds SMTP provider configuration from data supplied by the user.
// *
// * @param smtpServerName
// * @param authenticate
// * @param userId
// * @param password
// * @return
// */
// public static ProviderConfig getSmtpConfigInstance(String smtpServerName,
// boolean authenticate, String userId, String password) {
// ProviderConfig config = new ProviderConfig();
// config.setHost(smtpServerName);
// config.setAuthenticate(authenticate);
// if (authenticate) {
// config.setUserId(userId);
// config.setPassword(password);
// }
// return config;
// }

// /**
// *
// * @param pop3ServerName
// * @param authenticate
// * @param userId
// * @param password
// * @return
// */
// public static ProviderConfig getPop3ConfigInstance(String pop3ServerName,
// boolean authenticate, String userId, String password) {
// ProviderConfig config = new ProviderConfig();
// config.setHost(pop3ServerName);
// config.setAuthenticate(authenticate);
// if (authenticate) {
// config.setUserId(userId);
// config.setPassword(password);
// }
// return config;
// }

// /**
// * Obtains the ProviderConfig instance for the SOAP processor. The only
// property set in the ProviderConfig
// * instance will be the SOAP Host. The SOAP Host should be configured as the
// System property and can be
// * fetched as {@link
// com.api.config.PropertyFileSystemResourceConfigImpl.SOAP_HOST SOAP_HOST}
// *
// * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
// */
// public static ProviderConfig getSoapConfigInstance() {
// ProviderConfig config = new ProviderConfig();
// String soapHost =
// System.getProperty(PropertyFileSystemResourceConfigImpl.SOAP_HOST);
// config.setHost(soapHost);
// return config;
// }
