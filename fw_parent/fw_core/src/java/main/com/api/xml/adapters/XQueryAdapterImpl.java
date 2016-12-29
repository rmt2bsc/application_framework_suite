package com.api.xml.adapters;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.NotFoundException;
import com.RMT2Base;
import com.RMT2Exception;
import com.SystemException;
import com.api.DaoApi;
import com.api.persistence.DatabaseException;
import com.api.xml.XmlApiFactory;
import com.api.xml.XmlDao;
import com.util.RMT2BeanUtility;
import com.util.RMT2Utility;

/**
 * An implementation of XmlBeanAdapter interface using XQuery technologies.
 * 
 * @author appdev
 * 
 */
class XQueryAdapterImpl extends RMT2Base implements XmlBeanAdapter {
    protected static final String BEANCLASSNAME = "beanClassName";

    private Logger logger;

    /**
     * Default Constructor which simply creates a NativeAdapterImpl object.
     */
    public XQueryAdapterImpl() {
        super();
        this.logger = Logger.getLogger("XQueryAdapterImpl");
        this.logger.log(Level.INFO, "Logger created");
        return;
    }

    /**
     * Converts <i>bean</i> to a XML Element object. Returns the String
     * representation of <i>bean</i> as a XML document. The following javabeans
     * are supported: primitive data types, non-native complex javabeans, List,
     * Map, and Set objects. When <i>bean</i> is of type List, Set, or Map, the
     * outer element will be identified as <i>Collection</i>. Otherwise, the
     * outer element's name is that of the target class.
     * 
     * @param bean
     *            The target javabean.
     * @return String representation as a XML document.
     * @throws SystemException
     */
    public String beanToXml(Object bean) throws SystemException {
        if (bean == null) {
            return null;
        }

        Element root = null;
        if (bean instanceof List || bean instanceof Map || bean instanceof Set) {
            // root = buildComplexBeanElement("Collection", bean);
        }
        else {
            // root = this.buildRootElement(bean);
        }
        if (root == null) {
            return null;
        }

        // Output document as a String
        Document doc = new Document(root);
        XMLOutputter serializer = new XMLOutputter(Format.getCompactFormat()
                .setOmitDeclaration(true));
        String xml = serializer.outputString(doc);

        return xml;
    }

    /**
     * 
     * @param xml
     * @return
     * @throws SystemException
     */
    public Object xmlToBean(String xml) throws SystemException {
        // RMT2BeanUtility beanUtil = null;
        try {
            // beanUtil = this.createBeanInstance(xml);
            // Object bean = this.buildBeanProperty(beanUtil, xml, "/");
            // return bean;
            return null;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

}
