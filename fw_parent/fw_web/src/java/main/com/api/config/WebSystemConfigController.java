package com.api.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.api.web.controller.AbstractServlet;

/**
 * A servlet which functions to initialize the application.
 * <p>
 * User-defined JVM and application properties are loaded into memory from
 * localized sources that are put into place to be automatically loaded when the
 * web container or server is started.
 * 
 * @author roy terrell
 * 
 */
public class WebSystemConfigController extends AbstractServlet {

    private static final long serialVersionUID = 7358434218143689146L;

    private static Logger logger;

    /**
     * Initialize logging, system/application level configurations, and JDBC
     * connection pool.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Get Application name
        ServletContext ctx = config.getServletContext();
        String appName = ctx.getServletContextName();

        String appServConfigFile = config
                .getInitParameter("AppServerConfigSource");

        String env = config.getInitParameter("Environment");
        SystemConfigurator configUtil = new SystemConfigurator(appName, env);
        configUtil.start(appServConfigFile);
    }

}