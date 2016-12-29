package com.api.jsp.taglib.datasource;

import javax.servlet.jsp.JspException;

import com.NotFoundException;
import com.SystemException;
import com.api.persistence.DatabaseException;

/**
 * Creates a HTML DropDown List (Select) using external data sources that
 * provide the master list for the drop down and the data that serves as a
 * target for the lookup.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2SelectLookupTag extends RMT2AbstractLookupTag {
    private static final long serialVersionUID = -5749867177880072114L;

    /*
     * Sets the size property @param value
     */
    public void setSize(String value) {
        try {
            this.size = new Integer(value).toString();
        } catch (NumberFormatException e) {
            this.size = "1";
        }
    }

    /**
     * The driver for building the input control using look-up capabilities from
     * a database implementation of the DsoApi interface. Obtains and outputs
     * the value from the requesting custom tag.
     * 
     * @return IterationTag.SKIP_BODY
     * @throws JspException
     *             I/O errors, System errors, Database errors, or references
     *             made to properties that do not exist.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        return super.doStartTag();
    }

    /**
     * This method includes logic to ensure this tag is processed as a Select
     * control.
     * 
     * @return The HTML in the format of a String.
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     * @throws JspException
     */
    protected String buildInputControl() throws DatabaseException,
            NotFoundException, SystemException, JspException {
        this.type = "select";
        return super.buildInputControl();
    }

    /**
     * Creates the Select control by using the lookup datasource and determines
     * which option is selected, if any.
     * 
     * @return HTML String
     * @throws NotFoundException
     *             if the master code name, lookup display name, or lookup code
     *             name references do not exist.
     * @throws DatabaseException
     *             Database access errors
     * @throws SystemException
     *             if a problem occured obtaining the master code name, lookup
     *             display name, or lookup code name values.
     */
    protected String buildSelectControlData() throws SystemException {
        this.methodName = "setupControl";
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;
        String masterCodeValue = null;

        try {
            masterCodeValue = masterDso.getColumnValue(this.masterCodeName);
            while (lookupDso.getRs().next()) {
                displayValue = lookupDso.getColumnValue(this.lookupDisplayName);
                codeValue = lookupDso.getColumnValue(this.lookupCodeName);
                html.append("<option value=");
                html.append(codeValue);
                if (masterCodeValue != null
                        && codeValue.equalsIgnoreCase(masterCodeValue)) {
                    html.append(" selected ");
                }
                html.append("> ");
                html.append(displayValue);
            }
            html.append("</select>");
            return html.toString();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * No Action.
     * 
     * @return null
     */
    protected String buildRadioControlData() throws SystemException {
        return null;
    }

}