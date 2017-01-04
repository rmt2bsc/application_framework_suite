package com.api.xml;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * An entity bean that is used to track attribute name, type, and value data for
 * processing XML documents via SAX.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2SaxAttributesBean extends RMT2BaseBean {
    private static final long serialVersionUID = -8223180554591433038L;

    private String attName;

    private String attType;

    private String attValue;

    /**
     * Default Constructor.
     * 
     * @throws SystemException
     */
    public RMT2SaxAttributesBean() throws SystemException {
        super();
    }

    /**
     * Performs adtitional object intitialization.
     */
    public void initBean() {
        return;
    }

    /**
     * Gets the attribute name.
     * 
     * @returnString
     */
    public String getAttName() {
        return this.attName;
    }

    /**
     * Sets the attribute name.
     * 
     * @param value
     *            String
     */
    public void setAttName(String value) {
        this.attName = value;
    }

    /**
     * Gets the attribute Type
     * 
     * @return String
     */
    public String getAttType() {
        return this.attType;
    }

    /**
     * Sets the attribute Type
     * 
     * @param value
     *            String
     */
    public void setAttType(String value) {
        this.attType = value;
    }

    /**
     * Gets the attribute Value
     * 
     * @return String
     */
    public String getAttValue() {
        return this.attValue;
    }

    /**
     * Sets the attribute value.
     * 
     * @param value
     *            String
     */
    public void setAttValue(String value) {
        this.attValue = value;
    }

}
