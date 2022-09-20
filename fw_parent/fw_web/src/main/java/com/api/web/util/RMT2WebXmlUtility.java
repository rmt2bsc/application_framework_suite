package com.api.web.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.web.Request;
import com.api.web.Response;
import com.api.xml.RMT2XmlUtility;

/**
 * A utility for performing commonly used XML operations. Contains functionality
 * regarding XSLT transformation and converting java objects to XML.
 * <p>
 * The XSLT functionality offering contains XSLT technology to transform XML
 * data into meaningful output. The output can be in the form of XML, HTML,
 * FO-XSLT, and PDF. The Apache FO Processor is used to convert XSLT-FO
 * documents to PDF's which can be directed to disk or the browser.
 * <p>
 * Also contains functionality to convert java beans to XML documents, which the
 * the XML documents created are String based. The following java beans are
 * supported:
 * <ul>
 * <li>primitive data type</li>
 * <li>wrapper objects</li>
 * <li>non-native complex java beans</li>
 * <li>List</li>
 * <li>Map</li>
 * <li>Set</li>
 * </ul>
 * The following java native classes are ignored when encountered as a top-level
 * class or nested:
 * <ul>
 * <li>Class</li>
 * <li>ObjectInput</li>
 * <li>FileOutputStream</li>
 * <li>FileInputStream</li>
 * <li>ObjectOutput</li>
 * </ul>
 * 
 * @author appdev
 * 
 */
public class RMT2WebXmlUtility extends RMT2XmlUtility {
    public static Logger logger = Logger.getLogger("RMT2XmlUtility");

    /**
     * Privately constructs a RMT2XmlUtility object.
     * 
     */
    private RMT2WebXmlUtility() {
        super();
    }

    /**
     * Produces an instance of RMT2XmlUtility.
     * 
     * @return {@link RMT2XmlUtility}
     */
    public static RMT2WebXmlUtility getInstance() {
        return new RMT2WebXmlUtility();
    }

    /**
     * Renders a XSLT Formatted Object document to the client's browser in PDF
     * format.
     * 
     * @param srcFileName
     *            The XSLT-FO document to be rendered.
     * @param request
     *            The HTTP request object that may contain data needed to
     *            process PDF document.
     * @param response
     *            The HTTP response object used to send the PDF result to the
     *            browser.
     * @throws SystemException
     */
    public void renderPdf(String srcFileName, Request request, Response response) throws SystemException {
        ByteArrayOutputStream stream = this.renderPdf(srcFileName);
        try {
            // Prepare response
            response.setContentType("application/pdf");
            response.setContentLength(stream.size());

            // Send content to Browser
            ((ServletOutputStream) response.getOutputStream()).write(stream.toByteArray());
            ((ServletOutputStream) response.getOutputStream()).flush();
        } catch (IOException e) {
            this.msg = "IOException:  A problem occurred redering PDF output to browser";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
    }

} // end class

