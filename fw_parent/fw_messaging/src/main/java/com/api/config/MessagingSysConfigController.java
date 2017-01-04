package com.api.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.webservice.ServiceRegistry;
import com.api.messaging.webservice.ServiceRegistryFactory;
import com.api.web.controller.AbstractServlet;
import com.util.RMT2BeanUtility;

/**
 * A servlet which functions to initialize the messaging environment.
 * <p>
 * Initializing the messaging environment entails obtaining a JAXB context and
 * loading the web service registry into memory.
 * <p>
 * This controller should be loaded after the core system configurator has been
 * intitialized.
 * 
 * @author roy terrell
 * @deprecated No longer needed since all of these configurations are included
 *             in the AppServerConfig.xml
 * 
 */
public class MessagingSysConfigController extends AbstractServlet {

    private static final long serialVersionUID = 7358434218143689146L;

    private static Logger logger = Logger
            .getLogger(MessagingSysConfigController.class);

    private static final String WEB_SERVICE_LOADER_FACTORY_LDAP = "ldap";

    /**
     * Initialize JAXB and load the services registry.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        logger.info("Begin initializing messaging environment...");
        // Load JAXB context into memory a head of time so to not degrade the
        // performance of SOAP/JAXB clients when subsequent calls are made.
        logger.info("Setting up JAXB binder");
        MessagingResourceFactory.getJaxbMessageBinder();

        // Get Application name
        ServletContext ctx = config.getServletContext();
        String appName = ctx.getServletContextName();

        // Load web service registry
        logger.info("Loading web service registry for application" + appName);
        String loaderType = config.getInitParameter("ServiceLoaderFactory");
        if (loaderType == null) {
            throw new ServletException(
                    "Initialization parameter, \"ServiceLoaderFactory\", was not setup for System Configuration Servlet");
        }
        RMT2BeanUtility b = new RMT2BeanUtility();
        ServiceRegistryFactory f = (ServiceRegistryFactory) b.createBean(System
                .getProperty(ConfigConstants.PROPNAME_SERVICE_REGISTRY));
        ServiceRegistry r = null;
        if (loaderType.equalsIgnoreCase(WEB_SERVICE_LOADER_FACTORY_LDAP)) {
            String baseDn = config.getInitParameter("LdapBaseDn");
            String mapperClass = config.getInitParameter("LdapMapperClass");
            r = f.getLdapServiceRegistryManager(baseDn, mapperClass);
        }
        if (r != null) {
            r.loadServices();
        }

        logger.info("Initialization of messaging environment completed!");
    }

}