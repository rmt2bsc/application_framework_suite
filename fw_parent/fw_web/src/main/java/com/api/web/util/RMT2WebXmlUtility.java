package com.api.web.util;

import org.apache.log4j.Logger;

import com.api.xml.RMT2XmlUtility;

/**
 * A utility for performing commonly used XML operations. Conatins functionality
 * regarding XSLT transformation and converting java objects to XML.
 * <p>
 * The XSLT functionality offering contains XSLT technology to transform XML
 * data into meaningful output. The output can be in the form of XML, HTML,
 * FO-XSLT, and PDF. The Apache FO Processor is used to convert XSLT-FO
 * documents to PDF's which can be directed to disk or the browser.
 * <p>
 * Also contains functionality to convert javabeans to XML documents, which the
 * the XML documents created are String based. The following javabeans are
 * supported:
 * <ul>
 * <li>primitive data type</li>
 * <li>wrapper objects</li>
 * <li>non-native complex javabeans</li>
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

} // end class

