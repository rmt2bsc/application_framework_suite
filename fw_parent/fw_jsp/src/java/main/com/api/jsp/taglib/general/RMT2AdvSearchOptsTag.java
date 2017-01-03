package com.api.jsp.taglib.general;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import javax.servlet.jsp.JspException;

import com.RMT2Exception;
import com.SystemException;
import com.api.jsp.action.AbstractActionHandler;
import com.api.jsp.taglib.PageVariableHelper;
import com.api.jsp.taglib.RMT2TagSupportBase;

/**
 * This custom tag creates a group of radio buttons to provide advanced search
 * options for HTML input controls representing String values. The following
 * radio buttons are created:<br>
 * <ol>
 * <li>Begins With (default option)</li>
 * <li>Ends With</li>
 * <li>Contains</li>
 * <li>Exact Match</li>
 * </ol>
 * <br>
 * 
 * @author roy.terrell
 * 
 */
public class RMT2AdvSearchOptsTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 2449935393274203938L;

    private static Logger logger = Logger.getLogger(RMT2AdvSearchOptsTag.class);

    // private static final String FIELD_NAME_SUFFIX = "_ADVSRCHOPTS";

    private static String CONTROL_TEXT[] = { "Begins With", "Ends With",
            "Contains", "Exact Match" };

    private static String CONTROL_VALUE[] = { "begin", "end", "contain",
            "exact" };

    /**
     * The name of the HTML input control to link to this widget. This value
     * will also be used to compute the name of the radio buttons contained in
     * the widget. The radio button name format will be: <field_name> +
     * "_ADVSRCHOPTS". This property is required.
     */
    protected String fieldName = null;

    /**
     * An integer indicating the total number of rows that will be used to
     * display all radio buttons. The default is one row.
     */
    protected String rows = null;

    /**
     * An integer indicating the total number of colsumnss that will be used to
     * display all radio buttons. The default is four columns.
     */
    protected String cols = null;

    /**
     * An integer value representing the width of the widget. This value will be
     * used as a measurement of percentages.
     */
    protected String width = null;

    /**
     * An integer value representing the height of the widget. This value will
     * be used as a measurement of pixels.
     */
    // protected String height = null;
    /**
     * Instructs if the radio button controls are position to the left or right
     * of their respective text descriptions. Valid values are "right" and
     * "left". The dafault is "left".
     */
    // protected String controlAlign = null;
    /**
     * Determines if a border is highlight around the group of radio buttons.
     * Valid values are true and false where true is default value.
     */
    protected String border = null;

    /**
     * The value used to determine if control is checked.
     */
    protected String selectedValue = null;

    private boolean drawBorder;

    private String message;

    /**
     * @param fieldName
     *            the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @param rows
     *            the rows to set
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
     * @param cols
     *            the cols to set
     */
    public void setCols(String cols) {
        this.cols = cols;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    // /**
    // * @param height
    // * the height to set
    // */
    // public void setHeight(String height) {
    // this.height = height;
    // }

    // /**
    // * @param controlAlign the controlAlign to set
    // */
    // public void setControlAlign(String controlAlign) {
    // this.controlAlign = controlAlign;
    // }

    /**
     * @param drawBorder
     *            the drawBorder to set
     */
    public void setBorder(String border) {
        this.border = border;
    }

    /**
     * Set the value that is compared to the control's value in order to
     * determine if that control is to be checked.
     * 
     * @param value
     *            Can be a literal or VEL value.
     */
    public void setselectedValue(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.selectedValue = (String) helper.getValue(this.pageContext,
                    value, null, null);
        } catch (SystemException e) {
            this.selectedValue = value;
        }
    }

    /**
     * This method completely overrides ancestor in order to provide custom
     * logic to build thr four HTML radio button controls that are linked to a
     * particular input field.
     */
    public int doStartTag() throws JspException {
        // if (this.controlAlign == null) {
        // this.controlAlign = "left";
        // }
        this.drawBorder = (this.border != null
                && this.border.equalsIgnoreCase("true") ? true : false);
        try {
            String widgetName = this.computeWidgetName();
            String html = this.buildControl(widgetName);
            this.outputHtml(html);
        } catch (IOException e) {
            throw new JspException("IOException: " + e.getMessage());
        }

        return 1;
    }

    /**
     * Computes the name that will be assigned to each radio button created.
     * 
     * @return String The name of the control.
     * @throws JspException
     */
    private String computeWidgetName() throws JspException {
        if (this.fieldName == null) {
            this.message = "Unable to link HTML input control name to widget.  Field name is required";
            throw new JspException(this.message);
        }
        return this.fieldName + AbstractActionHandler.FIELD_NAME_SUFFIX;
    }

    /**
     * Builds the advanced search options widget.
     * 
     * @returnThe HTML in the format of a String.
     * @throws SystemException
     * @throws JspException
     */
    protected String buildControl(String widgetName) throws JspException {
        StringBuffer html = new StringBuffer();

        // Create an outer table with border, if requested.
        if (this.drawBorder) {
            html.append("<table ");
            if (this.width != null) {
                html.append("width=\"" + this.width + "%\"");
            }
            html.append(" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n");
            html.append("  <tr>\n");
            html.append("     <td>\n");
        }
        // Create table to contain the actual readio buttons.
        html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");

        // Gather rows of radio buttons
        String dataHtml = null;
        try {
            dataHtml = this.buildRadioButtons(widgetName);
        } catch (RMT2Exception e) {
            throw new JspException(e);
        }
        html.append(dataHtml);

        // clean up the inner table that houses the radio buttons.
        html.append("</table>\n");

        // clean up the outer table if applicable
        if (this.drawBorder) {
            html.append("</td>\n");
            html.append("</tr>\n");
            html.append("</table>\n");
        }
        return html.toString();
    }

    /**
     * Arranges the group of HTML radio buttons based on the user's
     * specifications.
     * 
     * @return String HTML rows and columns only.
     * @throws RMT2Exception
     */
    private String buildRadioButtons(String radioName) throws RMT2Exception {
        int rows = 0;
        int cols = 0;
        try {
            rows = Integer.parseInt(this.rows);
            cols = Integer.parseInt(this.cols);
        } catch (NumberFormatException e) {
            this.message = "Advanced Search Options widget will default to a formoat of 1 row and 4 columns";
            logger.log(Level.INFO, message);
            rows = 1;
            cols = 4;
        }

        StringBuffer html = new StringBuffer();
        for (int rowNdx = 0; rowNdx < rows; rowNdx++) {
            html.append("<tr>\n");
            for (int colNdx = 0; colNdx < cols; colNdx++) {
                int actualCntlNdx = (rowNdx * cols) + colNdx;
                String value = RMT2AdvSearchOptsTag.CONTROL_VALUE[actualCntlNdx];
                String displayText = RMT2AdvSearchOptsTag.CONTROL_TEXT[actualCntlNdx];
                html.append("   <td align=\"left\" valign=\"top\">\n");
                html.append("      <p>\n");
                html.append("         <font size=\"2\">\n");

                // begin building the actual radio button
                html.append("\n<input type=\"radio\" name=\"" + radioName
                        + "\"");
                html.append(" value=\"");
                html.append(value);
                html.append("\"");

                if (this.selectedValue != null) {
                    if (value.equalsIgnoreCase(this.selectedValue)) {
                        html.append(" checked ");
                    }
                }
                html.append("> ");
                html.append(displayText);

                // Finish up the table row
                html.append("     </font>\n");
                html.append("    </p>\n");
                html.append("  </td>\n");
            }
            html.append("</tr>\n");
        } // end loop

        return html.toString();
    }
}