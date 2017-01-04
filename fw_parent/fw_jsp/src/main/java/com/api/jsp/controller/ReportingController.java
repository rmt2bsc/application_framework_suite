package com.api.jsp.controller;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.SystemException;

/**
 * Process the commands of the requestor that pertains to reporting via HTTP.
 * 
 * @author appdev
 * 
 */
public class ReportingController extends JspResponseController {
    private static final long serialVersionUID = 6516468205536911715L;

    protected Properties mappings;

    protected Logger logger;

    public ReportingController() throws SystemException {
        super();
        this.logger = Logger.getLogger("ReportingController");
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
