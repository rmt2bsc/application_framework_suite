package com.api.jsp.taglib.datasource;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.SystemException;

import com.api.DaoApi;
import com.api.jsp.taglib.RMT2AbstractInputControl;
import com.api.jsp.taglib.bean.dao.BeanDao;

/**
 * This class builds HTML input controls from a RMT2DataSourceApi data source.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2DsoInputControlTag extends RMT2AbstractInputControl {
    private static final long serialVersionUID = 3291359912231644283L;

    /**
     * This is the data source api used to obtain date from an external
     * provider.
     */
    protected DaoApi dso = null;

    /**
     * The driver for building an input control from a database implementation
     * of the DsoApi interface. Obtains and outputs the value from the
     * requesting custom tag.
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
     * This method builds the options statements of the HTML Select control by
     * cycling through a RMT2DataSourceApi object, which serves as the external
     * data provider, and obtaining and assigning each appropriately mapped data
     * souce value to an select option.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildSelectControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;

        // Get DAO object from JSP page which contains the data needed to build
        // the HTML Select control
        // Check data type of incoming data object and cast accordingly.
        if (this.obj == null) {
            throw new SystemException(
                    "HTML Select control must have a valid DaoApi");
        }
        if (this.obj instanceof DaoApi) {
            this.dso = (DaoApi) this.obj;
        }

        try {
            while (this.dso.nextRow()) {
                displayValue = this.dso.getColumnValue(this.displayProperty)
                        .trim();
                codeValue = this.dso.getColumnValue(this.codeProperty).trim();
                html.append("\n\t<option value=\"");
                html.append(codeValue);
                html.append("\"");
                if (this.selectedValue != null
                        && codeValue.equalsIgnoreCase(this.selectedValue)) {
                    html.append(" selected ");
                }
                html.append("> ");
                html.append(displayValue);
            }
            return html.toString();
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * This method dynamically builds HTML radio button input controls using a
     * Java Bean object as the data source. The radio buttons are grouped
     * together using a HTML table.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildRadioControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;
        try {
            // Begin building radio buttons
            while (this.dso.nextRow()) {
                displayValue = this.dso.getColumnValue(this.displayProperty)
                        .trim();
                codeValue = this.dso.getColumnValue(this.codeProperty).trim();
                // Create HTML for radio button and the table row that contains
                // it.
                String temp = this.buildRadioControlHtml(codeValue,
                        displayValue);
                html.append(temp);
            }
            return html.toString();
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

} // end class
