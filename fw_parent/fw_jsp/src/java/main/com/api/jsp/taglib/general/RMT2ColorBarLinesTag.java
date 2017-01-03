package com.api.jsp.taglib.general;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.api.jsp.taglib.RMT2TagSupportBase;

/**
 * This custom tag alternates the background colors of each HTML table row based
 * on the color values associated with even and odd numbered rows. This custom
 * tag is designed to be used as an enclosed statement to one of the
 * RMT2ForEach* looping custom tags. For example:
 * <p>
 * <p>
 * <blockquote> <<beanlib:LoopRows bean="item" beanClassId="bean.ProjTask"
 * list="list">> <<gen:ColorBarLines evenColor="#CCFFCC" oddColor="#FFFFFF"/>>
 * </blockquote>
 * <p>
 * <p>
 * When using the ColorBarLines tag, the opening
 * <tr>
 * row tag is automatically coded for the user in which the background color
 * specified as one of the tag's attributes is applied. However, the user is
 * responsible for providing a matching end
 * </tr>
 * tag for the ColorBarLines custom tag.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ColorBarLinesTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 2449935393274203938L;

    private String lineNumber = null;

    /** The color designated for even numbered rows */
    protected String evenColor = null;

    /** The color designated for odd numbered rows */
    protected String oddColor = null;

    /*
     * private void setLineNumber(String value) { this.lineNumber = value; }
     */

    /**
     * Sets the background color for even numbered rows
     */
    public void setEvenColor(String value) {
        this.evenColor = value;
    }

    /**
     * Sets the background color for odd numbered rows
     * 
     * @param value
     */
    public void setOddColor(String value) {
        this.oddColor = value;
    }

    /**
     * This method applies the appropriate backcolor to the current HTML table
     * row. The background color is specified by the user when utilizing the
     * ColorBarLines custom tag in the JSP.
     */
    public int doStartTag() throws JspException {
        int row;
        String rowHtml = "";
        try {
            if (this.lineNumber == null || this.lineNumber.length() <= 0) {
                this.lineNumber = (String) pageContext.getAttribute("ROW");
            }
            try {
                row = Integer.valueOf(this.lineNumber).intValue();
            } catch (NumberFormatException e) {
                row = 0;
            }
            int result = row % 2;
            if (result == 0) {
                rowHtml = "<tr bgcolor=\"" + this.evenColor + "\">";
            }
            else {
                rowHtml = "<tr bgcolor=\"" + this.oddColor + "\">";
            }
            this.outputHtml(rowHtml);
            // reinitialize so that the next iteration will not pickup the
            // cached value.
            this.lineNumber = null;
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspException("Class: " + this.className + "  Method: "
                    + this.methodName + "  Message (IOException): "
                    + e.getMessage());
        }
    }

}