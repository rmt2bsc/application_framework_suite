package com.api.messaging.webservice.http.client;

import java.net.MalformedURLException;
import java.net.URL;

import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.messaging.MessageException;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.webservice.http.HttpResourceFactory;
import com.api.messaging.webservice.http.client.HttpMessageSender;

/**
 * Assists clients as a factory for creating various HTTP Client resources
 * necessary for creating and managing HTTP URL messaging related connections.
 * 
 * @author RTerrell
 * 
 */
public class HttpClientResourceFactory extends HttpResourceFactory {

    /**
     * Creates an instance of HttpMessageSender using
     * SimpleHttpClientMessageImpl implementation
     * 
     * @param inParameters
     *            an arbitrary object implementing the Serializable interface.
     * @return
     */
    public static final HttpMessageSender getHttpInstance() {
        HttpMessageSender obj = new SimpleHttpClientMessageImpl();
        return obj;
    }

    /**
     * Builds HTTP URL provider configuration information from System properties
     * 
     * @return {@link com.api.messaging.ProviderConfig ProviderConfig}
     * @throws MessageException
     */
    public static ProviderConfig getHttpConfigInstance()
            throws MessageException {
        String url;
        String server;
        String servicesApp;
        String servicesServlet;

        // Build service URL
        server = System.getProperty(ConfigConstants.PROPNAME_SERVER);
        servicesApp = System.getProperty(ConfigConstants.PROPNAME_SERVICE_APP);
        servicesServlet = System
                .getProperty(ConfigConstants.PROPNAME_SERVICE_SERVLET);
        url = server + "/" + servicesApp + "/" + servicesServlet;

        // Validate syntax of URL
        try {
            new URL(url);
            ProviderConfig config = MessagingResourceFactory
                    .getConfigInstance();
            config.setHost(url);
            return config;
        } catch (MalformedURLException e) {
            String msg = "HTTP Web Service URL, " + url
                    + ", is syntactically incorrect.  Other Message: "
                    + e.getMessage();
            throw new MessageException(msg);
        }
    }
}
