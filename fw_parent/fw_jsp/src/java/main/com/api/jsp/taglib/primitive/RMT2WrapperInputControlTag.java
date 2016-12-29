package com.api.jsp.taglib.primitive;

import com.SystemException;
import com.api.jsp.taglib.RMT2AbstractInputControl;

import com.util.RMT2Utility;

/**
 * This class builds HTML input controls using one of the primitive wrapper
 * object types as the data source.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2WrapperInputControlTag extends RMT2AbstractInputControl {
    private static final long serialVersionUID = 7958677997453466597L;

    /**
     * This method builds the options statements of the HTML Select control by
     * cycling through a RMT2DataSourceApi object, which serves as the external
     * data provider, and obtaining and assigning each appropriately mapped data
     * source value to an select option. The values of option.name and
     * option.value will equate.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildSelectControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        Object[] objs;

        // Get array of objects.
        if (RMT2Utility.isArray(this.obj)) {
            objs = (Object[]) this.obj;
        }
        else {
            return "";
        }

        // Build option list
        for (int ndx = 0; ndx < objs.length; ndx++) {
            // Get next element in ArrayList
            displayValue = objs[ndx].toString();
            // Build HTML select control option clause.
            html.append("\n\t<option value=\"");
            html.append(displayValue);
            html.append("\"");
            if (this.selectedValue != null
                    && displayValue.equalsIgnoreCase(this.selectedValue)) {
                html.append(" selected ");
            }
            html.append("> ");
            html.append(displayValue);
        }

        return html.toString();
    }

    /**
     * This method dynamically builds HTML radio button input controls using a
     * primitive wrapper objects as the data source. The radio buttons are
     * grouped together using a HTML table.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildRadioControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        Object[] objs;

        // Get array of objects.
        if (RMT2Utility.isArray(this.obj)) {
            objs = (Object[]) this.obj;
        }
        else {
            return "";
        }

        // Build option list
        for (int ndx = 0; ndx < objs.length; ndx++) {
            // Get next display value in the ArrayList
            displayValue = objs[ndx].toString();
            // Create HTML for radio button and the table row that contains it.
            String temp = this
                    .buildRadioControlHtml(displayValue, displayValue);
            html.append(temp);
        }
        return html.toString();
    }

} // end class
