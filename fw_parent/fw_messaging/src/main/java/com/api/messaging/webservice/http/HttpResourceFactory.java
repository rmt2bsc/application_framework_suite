package com.api.messaging.webservice.http;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.config.ConfigConstants;

import com.api.messaging.MessagingResourceFactory;

/**
 * Assists clients as a factory for creating various HTTP Client resources
 * necessary for creating and managing HTTP URL messaging related connections.
 * 
 * @author RTerrell
 * 
 */
public class HttpResourceFactory extends MessagingResourceFactory {

    /**
     * Builds the base part of the HTTP URL which will function to identify and
     * invoke the requested web service. The componentes that make up the base
     * part of the URL are the <i>server</i> + <i>services app name</i> +
     * <i>servlet</i>. These components are configured as the web containter
     * environment variables and are assigned to the System properties
     * collection during server startup.
     * 
     * @return URL as a String.
     * @throws HttpException
     *             When the URL is not found, general error loading or obtaining
     *             list of services, or if the URL is syntactically incorrect.
     */
    public static String getServiceDispatchUrl() throws HttpException {
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
            return url;
        } catch (MalformedURLException e) {
            String msg = "Remote Service URL, " + url
                    + ", is syntactically incorrect.  Other Message: "
                    + e.getMessage();
            Logger.getLogger(HttpResourceFactory.class).log(Level.ERROR, msg);
            throw new HttpException(msg);
        }
    }

}
