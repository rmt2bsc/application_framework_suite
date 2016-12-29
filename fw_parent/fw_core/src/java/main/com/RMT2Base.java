package com;

import java.util.Hashtable;

import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.xml.adapters.XmlAdapterFactory;
import com.api.xml.adapters.XmlBeanAdapter;

/**
 * This abstract class serves as the ancestor for all other classes in the
 * system.
 * 
 * @author roy.terrell
 * 
 */
public abstract class RMT2Base {

    private static final String ENV = AppPropertyPool
            .getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);

    /**
     * Message indicating that the logger has been initialized.
     */
    protected static final String LOGGER_INIT_MSG = "Logger has been initialized";

    /** Single messabe literal */
    public static final String SINGLE_MSG = "msg";

    /** Multi Message literal */
    public static final String MULTI_MSG = "messages";

    /** Success code */
    public static final int SUCCESS = 1;

    /** Failure code */
    public static final int FAILURE = -1;

    /** Messages hash */
    protected Hashtable<String, String> messages;

    /** Used to contain a single message */
    protected String msg;

    /**
     * Constructs a RMT2Base object
     * 
     */
    public RMT2Base() {
        this.init();
        return;
    }

    /**
     * Initializes the messages list.
     * 
     */
    public void init() {
        this.messages = new Hashtable<String, String>();
        return;
    }

    /**
     * Obtains the environment value.
     * 
     * @return String
     */
    public static final String getEnvironment() {
        return RMT2Base.ENV;
    }

    /**
     * Gets the XML representation of this object as a String.
     * 
     * @return String XML document.
     */
    public String toXml() {
        XmlBeanAdapter adpt = XmlAdapterFactory.createNativeAdapter();
        try {
            String results = adpt.beanToXml(this);
            return results;
        } catch (SystemException e) {
            return null;
        }
    }

}