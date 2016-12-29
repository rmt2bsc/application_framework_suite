package com.api.messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.ConfigConstants;
import com.constants.GeneralConst;

//import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Helper class designed to provide generic object binding functionality using
 * JAXB methodologies. In order to use this class, the JAXB context must be
 * setup using the package name which refers to the location of the
 * auto-genrated class files created by the xjc command.
 * <p>
 * <b>Usage:</b>
 * 
 * <pre>
 *    JaxbMessageBinderImpl binder;
 *    // Create JaxbMessageBinderImpl which JAXB context is pointing to the default package
 *    binder = new JaxbMessageBinderImpl();
 *    // or Create JaxbMessageBinderImpl which JAXB context is pointing to a specified package
 *    binder = new JaxbMessageBinderImpl("com.jaxb.binding.classes.location");
 *    
 *    // marshall a data object using some JAXB object
 *    String xml = binder.marshalMessage(<the JAXB object);
 *    
 *    // unmarshal XML to JAXB object
 *    Object obj = binder.unMarshalMessage(xml);
 * </pre>
 * 
 * @author rterrell
 * @deprecated Use JaxbUtil class the XML API of the fw_core project for JAXB
 *             functionality.
 * 
 */
class JaxbMessageBinderImpl implements MessageBinder {
    private static Logger logger = Logger
            .getLogger(JaxbMessageBinderImpl.class);

    private static final String JAXB_SCHEMA_LOCATION = "http://rmt2.net/XMLMessages";

    private static final String JAXB_DEFAULT_PKG = "com.bindings.xml.jaxb";

    // private static final String PROP_PREFIX_MAPPER =
    // "com.sun.xml.bind.namespacePrefixMapper";

    private static final String PROP_FORMAT_OUTPUT = "jaxb.formatted.output";

    private JAXBContext context;

    private String jaxbPkg;

    // /**
    // * Provides a way to create custom namespace prefixes when marshalling
    // JAXB objects to XML documents.
    // * This inner class eliminates the possiblity of the Marshaller creating
    // undesireable auto-generated
    // * namespace prefixes.
    // *
    // * @author Roy Terrell
    // *
    // */
    // private class PreferredMapper extends NamespacePrefixMapper {
    // /**
    // * Returns the prefered prefix based on matching namespace URI. Also,
    // prefixes the root element
    // * with the target namespace which the root element was defined in XSD.
    // *
    // * @param namespaceUri
    // * the namespace URI that is to be used in the marshalled XML document.
    // * @param suggestion
    // *
    // * @param requirePrefix
    // * @return
    // * null if there's no preferred prefix for the namespace URI.
    // * In this case, the system will generate a prefix for you.
    // *
    // * Otherwise the system will try to use the returned prefix,
    // * but generally there's no guarantee if the prefix will be
    // * actually used or not.
    // *
    // * return "" to map this namespace URI to the default namespace.
    // * Again, there's no guarantee that this preference will be
    // * honored.
    // */
    // @Override
    // public String getPreferredPrefix(String namespaceUri, String suggestion,
    // boolean requirePrefix) {
    // if ("http://www.rmt2.net/webservices/schemas".equals(namespaceUri)) {
    // return "tns";
    // }
    // if ("http://www.rmt2.net/webservices/schemas/mime".equals(namespaceUri))
    // {
    // return "mns";
    // }
    // if
    // ("http://www.rmt2.net/webservices/schemas/postal".equals(namespaceUri)) {
    // return "pns";
    // }
    // if
    // ("http://www.rmt2.net/webservices/schemas/generalcodes".equals(namespaceUri))
    // {
    // return "gcns";
    // }
    //
    // return suggestion;
    // }
    // }

    /**
     * Creates a JaxbMessageBinderImpl object whose object binding context is
     * initialized to the default java package where the auto-generated JAXB
     * class files live.
     * 
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    protected JaxbMessageBinderImpl() {
        this.jaxbPkg = null;
        this.setupEnv(JaxbMessageBinderImpl.JAXB_DEFAULT_PKG);
        return;
    }

    /**
     * Creates a JaxbMessageBinderImpl object whose object binding context is
     * initialized to the specified java package where the auto-generated JAXB
     * class files live.
     * 
     * @param jaxbObj
     *            An object that is capable of manipulating XML from within the
     *            JAXP API.
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    protected JaxbMessageBinderImpl(Object jaxbObj) {
        this.jaxbPkg = jaxbObj.getClass().getName();
        this.setupEnv(jaxbObj);
        return;
    }

    /**
     * Creates the JAXB context object needed to bind XML documents and java
     * data objects. The context can be created by the default location where
     * the auto-generated files live or by some other specfied package. If
     * invalid, a system exception is thrown.
     * 
     * @param source
     *            the package name where the auto-generate binding classes live
     *            or null when the default package location is desired.
     * @throws SystemException
     *             general JAXB errors.
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public void setupEnv(Object source) throws SystemException {
        String msg = null;
        String pkg = null;
        if (source == null) {
            msg = "The name of the package that houses the JAXB classes must be supplied in orer to initialize the JAXB environment";
            JaxbMessageBinderImpl.logger.error(msg);
            throw new SystemException(msg);
        }
        if (source instanceof String) {
            pkg = source.toString();
            try {
                this.context = JAXBContext.newInstance(pkg);
            } catch (JAXBException e) {
                throw new SystemException(e);
            }
        }
        else {
            try {
                this.context = JAXBContext.newInstance(source.getClass());
            } catch (JAXBException e) {
                throw new SystemException(e);
            }
        }
        // else {
        // msg =
        // "JAXB environment failed to be configured because the package name must be of type String";
        // JaxbMessageBinderImpl.logger.error(msg);
        // throw new SystemException(msg);
        // }

        // try {
        // this.context = JAXBContext.newInstance(pkg);
        // } catch (JAXBException e) {
        // throw new SystemException(e);
        // }
    }

    /**
     * @return the jaxbPkg
     */
    String getJaxbPkg() {
        return jaxbPkg;
    }

