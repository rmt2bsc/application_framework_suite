package com.api.xml;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Constants;

/**
 * An implementation of javax.xml.namespace.NamespaceContext interface that will
 * dynamically map namespace prefixes to URI's. This resolver the will allow
 * XPath queries to become namespace aware. When the need arises to match XML
 * elements that are prefixed with a namespace identifier, we must provide our
 * own logistical scheme to query XML elements that are prefixed with namespace
 * identifiers. This implemntation is quite simple, and it relies on a
 * ResourceBundle (NamespacePrefixResolver.properties) which serves as the main
 * datasource for managing a list of namespace URI's with an abbreviation as a
 * key.
 * <p>
 * This namespace prefix resolver is best used in the following way:
 * <blockquote>
 * 
 * <pre>
 * XPathFactory factory = XPathFactory.newInstance();
 * XPath xpath = factory.newXPath();
 * xpath.setNamespaceContext(new NamespacePrefixResolver());
 * </pre>
 * 
 * </blockquote>
 * 
 * @author appdev
 * 
 */
public class NamespacePrefixResolver implements NamespaceContext {

    private static Logger logger = Logger
            .getLogger(NamespacePrefixResolver.class);

    /**
     * Default constructor
     */
    public NamespacePrefixResolver() {
        return;
    }

    /**
     * Locates the namespace URI that is bound to a given prefix. This method
     * relies on the ResourceBundle, NamespacePrefixResolver.properties, as the
     * main datasource for locating the namespace URI related to <i>prefix</i>.
     * 
     * @param prefix
     *            the prefix to lookup
     * @retrun String the namespace that is mapped to the prefix
     * @throws NullPointerException
     *             if <i>prefix</i> is null
     */
    public String getNamespaceURI(String prefix) {
        NamespacePrefixResolver.logger.log(Level.DEBUG,
                "XML URI Prefix to Evaluate: " + prefix);
        if (prefix == null) {
            throw new NullPointerException(
                    "The namespace prefix was found to be null");
        }
        String uri = RMT2XmlUtility.getXmlNamespaceUri(prefix);
        if (uri == null) {
            return XMLConstants.NULL_NS_URI;
        }
        return uri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
     */
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
     */
    public Iterator<String> getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

}
