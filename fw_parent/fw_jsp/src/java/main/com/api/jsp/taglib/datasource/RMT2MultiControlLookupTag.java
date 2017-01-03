package com.api.jsp.taglib.datasource;

import javax.servlet.jsp.JspException;

import com.NotFoundException;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.DataSourceConst;

/**
 * Provides the option of creating a HTML lookup controleither as a Text field,
 * DropDown List (Select), or a group of radio buttons using external data
 * sources that provide the master list for the drop down and the data that
 * serves as a target for the lookup.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2MultiControlLookupTag extends RMT2AbstractLookupTag {
    private static final long serialVersionUID = 4038072188056879699L;

    /**
     * This indicates the type of control to create.
     * <ul>
     * <li>1 = Non editable text</li>
     * <li>2 = Input Text control</li>
     * <li>3 = Select control (DropDown List)</li>
     * <li>4 = Radio Button Group</li>
     * </ul>
     */
    protected int typeInt = 0;

    /**
     * Sets the type of control to create.
     * 
     * @param value
     */
    public void setType(String value) {
        this.type = value;
        this.typeInt = new Integer(value).intValue();
    }

    /**
     * The driver for building text, select or radio input controls based on
     * look up capabilities from a database implementation of the DsoApi
     * interface. Obtains and outputs the value from the requesting custom tag.
     * <ul>
     * <li>Initializes <i>this.obj</i> property to null.</li>
     * <li>Calls ancesotr doStartTag method to build input contril.</li>
     * </ul>
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
     * This method determines the type of control that is to be created and
     * invokes the appropriate method for the creation task.
     * 
     * @return HTML script as a String.
     * @throws NotFoundException
     *             if the master code name, lookup display name, or lookup code
     *             name references do not exist.
     * @throws DatabaseException
     *             Database access errors
     * @throws SystemException
     *             if a problem occured obtaining the master code name, lookup
     *             display name, or lookup code name values.
     * @throws JspException
     */
    protected String buildInputControl() throws DatabaseException,
            NotFoundException, SystemException, JspException {
        String html = null;
        boolean invokeAncestor = false;

        switch (this.typeInt) {
            case DataSourceConst.LOOKUPTYPE_TEXT:
            case DataSourceConst.LOOKUPTYPE_INPUT_TEXT:
                html = this.buildTextControl();
                break;

            case DataSourceConst.LOOKUPTYPE_DROPDOWN:
                this.type = "select";
                invokeAncestor = true;
                break;

            case DataSourceConst.LOOKUPTYPE_RADIO:
                this.type = "radio";
                invokeAncestor = true;
                break;

            default:
                html = "";
        }

        // Select and Radio controls require ancestor script to be executed.
        if (invokeAncestor) {
            html = super.buildInputControl();
        }
        return html;
    }

    /**
     * This method creates a non-editable or HTML input text control using the
     * lookup and target data sources. If a matching value is found for the text
     * control, the text will appear automatically populated with that value.
     * 
     * @return HTML as a String
     * @throws NotFoundException
     *             if the master code name, lookup display name, or lookup code
     *             name references do not exist.
     * @throws DatabaseException
     *             Database access errors
     * @throws SystemException
     *             if a problem occured obtaining the master code name, lookup
     *             display name, or lookup code name values.
     */
    protected String buildTextControl() throws NotFoundException,
            DatabaseException, SystemException {
        String displayValue = null;
        String codeValue = null;
        String masterDsoCodeValue = null;
        StringBuffer html = new StringBuffer(100);

        try {
            if (masterDso != null && this.masterCodeName != null) {
                masterDsoCodeValue = masterDso
                        .getColumnValue(this.masterCodeName);
            }
            if (masterDso == null && this.masterCodeName == null
                    && this.masterCodeValue != null) {
                masterDsoCodeValue = this.masterCodeValue;
            }
            while (lookupDso.nextRow()) {
                codeValue = lookupDso.getColumnValue(this.lookupCodeName)
                        .trim();
                if (masterDsoCodeValue.equalsIgnoreCase(codeValue)) {
                    displayValue = lookupDso.getColumnValue(
                            this.lookupDisplayName).trim();
                }
            }

            if (displayValue == null) {
                displayValue = "&nbsp;";
            }
            if (this.typeInt == DataSourceConst.LOOKUPTYPE_TEXT) {
                return displayValue;
            }
            if (this.typeInt == DataSourceConst.LOOKUPTYPE_INPUT_TEXT) {
                html.append("<input");
                if (this.name != null) {
                    html.append(" name=");
                    html.append("\"" + this.name + "\"");
                }
                html.append(" type=text ");
                if (this.size != null) {
                    html.append(" size=");
                    html.append("\"" + this.size + "\"");
                }
                if (this.maxLength != null) {
                    html.append(" maxlength=");
                    html.append("\"" + this.maxLength + "\"");
                }
                if (this.tabIndex != null) {
                    html.append(" tabindex=\"");
                    html.append(this.tabIndex);
                    html.append("\"");
                }
                html.append(" value=");
                html.append("\"" + displayValue + "\"");
                html.append(">");
                return html.toString();
            } // end while

            return "&nbsp;";
        } // end try
        catch (NullPointerException e) {
            throw new SystemException("Something was null");
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * This method creates the list of options for s Select input control using
     * the lookup and target data sources. If a matching value is found for the
     * select control, that value will appear selected in the dropdown control.
     * Otherwise, the control will be properly populated with the lookup data.
     * 
     * @return HTML as a String
     * @throws SystemException
     *             if a problem occured obtaining the master code name, lookup
     *             display name, or lookup code name values.
     */
    protected String buildSelectControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;
        String masterDsoCodeValue = null;

        try {
            if (masterDso != null && this.masterCodeName != null) {
                masterDsoCodeValue = masterDso
                        .getColumnValue(this.masterCodeName);
            }
            if (masterDso == null && this.masterCodeName == null
                    && this.masterCodeValue != null) {
                masterDsoCodeValue = this.masterCodeValue;
            }
            html.append("\n\t<option value=\"\">-- Select One --");
            while (lookupDso.nextRow()) {
                displayValue = lookupDso.getColumnValue(this.lookupDisplayName)
                        .trim();
                codeValue = lookupDso.getColumnValue(this.lookupCodeName)
                        .trim();
                html.append("\n\t<option value=");
                html.append(codeValue);
                if (masterDsoCodeValue != null
                        && codeValue.equalsIgnoreCase(masterDsoCodeValue)) {
                    html.append(" selected ");
                }
                html.append("> ");
                html.append(displayValue);
            } // end while
            html.append("</select>");
            return html.toString();
        } // end try
        catch (NullPointerException e) {
            throw new SystemException(
                    "Something was null in buildSelectControlData");
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * This method creates a Radio button group using the lookup and target data
     * sources. If the target code value matches the value of one of the radio
     * button, then that radio button will appear selected. Otherwise, all radio
     * buttons will be created unselected.
     * 
     * @return HTML as a String
     * @throws SystemException
     *             if a problem occured obtaining the master code name, lookup
     *             display name, or lookup code name values.
     */
    protected String buildRadioControlData() throws SystemException {

        this.methodName = "setupRadioButton";
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;
        String masterDsoCodeValue = null;

        try {
            if (masterDso != null && this.masterCodeName != null) {
                masterDsoCodeValue = masterDso
                        .getColumnValue(this.masterCodeName);
            }
            if (masterDso == null && this.masterCodeName == null
                    && this.masterCodeValue != null) {
                masterDsoCodeValue = this.masterCodeValue;
            }
            while (lookupDso.nextRow()) {
                displayValue = lookupDso.getColumnValue(this.lookupDisplayName)
                        .trim();
                codeValue = lookupDso.getColumnValue(this.lookupCodeName)
                        .trim();
                this.selectedValue = masterDsoCodeValue;
                String rowHtml = this.buildRadioControlHtml(codeValue,
                        displayValue);
                html.append(rowHtml);
            } // end while

            // Return rows of radio buttons to caller
            return html.toString();
        } // end try
        catch (NullPointerException e) {
            throw new SystemException("Something was null");
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}