    /**
     * Creates a XML document formatted for readability from an equivalent java
     * object using JAXB data binding techniques.
     * 
     * @param source
     *            the java object that is to be converted.
     * @return String the XML document equivalent to <i>obj</i>.
     * @throws SystemException
     *             general JAXB marshalling errors.
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public String marshalMessage(Object source) throws SystemException {
        return this.marshalMessage(source, GeneralConst.FORMAT_SOAP_XML);
    }

    /**
     * 
     * @param msgObj
     * @param messageId
     * @return
     * @throws SystemException
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public String marshalMessage(Object source, String messageId)
            throws SystemException {
        return this.marshalMessage(source, messageId,
                GeneralConst.FORMAT_SOAP_XML);
    }

    /**
     * Creates a XML document from an equivalent java object that is compatible
     * to JAXB v1.x data binding techniques.
     * 
     * @param source
     *            the java object that is to be converted.
     * @param formatOutput
     *            boolean indicating if XML is to be formatted for readability.
     *            When set to true, the document will be formatted. When set to
     *            false, the document will be presented in raw format.
     * @return String the XML document equivalent to <i>obj</i>.
     * @throws SystemException
     *             general JAXB marshalling errors.
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public String marshalMessage(Object source, boolean formatOutput)
            throws SystemException {
        try {
            Marshaller m = this.context.createMarshaller();
            // NamespacePrefixMapper pm = new PreferredMapper();
            // // Set custom Namespace prefix mapper
            // m.setProperty(JaxbMessageBinderImpl.PROP_PREFIX_MAPPER, pm);
            // Give me pretty output
            m.setProperty(JaxbMessageBinderImpl.PROP_FORMAT_OUTPUT,
                    formatOutput);
            // Supress the XML declaration
            m.setProperty("jaxb.fragment", Boolean.TRUE);

            boolean nsAware = Boolean
                    .getBoolean(ConfigConstants.SOAP_NAMESPACE_AWARE);
            if (nsAware) {
                // Found out through testing that identifying the JAXB schema
                // location has no bearing on the
                // success or failure on adding XML document to the SOAP Body
                // instance. This line of code was
                // put into place for that reason. Will let remain for other
                // applicable situations.
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                        JaxbMessageBinderImpl.JAXB_SCHEMA_LOCATION);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            m.marshal(source, baos);

            String xml = baos.toString();
            JaxbMessageBinderImpl.logger.info(xml);
            return xml;
        } catch (JAXBException e) {
            throw new SystemException(e);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Creates a XML document from an equivalent java object that is compatible
     * to JAXB v2.x data binding techniques.
     * <p>
     * The purpose of this overloaded method is to prevent the marshalling error
     * message, <i>unable to marshal type XXX as an element because it is
     * missing an @XmlRootElement annotation</i> when marshalling JAXB objects
     * where the XJC compiler failed to include the @XmlRootElement annotation
     * in the respective generated class. The JAXBElement wrapper object is used
     * to marshal the JAXB object, which provides the same information as
     * 
     * @XmlRootElement, but in the form of an object, rather than an annotation.
     * 
     * @param source
     * @param messageId
     * @param formatOutput
     * @return
     * @throws SystemException
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public String marshalMessage(Object source, String messageId,
            boolean formatOutput) throws SystemException {
        try {
            Marshaller m = this.context.createMarshaller();
            // Give me pretty output
            m.setProperty(JaxbMessageBinderImpl.PROP_FORMAT_OUTPUT,
                    formatOutput);
            // Supress the XML declaration
            m.setProperty("jaxb.fragment", Boolean.TRUE);

            boolean nsAware = Boolean
                    .getBoolean(ConfigConstants.SOAP_NAMESPACE_AWARE);
            if (nsAware) {
                // Found out through testing that identifying the JAXB schema
                // location has no bearing on the
                // success or failure on adding XML document to the SOAP Body
                // instance. This line of code was
                // put into place for that reason. Will let remain for other
                // applicable situations.
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                        JaxbMessageBinderImpl.JAXB_SCHEMA_LOCATION);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Use the JAXBElement wrapper object, which provide the same
            // information as @XmlRootElement,
            // but in the form of an object, rather than an annotation.
            JAXBElement jaxbObj = new JAXBElement(new QName("", messageId),
                    source.getClass(), source);
            m.marshal(jaxbObj, baos);
            String xml = baos.toString();
            JaxbMessageBinderImpl.logger.info(xml);
            return xml;
        } catch (JAXBException e) {
            throw new SystemException(e);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Generically creates a java object from an equivalent XML documnet using
     * JAXB data binding techniques. The user is responsible for casting the
     * returned java object to the appropriate runtime data type as specified by
     * <i>xmlDoc</i>.
     * 
     * @param xmlDoc
     *            the XML document that is to be converted to a java bean.
     * @return Object an arbitrary object equivalent to the structure of the XML
     *         document that was unmarshalled. The user must apply the
     *         appropriate cast on the generic object in order to access its
     *         data.
     * @throws SystemException
     *             general JAXB marshalling errors.
     * @deprecated Use JaxbUtil class the XML API of the fw_core project for
     *             JAXB functionality.
     */
    public Object unMarshalMessage(String xmlDoc) throws SystemException {
        try {
            Unmarshaller u = this.context.createUnmarshaller();
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    xmlDoc.getBytes());
            Object bindObj = u.unmarshal(bais);
            return bindObj;
        } catch (JAXBException e) {
            throw new SystemException(e);
        }
    }

}
