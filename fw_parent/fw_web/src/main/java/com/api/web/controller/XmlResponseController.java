package com.api.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.constants.RMT2ServletConst;

/**
 * Command Servlet implementation that sends the server's response as a typical
 * XML data stream to a non JSP client. The response is expected to be received
 * from the client action handler via the request object as the attribute,
 * {@link com.constants.RMT2ServletConst.RESPONSE_NONJSP_DATA
 * RESPONSE_NONJSP_DATA}.
 * <p>
 * For example, the client action handler would place the XML response on the
 * request object as: <blockquote> String xml =
 * "&lt;customer&gt;&lt;firstname&gt;&lt;/firstname&gt;&lt;/customer&gt;";<br>
 * this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA, xml);
 * </blockquote>
 * 
 * 
 * @author appdev
 * 
 * @deprecated moved to com.api.jsp.controller package as XmlResponseController.
 *             Will be removed in future versions
 * 
 */
public class XmlResponseController extends AbstractCommandServlet {
    private static final long serialVersionUID = 7482026698494996452L;

    private Logger logger;

    /**
     * Performs general initialization.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.logger = Logger.getLogger("XmlResponseController");
        this.logger
                .log(Level.INFO, "XmlResponseController Servlet initialized");
    }

    protected void doPreSendResponse(HttpServletRequest request,
            HttpServletResponse response, String url, Object data) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.controller.stateless.AbstractCommandServlet#sendResponse(javax.servlet
     * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.String, java.lang.String, java.util.ResourceBundle, boolean)
     */
    @Override
    protected void sendResponse(HttpServletRequest request,
            HttpServletResponse response, String nextURL,
            String requestedCommand, ResourceBundle mappings, boolean error)
            throws ServletException {
        try {
            this.sendXmlStream(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * Sends response data as a XML stream to the client.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void sendXmlStream(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String data = (String) request
                .getAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA);
        // Send XML results to client
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        out.println(data);
        out.close();
    }

}
