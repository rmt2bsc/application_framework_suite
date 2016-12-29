package com.api.jsp.taglib.general;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.SystemException;
import com.api.jsp.taglib.RMT2AbstractInputControl;
import com.constants.RMT2TagConst;

/**
 * This custom tag builds a HTML select control tha contains a list of
 * relational operators.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ConditionalOperatorTag extends RMT2AbstractInputControl {
    private static final long serialVersionUID = 1L;

    /**
     * This method serves as the entry point into this custom tag
     */
    public int doStartTag() throws JspException {
        try {
            this.outputHtml(buildInputControl());
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspException("Class: " + this.className + "  Method: "
                    + this.methodName + "  Message (IOException): "
                    + e.getMessage());
        } catch (SystemException e) {
            throw new JspException("Class: " + this.className + "  Method: "
                    + this.methodName + "  Message (SystemException): "
                    + e.getMessage());
        }
    }

    /**
     * This method invokes the process that builds the actual HTML Select
     * control of relational operators.
     * 
     * @return HTML code as a String
     * @throws SystemException
     */
    protected String buildInputControl() throws SystemException {
        StringBuffer html = new StringBuffer(200);
        return this.buildSelectControl(html);
    }

    /**
     * Builds the HTML Select control that will contain a list of relational
     * operators using the values of the member variables.
     * 
     * @param html
     *            variable used as a work area for building the control.
     * @returnThe HTML in the format of a String.
     * @throws SystemException
     */
    protected String buildSelectControl(StringBuffer html)
            throws SystemException {

        if (this.name != null) {
            html.append("<select name=\"" + this.name + "\"");
        }
        else {
            html.append("<select ");
        }
        if (this.id != null) {
            html.append(" id=\"");
            html.append(this.id);
            if (this.uniqueName) {
                String row = (String) pageContext
                        .getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
                                : RMT2TagConst.ROW_ID_REGULAR);
                if (row != null && row.length() > 0) {
                    html.append(row);
                }
            }
            html.append("\"");
        }
        if (this.size != null) {
            html.append(" size=");
            html.append(this.size);
        }
        if (this.multiple) {
            html.append(" multiple");
        }

        // Add style attribute
        if (this.style != null) {
            html.append(" style=\"");
            html.append(this.style);
            html.append("\"");
        }

        // Add tabIndex attribute
        if (this.tabIndex != null) {
            html.append(" tabindex=\"");
            html.append(this.tabIndex);
            html.append("\"");
        }

        // Add onFocus attribute
        if (this.onFocus != null) {
            html.append(" onFocus=\"");
            html.append(this.onFocus);
            html.append("\"");
        }

        // Add onBlur attribute
        if (this.onBlur != null) {
            html.append(" onBlur=\"");
            html.append(this.onBlur);
            html.append("\"");
        }

        // Add onSelect attribute
        if (this.onSelect != null) {
            html.append(" onSelect=\"");
            html.append(this.onSelect);
            html.append("\"");
        }

        // Add onChange attribute
        if (this.onChange != null) {
            html.append(" onChange=\"");
            html.append(this.onChange);
            html.append("\"");
        }
        // end select opening tag
        html.append(">  ");

        // Build select list
        html.append(this.buildSelectControlData());
        html.append("</select>");
        return html.toString();
    }

    @Override
    protected String buildRadioControlData() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String buildSelectControlData() throws SystemException {
        StringBuffer html = new StringBuffer();
        // Do first one.
        html.append("\n\t<option value=\"\">");

        // Do =
        html.append("\n\t<option value=\"=\" ");
        if (this.selectedValue != null && this.selectedValue.equals("=")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append("=");

        // Do <>
        html.append("\n\t<option value=\"<>\" ");
        if (this.selectedValue != null && this.selectedValue.equals("<>")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append("<>");

        // Do >
        html.append("\n\t<option value=\">\" ");
        if (this.selectedValue != null && this.selectedValue.equals(">")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append(">");

        // Do >=
        html.append("\n\t<option value=\">=\" ");
        if (this.selectedValue != null && this.selectedValue.equals(">=")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append(">=");

        // Do <
        html.append("\n\t<option value=\"<\" ");
        if (this.selectedValue != null && this.selectedValue.equals("<")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append("<");

        // Do <=
        html.append("\n\t<option value=\"<=\" ");
        if (this.selectedValue != null && this.selectedValue.equals("<=")) {
            html.append(" selected ");
        }
        html.append("> ");
        html.append("<=");
        return html.toString();
    }

} // end class
