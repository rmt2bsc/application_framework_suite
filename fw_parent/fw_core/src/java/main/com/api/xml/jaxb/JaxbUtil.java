package com.api.xml.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.api.config.ConfigConstants;

/**
 * Helper class designed to provide generic object binding functionality using
 * JAXB methodologies. In order to use this class, the JAXB context must be
 * setup using the package name which refers to the location of the
 * auto-genrated class files created by the xjc command.
 * <p>
 * <b>Usage:</b>
 * 
 * <pre>
 *    JaxbUtil binder;
 *    // Create JaxbUtil containing in multiple ways.
 *    binder = new JaxbUtilJaxbUtil();
 *    or
 *    binder = new JaxbUtilJaxbUtil("com.jaxb.package.one");
 *    or
 *    binder = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
 *    
 *    // marshall a data object using some JAXB object
 *    String xml = binder.marshalMessage(the JAXB object);
 *    
 *    // unmarshal XML to JAXB object
 *    Object obj = binder.unMarshalMessage(xml);
 * </pre>
 * 
 * @author rterrell
 * 
 */
public class JaxbUtil {
    private static Logger LOGGER = Logger.getLogger(JaxbUtil.class);

    private static final String PROP_FORMAT_OUTPUT = "jaxb.formatted.output";

    /** Indicates whether or not marshalled XML should use pretty formatting */
    private static final boolean FORMAT_XML = true;

    // private static Map<String, JAXBContext> contexts = null;

    // private static JaxbUtil util = null;

    private JAXBContext context;

    // private String targetContext;

    /**
     * Creates a JaxbUtil object using the default context,
     * {@link ConfigConstants#JAXB_CONTEXNAME_DEFAULT}
     */
    public JaxbUtil() {
        this(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        return;
    }

    /**
     * Creates a JaxbUtil object using the specified context.
     */
    public JaxbUtil(String jaxbPackage) {
        try {
            this.createContext(jaxbPackage);
        } catch (JaxbUtilException e) {
            throw new JaxbUtilException("Unable to create JAXB Utility object",
                    e);
        }
        return;
    }

    private void createContext(String jaxbPackage) {
        try {
            this.context = JAXBContext.newInstance(jaxbPackage);
        } catch (JAXBException e) {
            throw new JaxbUtilException(
                    "Error occurred creating JAXB Context for package, "
                            + jaxbPackage, e);
        } catch (NullPointerException e) {
            throw new JaxbUtilException(
                    "Error: Input JAXB Package name must not be null when initializing JAXBUtil object");
        }
    }

    // /**
    // * Creates a JAXB context for package, <i>jaxbPackage</i> and returns an
    // * instance of JaxbUtil.
    // * <p>
    // * The JAXB context are is where the auto-generated classes live. For
    // * example, <i>com.bindings.xml.jaxb</i> could point to where the various
    // * XML message binding objects live. If invalid, a JaxbUtilException is
    // * thrown.
    // *
    // * @param jaxbPackage
    // * the java package containing the set of JAXB classes to build
    // * the context.
    // * @return {@link JaxbUtil}
    // * @throws JaxbUtilException
    // * <i>jaxbPackage</i> is null or a JAXB context could not be
    // * created from <i>jaxbPackage</i>.
    // */
    // public static final JaxbUtil getJaxbUtility(String jaxbPackage) {
    // if (JaxbUtil.util == null) {
    // JaxbUtil.util = new JaxbUtil();
    // }
    // try {
    // if (JaxbUtil.contexts.get(jaxbPackage) == null) {
    // JAXBContext ctx = JAXBContext.newInstance(jaxbPackage);
    // JaxbUtil.contexts.put(jaxbPackage, ctx);
    // }
    // } catch (JAXBException e) {
    // throw new JaxbUtilException(
    // "Error occurred creating JAXB Context for package, "
    // + jaxbPackage, e);
    // } catch (NullPointerException e) {
    // throw new JaxbUtilException(
    // "Input JAXB Package name must not be null when initializing JAXBUtil object");
    // }
    // return JaxbUtil.util;
    // }

    /**
     * Creates a XML document formatted for readability from an equivalent java
     * object using JAXB data binding techniques.
     * 
     * @param source
     *            the java object that is to be converted.
     * @return String the XML document equivalent to <i>obj</i>.
     * @throws RuntimeException
     *             general JAXB marshalling errors.
     */
    public String marshalMessage(Object source) {
        return this.marshalMessage(source, FORMAT_XML);
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
     * @throws RuntimeException
     *             general JAXB marshalling errors.
     */
    public String marshalMessage(Object source, boolean formatOutput) {
        if (this.context == null) {
            throw new RuntimeException(
                    "Unable to marshal object.  JAXB context is invalid or null");
        }
        try {
            Marshaller m = context.createMarshaller();
            // Give me pretty output
            m.setProperty(JaxbUtil.PROP_FORMAT_OUTPUT, formatOutput);
            // Supress the XML declaration
            m.setProperty("jaxb.fragment", Boolean.TRUE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            m.marshal(source, baos);

            String xml = baos.toString();
            JaxbUtil.LOGGER.debug(xml);
            return xml;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a XML document formatted for readability from an equivalent java
     * object using JAXB data binding techniques.
     * 
     * @param source
     *            the java object that is to be converted.
     * @return String the XML document equivalent to <i>obj</i>.
     * @throws RuntimeException
     *             general JAXB marshalling errors.
     */
    public String marshalJsonMessage(Object source) {
        return this.marshalMessage(source, FORMAT_XML);
    }

    /**
     * Generically creates a java object from an equivalent XML documnet using
     * JAXB data binding techniques.
     * <p>
     * The user is responsible for casting the returned java object to the
     * appropriate runtime data type as specified by <i>xmlDoc</i>.
     * 
     * @param xmlDoc
     *            the XML document that is to be converted to a java bean.
     * @return Object an arbitrary object equivalent to the structure of the XML
     *         document that was unmarshalled. The user must apply the
     *         appropriate cast on the generic object in order to access its
     *         data.
     * @throws RuntimeException
     *             general JAXB marshalling errors.
     */
    public Object unMarshalMessage(String xmlDoc) {
        if (this.context == null) {
            throw new RuntimeException(
                    "Unable to unmarshal object.  JAXB context is invalid or null");
        }
        try {
            Unmarshaller u = context.createUnmarshaller();
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    xmlDoc.getBytes());
            Object bindObj = u.unmarshal(bais);
            return bindObj;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // /**
    // * Return the name of the JAXB context that is current for this instance
    // *
    // * @return the targetContext
    // */
    // public String getTargetContext() {
    // return targetContext;
    // }

    // /**
    // * Sets the JAXB context to work with and makes either the request or
    // * response context current.
    // *
    // * @param targetContext
    // * the package containg the JAXB binding objects. Valid values
    // * are {@link JaxbUtil#JAXB_REQUEST_PKG} or
    // * {@link JaxbUtil#JAXB_RESPONSE_PKG}
    // */
    // public void setTargetContext(String targetContext) {
    // this.targetContext = targetContext;
    // try {
    // this.context = JaxbUtil.contexts.get(targetContext);
    // } catch (NullPointerException e) {
    // throw new JaxbUtilException(
    // "Input JAXB Package name must not be null when fetching JAXB context object from Map");
    // }
    // }

}
