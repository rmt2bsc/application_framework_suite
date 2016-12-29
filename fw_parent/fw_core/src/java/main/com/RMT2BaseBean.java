package com;

import java.io.Serializable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Date;

import com.RMT2Base;
import com.api.xml.adapters.XmlAdapterFactory;
import com.api.xml.adapters.XmlBeanAdapter;

import com.util.RMT2Date;

/**
 * Abstract bean class which is generally used as the ancestor for all beans.
 * 
 * @author appdev
 * @deprecated This class is no longer used.
 * 
 */
public abstract class RMT2BaseBean extends RMT2Base implements Serializable {

    private static final long serialVersionUID = -6739003215847406841L;

    private String serialPath;

    private String fileName;

    private ObjectOutput outObj;

    private ObjectInput inObj;

    private FileOutputStream outStream;

    private FileInputStream inStream;

    private String jspOrigin;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    public RMT2BaseBean() throws SystemException {
        super();
        this.initBean();

    }

    /**
     * Abstract method to perform additional class initialization.
     * 
     * @throws SystemException
     */
    public abstract void initBean() throws SystemException;

    /**
     * Gets the file path where object serialization output will be stored.
     * 
     * @return file path.
     */
    public String getSerialPath() {
        return this.serialPath;
    }

    /**
     * Sets the file path where object serialization will occur.
     * 
     * @param value
     */
    public void setSerialPath(String value) {
        this.serialPath = value;
    }

    /**
     * Gets the filename where object serialization output is stored.
     * 
     * @return Filename
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the filename where object serialzation output is stored.
     * 
     * @param value
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Performs the actual serialization of this object.
     * 
     */
    public void serialize() {
        try {
            this.outStream = new FileOutputStream(this.fileName);
            this.outObj = new ObjectOutputStream(this.outStream);
            this.outObj.writeObject(this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Performs the deserialization of this object.
     * 
     * @return Serialized object as {@link RMT2BaseBean}.
     */
    public RMT2BaseBean deserialize() {
        try {
            this.inStream = new FileInputStream(this.fileName);
            this.inObj = new ObjectInputStream(this.inStream);
            return (RMT2BaseBean) this.inObj.readObject();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Displays "date" in the format of "format".
     * 
     * @param date
     *            java.util.Date type
     * @param format
     *            One of man java date formats
     * @return String representation of date. Returns an empty String if date
     *         equal null or format equals null, or the length of format is less
     *         than or equal to zero.
     */
    public String displayDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null) {
            return "";
        }
        if (format.length() <= 0) {
            return "";
        }

        try {
            return RMT2Date.formatDate(date, format);
        } catch (SystemException e) {
            return "";
        }
    }

    /**
     * Determines where this bean was instatiated in terms of the client.
     * 
     * @return name of a client resource such as the JSP page or special tag
     *         name..
     */
    public String getJspOrigin() {
        return this.jspOrigin;
    }

    /**
     * Sets the jspOrgin variable.
     * 
     * @param value
     */
    public void setJspOrigin(String value) {
        this.jspOrigin = value;
    }

    // /**
    // * Gets the XML representation of this object as a String.
    // *
    // * @return String XML document.
    // */
    // public String toXml() {
    // XmlBeanAdapter adpt = XmlAdapterFactory.createNativeAdapter();
    // try {
    // String results = adpt.beanToXml(this);
    // return results;
    // }
    // catch (SystemException e) {
    // return null;
    // }
    // }
}