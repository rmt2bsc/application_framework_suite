package com.api.web.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;

/**
 * Process the commands of the requestor that pertains to reporting via HTTP.
 * 
 * @author appdev
 * 
 * @deprecated moved to com.api.jsp.controller package as ReportingController.
 *             Will be removed in future versions
 * 
 */
public class ReportingServlet extends JavaResponseController {
    private static final long serialVersionUID = 6516468205536911715L;

    protected Properties mappings;

    protected Logger logger;

    public ReportingServlet() throws SystemException {
        super();
        this.logger = Logger.getLogger("ReportingServlet");
        this.logger.log(Level.INFO, "Reporting Servlet initiailized");
    }

    /**
     * Stub method for handling response before it is processed and sent to the
     * client.
     */
    protected void doPreSendResponse(HttpServletRequest request,
            HttpServletResponse response, String url, Object data) {
        return;
    }

}
