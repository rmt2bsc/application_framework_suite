package com.api.jsp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.constants.RMT2TagConst;
import com.api.persistence.DatabaseException;

/**
 * This class builds HTML input controls from a java bean data source.
 * 
 * @author roy.terrell
 * 
 */
public abstract class RMT2AbstractInputControl extends RMT2TagSupportBase {
    private static final long serialVersionUID = -6105678683658087570L;
    private static Logger logger = Logger.getLogger(RMT2AbstractInputControl.class);

    /**
     * Tracks the position of the item that is currently being processed from a
     * list of items.
     */
    private int itemNdx;

    /** The integer equivalent of property, <i>col</i> */
    private int colInt;

    /** Select input control type tag name */
    protected static final String INPUT_TYPE_SELECT = "select";

    /** Radio Button Lookup input control type tag name */
    protected static final String INPUT_TYPE_RADIOLOOKUP = "radiolookup";

    /** Radio Button input control type tag name */
    protected static final String INPUT_TYPE_RADIO = "radio";

    /** TextArea input control type tag name */
    protected static final String INPUT_TYPE_MUTILINE = "textarea";

    /** The name of the property targeted for this tag */
    protected String property = null;

    /** The data type the property */
    protected String type = null;

    /** The name of the HTML input control */
    protected String name = null;

    /** The size of the HTML input control */
    protected String size = null;

    /** Maximum length of HTML input control */
    protected String maxLength = null;

    /** Value indicating the alignment of the contents of the HTML input control */
    protected String align = null;

    /**
     * Indicates whether or not the HTML input control is disabled. It is
     * disabled by default.
     */
    protected boolean disabled = false;

    /**
     * Indicates whether or not the HTML input control is read only. It is read
     * only by default.
     */
    protected boolean readOnly = false;

    /** Indicates the tab ordering of the HTML input */
    protected String tabIndex = null;

    /** The style clause of the HTML input control */
    protected String style = null;

    /** The onFocus event handler invocation implementation */
    protected String onFocus = null;

    /** The onBlur event handler invocation implementation */
    protected String onBlur = null;

    /** The onSelect event handler invocation implementation */
    protected String onSelect = null;

    /** The onChange event handler invocation implementation */
    protected String onChange = null;

    /** The onClick event handler invocation implementation */
    protected String onClick = null;

    /** The onkeydown event handler invocation implementation */
    protected String onKeydown = null;

    /** The onkeypress event handler invocation implementation */
    protected String onKeypress = null;

    /** The onkeyup event handler invocation implementation */
    protected String onKeyup = null;

    /**
     * The format string to be applied to property value of the HTML input
     * control. Uses basic Java Masks
     */
    protected String format = null;

    /** The value of the property */
    protected String value = null;

    /**
     * Contains a value of 'checked' to indicate that the HTML radio button or
     * checkbox input control is selected
     */
    protected String checkedValue = null;

    /**
     * Contains a blank value to indicate that the HTML radio button or checkbox
     * input control is not selected.
     */
    protected String unCheckedValue = null;

    /**
     * Indicates that the HTML input control's name should be uniquely
     * identified.
     */
    protected boolean uniqueName = false;

    /**
     * Used as one of the custom tag properties to indicate whether or not the
     * HTML Select tag can have multiple selections
     */
    protected String multiSelect = null;

    /**
     * Used internally as a boolean value to indicate whether or not the HTML
     * Select tag can have multiple selections
     */
    protected boolean multiple = false;

    /**
     * The value contained in the HTML Select tag that is to be selected once
     * the Select tag is rendered.
     */
    protected String selectedValue = null;

    /**
     * This is the name of the property that is mapped to the values displayed
     * in the dropdown list
     */
    protected String displayProperty = null;

    /**
     * This is the name of the property that is mapped to the code values in the
     * dropdown list
     */
    protected String codeProperty = null;

    /** The title of the HTML input control */
    protected String title = null;

    /**
     * Used as one of the custom tag properties to indicate whether or not a
     * border should be drawn around the the HTML input control
     */
    protected String border = null;

    /**
     * Used internally to indicate whether or not a border should be drawn
     * around the the HTML input control
     */
    protected boolean drawBorder = false;

    /** Mandates the number of rows contained in the HTML input control. */
    protected String rows = null;

    /** Mandates the number of columns contained in the HTML input control. */
    protected String cols = null;

    /**
     * Used as one of the custom tag properties to indicate whether or not the
     * custom tag is part of master/detail relationship
     */
    protected String masterDetailLink = null;

    /**
     * Used internally to indicate whether or not the custom tag is part of
     * master/detai relationship
     */
    protected boolean masterDetail = false;

    /** Used to wrap content in a text or textarea input control */
    protected String wrap = null;

    /**
     * The position text value is to be displayed on radio button or checkbox
     * control. The default position is "left".
     */
    protected String displayPropertyPos;

    /**
     * Sets the property name.
     * 
     * @param _value
     */
    public void setProperty(String _value) {
        this.property = _value;
    }

    /**
     * Set Property Type
     * 
     * @param _value
     */
    public void setType(String _value) {
        this.type = _value;
    }

    /**
     * Set the name of the HTML input cotrol.
     * 
     * @param _value
     */
    public void setName(String _value) {
        this.name = _value;
    }

    /**
     * Set the value of the HTML Input control.
     * 
     * @param _value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Set the size of the HTML input control.
     * 
     * @param _value
     */
    public void setSize(String _value) {
        this.size = _value;
    }

    /**
     * Set the maximum length of the contents of the HTML input cotrol.
     * 
     * @param _value
     */
    public void setMaxLength(String _value) {
        this.maxLength = _value;
    }

    /**
     * Set the alignment of the HTML input control.
     * 
     * @param _value
     */
    public void setAlign(String _value) {
        this.align = _value;
    }

    /**
     * Set the disable attribute of the HTML control.
     * 
     * @param _value
     */
    public void setDisabled(String _value) {
        this.disabled = (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("y") || _value.equalsIgnoreCase("true"));
    }

    /**
     * Set the read only attribute of the HTML input control.
     * 
     * @param _value
     */
    public void setReadOnly(String _value) {
        this.readOnly = (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("y") || _value.equalsIgnoreCase("true"));
    }

    /**
     * Set the tab order of the HTML input control.
     * 
     * @param _value
     */
    public void setTabIndex(String _value) {
        this.tabIndex = _value;
    }

    /**
     * Set the style of the HTML input control.
     * 
     * @param _value
     */
    public void setStyle(String _value) {
        this.style = _value;
    }

    /**
     * Set the onFoucus event Handler for the HTML control. _value should be the
     * actual call to the event handler.
     * 
     * @param _value
     */
    public void setOnFocus(String _value) {
        this.onFocus = _value;
    }

    /**
     * Set the onBlur event Handler for the HTML control. _value should be the
     * actual call to the event handler.
     * 
     * @param _value
     */
    public void setOnBlur(String _value) {
        this.onBlur = _value;
    }

    /**
     * Set the onSelect event Handler for the HTML control. _value should be the
     * actual call to the event handler.
     * 
     * @param _value
     */
    public void setOnSelect(String _value) {
        this.onSelect = _value;
    }

    /**
     * Set the onChange event Handler for the HTML control. _value should be the
     * actual call to the event handler.
     * 
     * @param _value
     */
    public void setOnChange(String _value) {
        this.onChange = _value;
    }

    /**
     * Set the onClick event Handler for the HTML control. _value should be the
     * actual call to the event handler.
     * 
     * @param _value
     */
    public void setOnClick(String _value) {
        this.onClick = _value;
    }

    public String getOnKeydown() {
        return onKeydown;
    }

    public void setOnKeydown(String onKeydown) {
        this.onKeydown = onKeydown;
    }

    public String getOnKeypress() {
        return onKeypress;
    }

    public void setOnKeypress(String onKeypress) {
        this.onKeypress = onKeypress;
    }

    public String getOnKeyup() {
        return onKeyup;
    }

    public void setOnKeyup(String onKeyup) {
        this.onKeyup = onKeyup;
    }

    /**
     * Set the format of the value property of the HTML input control.
     * 
     * @param _value
     */
    public void setFormat(String _value) {
        this.format = _value;
    }

    /**
     * Set the checked indicator for the radio button or checkbox HTML input
     * control.
     * 
     * @param _value
     */
    public void setCheckedValue(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.checkedValue = (String) helper.getValue(this.pageContext, value, null, null);
        } catch (SystemException e) {
            this.checkedValue = value;
        }
    }

    /**
     * Set HTML radio button or checkbox input control as unchecked
     * 
     * @param _value
     */
    public void setUnCheckedValue(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.unCheckedValue = (String) helper.getValue(this.pageContext, value, null, null);
        } catch (SystemException e) {
            this.unCheckedValue = value;
        }
    }

    /**
     * Set the unique name indicator of the HTML input control.
     * 
     * @param _value
     */
    public void setUniqueName(String _value) {
        this.uniqueName = (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("y") || _value.equalsIgnoreCase("true"));
    }

    /**
     * Set the multi-select indicator for HTML Select tag
     * 
     * @param _value
     */
    public void setMultiSelect(String _value) {
        this.multiple = (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("y") || _value.equalsIgnoreCase("true"));
    }

    /**
     * Set the Select value property of the HTML Select cotrol.
     * 
     * @param _value
     */
    public void setSelectedValue(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.selectedValue = (String) helper.getValue(this.pageContext, value, null, null);
        } catch (SystemException e) {
            this.selectedValue = value;
        }
    }

    /**
     * Set the display property of the dropdown list control.
     * 
     * @param _value
     */
    public void setDisplayProperty(String _value) {
        this.displayProperty = _value;
    }

    /**
     * Select the code value of the dropdown list control.
     * 
     * @param _value
     */
    public void setCodeProperty(String _value) {
        this.codeProperty = _value;
    }

    /**
     * Select the title of the HTML input control.
     * 
     * @param _value
     */
    public void setTitle(String _value) {
        this.title = _value;
    }

    /**
     * Set the border indicator for the HTML input control.
     * 
     * @param _value
     */
    public void setBorder(String value) {
        this.drawBorder = (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("true"));
    }

    /**
     * Set the number of rows in the HTML input control.
     * 
     * @param value
     */
    public void setRows(String value) {
        this.rows = value;
    }

    /**
     * Set the number of columns in the HTML input control.
     * 
     * @param value
     */
    public void setCols(String value) {
        this.cols = value;
        try {
            this.colInt = Integer.parseInt(this.cols);
        } catch (NumberFormatException e) {
            this.colInt = 0;
        }
    }

    /** The space format used for property value. */
    protected String spaceFormat = null;

    /**
     * Set the Master/Detail Link inidcator for the custom tag.
     * 
     * @param _value
     */
    public void setMasterDetailLink(String value) {
        this.masterDetail = (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("true"));
    }

    /**
     * Set the spacing format of property.
     * 
     * @param value
     */
    public void setSpaceFormat(String value) {
        // value = ""
        // value = "&nbsp;"
        this.spaceFormat = value;
    }

    /**
     * Set the display text position for a radio button or a checkbox. Valid
     * values should be "left" and "right".
     * 
     * @param textPos
     *            the textPos to set
     */
    public void setDisplayPropertyPos(String textPos) {
        this.displayPropertyPos = textPos;
    }

    /**
     * Set the wrap attribute for a text or textarea control
     * 
     * @param wrap
     */
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    /**
     * The driver for obtaining and outputting the value from the requesting
     * custom tag.
     * <ul>
     * <li>Calls startUp method to obtain the value.</li>
     * <li>Builds the appropriate HTML input control based on the Type attribute
     * of the custom tag by invoking the method, buildInputControl()</li>
     * <li>Outputs the dynamic HTML to the JSP page by invoking the method,
     * outputHtml(String).</li>
     * </ul>
     * 
     * @return IterationTag.SKIP_BODY
     * @throws JspException
     *             I/O errors, System errors, Database errors, or references
     *             made to properties that do not exist.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        super.doStartTag();

        // Get value
        this.startUp();

        // Output HTML as either a form control or plain text
        try {
            if (this.type != null) {
                this.outputHtml(this.buildInputControl());
            }
            else if (this.value != null && !this.value.equals("")) {
                this.outputHtml(this.value);
            }
        } catch (IOException e) {
            throw new JspException("IOException: " + e.getMessage());
        } catch (SystemException e) {
            throw new JspException("SystemException: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new JspException("DatabaseException: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new JspException("NotFoundException: " + e.getMessage());
        }

        // Reset the state of the object
        this.cleanUp();

        return IterationTag.SKIP_BODY;
    }

    /**
     * Obtains the appropriate value requested by the client either a
     * {@link com.api.DaoApi DaoApi} or a wrapper object for primitive types.
     * <ul>
     * <li>Attempts to obtain the value as a row id from either
     * {@link com.constants.RMT2TagConst.ROW_ID_REGULAR ROW_ID_REGULAR} or
     * {@link com.constants.RMT2TagConst.ROW_ID_MASTERDETAIL
     * ROW_ID_MASTERDETAIL} named attributes that exist on one of the web
     * container variable scopes.</li>
     * <li>Attempts to obtain the value from an object variable expression
     * forwarded from the user's JSP.</li>
     * <li>Apply any formatting to the value, if applicable</li>
     * </ul>
     * 
     * @return IterationTag.SKIP_BODY
     * @throws JspException
     *             N/A
     */
    protected int startUp() throws JspException {
        // Attempt to obtain the value from target data source.
        if (this.value != null) {
            if (this.value.equalsIgnoreCase(RMT2TagConst.HTML_ROWID)) {
                this.value = (String) pageContext.getAttribute(RMT2TagConst.ROW_ID_REGULAR);
            }
            else if (this.value.equalsIgnoreCase(RMT2TagConst.HTML_MASTER_ROWID)) {
                this.value = (String) pageContext.getAttribute(RMT2TagConst.ROW_ID_MASTERDETAIL);
            }
            else if (this.value != null && !this.value.equals("")) {
                try {
                    PageVariableHelper valueHelper = new PageVariableHelper();
                    this.value = (String) valueHelper.getValue(this.pageContext, this.value, null, this.format);
                } catch (SystemException e) {
                    RMT2AbstractInputControl.logger.log(Level.WARN, "Variable Expression Language (VEL) property expressiion, "
                                    + this.value + ", is invalid");
                    this.value = null;
                }
            }
            else {
                // Apply any space formatting.
                if (this.value == null) {
                    if (this.spaceFormat == null) {
                        this.value = "";
                    }
                    else {
                        this.value = this.spaceFormat;
                    }
                }
                else if (this.value.length() <= 0) {
                    if (this.spaceFormat == null) {
                        this.value = "";
                    }
                    else {
                        this.value = this.spaceFormat;
                    }
                }
            }
        }
        return IterationTag.SKIP_BODY;
    }

    /**
     * Stub method just after results are output to the page.
     * 
     * @throws JspException
     */
    protected void cleanUp() throws JspException {
        return;
    }

    /**
     * This method uses the information gathered in its member variables to
     * build HTML input controls such as text, radio button, textarea, or Select
     * controls. Special processing is required for select, textarea, and lookup
     * radio buttons. Values that are to be mapped to text, checkboxes,and radio
     * buttons directly do not require any special processing.
     * 
     * @return The HTML in the format of a String.
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     * @throws JspException
     */
    protected String buildInputControl() throws DatabaseException, NotFoundException, SystemException, JspException {
        StringBuffer html = new StringBuffer(200);

        if (this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_SELECT)
                || this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_RADIOLOOKUP)
                || this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_MUTILINE)) {

            // Build HTML for Select control
            if (this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_SELECT)) {
                return this.buildSelectControl(html);
            }
            // Build HTML for a group of radio buttons driven by the database
            if (this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_RADIOLOOKUP)
                    || this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_RADIO)) {
                return this.buildRadioControl(html);
            }
            // Build HTML for textarea control.
            if (this.type.equalsIgnoreCase(RMT2AbstractInputControl.INPUT_TYPE_MUTILINE)) {
                return this.buildTextArea(html);
            }
        }

        // Add id attribute
        if (this.id != null) {
            html.append(" id=\"");
            html.append(this.id);
            if (this.uniqueName) {
                String row = (String) pageContext.getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
                                : RMT2TagConst.ROW_ID_REGULAR);
                if (row != null && row.length() > 0) {
                    html.append(row);
                }
            }
            html.append("\"");
        }

        // Add type attribute
        if (this.type != null) {
            html.append(" type=\"");
            html.append(this.type.toLowerCase());
            html.append("\"");
        }
        else {
            html.append(" type=\"text\" ");
        }

        // Add name attribute
        if (this.name != null) {
            html.append(" name=\"");
            html.append(this.name);
            if (this.uniqueName) {
                String row = (String) pageContext.getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
                                : RMT2TagConst.ROW_ID_REGULAR);
                if (row != null && row.length() > 0) {
                    html.append(row);
                }
            }
            html.append("\"");
        }

        // Add value attribute
        if (this.value != null) {
            html.append(" value=\"");
            html.append(this.value);
            html.append("\"");
        }

        // Add size attribute
        if (this.size != null) {
            html.append(" size=\"");
            html.append(this.size);
            html.append("\"");
        }

        // Add maxLength attribute
        if (this.maxLength != null) {
            html.append(" maxlength=\"");
            html.append(this.maxLength);
            html.append("\"");
        }

        // Add align attribute
        if (this.align != null) {
            html.append(" align=\"");
            html.append(this.align.toLowerCase());
            html.append("\"");
        }

        // Add disabled attribute
        if (this.disabled) {
            html.append(" disabled");
        }

        // Add read only attribute
        if (this.readOnly) {
            html.append(" readonly");
        }

        // Add tabIndex attribute
        if (this.tabIndex != null) {
            html.append(" tabindex=\"");
            html.append(this.tabIndex);
            html.append("\"");
        }

        // Add style attribute
        if (this.style != null) {
            html.append(" style=\"");
            html.append(this.style);
            html.append("\"");
        }

        // Add Multiple attribute for Select HTML Control
        if (this.multiple) {
            html.append(" multiple");
        }

        // Determine if checkbox is slected.
        if (this.type.equalsIgnoreCase("checkbox") || this.type.equalsIgnoreCase("radio")) {
            if (this.value.equalsIgnoreCase(this.checkedValue)) {
                html.append(" checked ");
            }
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

        // Add onClick attribute
        if (this.onClick != null) {
            html.append(" onClick=\"");
            html.append(this.onClick);
            html.append("\"");
        }

        // Add onkeydown attribute
        if (this.onKeydown != null) {
            html.append(" onkeydown=\"");
            html.append(this.onKeydown);
            html.append("\"");
        }

        // Add onkeypress attribute
        if (this.onKeypress != null) {
            html.append(" onkeypress=\"");
            html.append(this.onKeypress);
            html.append("\"");
        }

        // Add onkeyup attribute
        if (this.onKeyup != null) {
            html.append(" onkeyup=\"");
            html.append(this.onKeyup);
            html.append("\"");
        }

        // Complete construction of input control
        if (html.length() <= 0) {
            return "";
        }

        html.insert(0, "<input");
        html.append(">");

        String finalHtlm = html.toString();

        if (this.type.equalsIgnoreCase("radio") || this.type.equalsIgnoreCase("checkbox")) {
            if (this.displayProperty != null) {
                // Determine the position of display text, if available.
                if (this.displayPropertyPos == null || this.displayPropertyPos.equalsIgnoreCase("left")) {
                    finalHtlm = this.displayProperty + html.toString();
                }
                else {
                    finalHtlm = html.toString() + this.displayProperty;
                }
            }
        }

        return finalHtlm;
    }

    /**
     * Builds a HTML TextArea control when the custom tag attribute, type,
     * equals "multiline".
     * 
     * @param html
     *            variable used as a work area for building the control.
     * @return The HTML in the format of a String.
     */
    protected String buildTextArea(StringBuffer html) {
        if (this.name != null) {
            html.append("<textarea name=\"" + this.name + "\"");
        }
        else {
            html.append("<textarea ");
        }

        // Add id attribute
        if (this.id != null) {
            html.append(" id=\"");
            html.append(this.id);
            if (this.uniqueName) {
                String row = (String) pageContext.getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
                                : RMT2TagConst.ROW_ID_REGULAR);
                if (row != null && row.length() > 0) {
                    html.append(row);
                }
            }
            html.append("\"");
        }

        // Add rows
        if (this.rows != null) {
            html.append(" rows=");
            html.append(this.rows);
        }

        // Add cols
        if (this.cols != null) {
            html.append(" cols=");
            html.append(this.cols);
        }

        // Set wrap attribute
        if (this.wrap != null) {
            html.append(" wrap=");
            html.append(this.wrap);
        }

        // Add disabled attribute
        if (this.disabled) {
            html.append(" disabled");
        }

        // Add read only attribute
        if (this.readOnly) {
            html.append(" readonly");
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

        html.append(">");
        if (this.value == null) {
            html.append("");
        }
        else {
            if (this.value.trim().length() > 0) {
                html.append(this.value);
            }
        }
        html.append("</textarea>");
        return html.toString();
    }

    /**
     * Builds a HTML Select control when the custom tag, type, equals "select".
     * 
     * @param html
     *            variable used as a work area for building the control.
     * @returnThe HTML in the format of a String.
     * @throws NotFoundException
     * @throws SystemException
     * @throws JspException
     */
    protected String buildSelectControl(StringBuffer html) throws NotFoundException, SystemException, JspException {

        if (this.name != null) {
            html.append("<select name=\"" + this.name);
            if (this.uniqueName) {
                String row = (String) pageContext.getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
                                : RMT2TagConst.ROW_ID_REGULAR);
                if (row != null && row.length() > 0) {
                    html.append(row);
                }
            }
            html.append("\"");
        }
        else {
            html.append("<select ");
        }

        // Add id attribute
        if (this.id != null) {
            html.append(" id=\"");
            html.append(this.id);
            if (this.uniqueName) {
                String row = (String) pageContext.getAttribute(this.masterDetail ? RMT2TagConst.ROW_ID_MASTERDETAIL
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
        html.append(">  ");

        // Do not display "Select One" option if we allow multiple selections
        if (!this.multiple) {
            html.append("\n\t<option value=\"\">-- Select One --");
        }

        // Gather select options as HTML script
        String dataHtml = this.buildSelectControlData();
        html.append(dataHtml);
        html.append("</select>");
        return html.toString();
    }

    /**
     * Abstract method for building the options statements of the HTML Select
     * control using values from an external data provider. The implementor is
     * required to construct the portion of the HTML Select statement that
     * follows: {@literal <select name='****' attrib1=xxx, attrib2=xxx,..>}.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected abstract String buildSelectControlData() throws SystemException;

    /**
     * Builds a HTML radio button input control when the custom tag, type,
     * equals "radio" or "radiolookup".
     * 
     * @param html
     *            variable used as a work area for building the control.
     * @returnThe HTML in the format of a String.
     * @throws NotFoundException
     * @throws SystemException
     * @throws JspException
     */
    protected String buildRadioControl(StringBuffer html) throws JspException,
            NotFoundException, SystemException {
        // Create an outer table with border, if requested.
        if (this.drawBorder) {
            html.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n");
            html.append("  <tr>\n");
            html.append("     <td>\n");
        }
        // Create table to contain the actual radio buttons.
        html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");

        // Gather rows of radio buttons
        String dataHtml = this.buildRadioControlData();
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
     * Abstract method for dynamically building HTML radio button input controls
     * from an external data provider. The implementation should include logic
     * to cycle through the data source and obtain the code values and display
     * values needed to create the radio buttons. For each iteration the code
     * and display values are gathered, invoke method,
     * buildRadioControlHtml(String, String), to create the HTML table row
     * container for the radio button.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected abstract String buildRadioControlData() throws SystemException;

    /**
     * Setup <i>n</i> number of columns of radio buttons for each row of the
     * enclosing table. Uses <i>itemIndex</i> to determine the maximum number or
     * columns to included per row.
     * 
     * @param codeValue
     *            The value assigned to the value property of the radio button
     *            input control.
     * @param displayValue
     *            The value that is displayed as text on the radio button.
     * @param itemIndex
     *            The index of the current item from a list of items which is
     *            zero based.
     * @return The HTML needed to render the table row and its required number
     *         of columns.
     */
    protected String buildRadioControlHtml(String codeValue, String displayValue, int itemIndex) {
        // Increment item index to change index base from zero to one.
        this.itemNdx = itemIndex + 1;
        return buildRadioControlHtml(codeValue, displayValue);
    }

    /**
     * This method dynamically builds a HTML table row and the radio button that
     * is contained within the row usint codeValue and displayValue.
     * 
     * @param codeValue
     *            The value assigned to the value property of the radio button
     *            input control.
     * @param displayValue
     *            The value that is displayed as text on the radio button.
     * @return The HTML needed to render the table row and its contents.
     */
    protected String buildRadioControlHtml(String codeValue, String displayValue) {
        StringBuffer html = new StringBuffer(100);
        int startCol;
        int endCol;

        // See if we need to begin a new table row.
        if (this.colInt > 0) {
            // User has requested that n number of columns are displayed per
            // row.
            startCol = (this.itemNdx + this.colInt - 1) % this.colInt;
            if (startCol == 0) {
                html.append("<tr>\n");
            }
        }
        else {
            html.append("<tr>\n");
        }

        html.append("   <td align=\"left\" valign=\"top\">\n");
        html.append("      <p>\n");
        html.append("         <font size=\"2\">\n");

        // begin building the actual radio button
        if (this.name != null) {
            html.append("\n<input type=\"radio\" name=\"" + this.name + "\"");
        }
        else {
            html.append("<input type=\"radio\" ");
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

        // Add onClick attribute
        if (this.onClick != null) {
            html.append(" onClick=\"");
            html.append(this.onClick);
            html.append("\"");
        }

        // Add onChange attribute
        if (this.onChange != null) {
            html.append(" onChange=\"");
            html.append(this.onChange);
            html.append("\"");
        }

        html.append(" value=\"");
        html.append(codeValue);
        html.append("\"");

        if (this.selectedValue != null) {
            if (codeValue.equalsIgnoreCase(this.selectedValue)) {
                html.append(" checked ");
            }
        }
        html.append("> ");
        html.append(displayValue);

        // Finish up the table row
        html.append("     </font>\n");
        html.append("    </p>\n");
        html.append("  </td>\n");

        // See if we need to conditionally terminate table row
        if (this.colInt > 0) {
            // User has requested that n number of columns are displayed per
            // row.
            endCol = this.itemNdx % this.colInt;
            if (endCol == 0) {
                html.append("</tr>\n");
            }
        }
        else {
            html.append("</tr>\n");
        }

        return html.toString();
    }

    /**
     * Defaults to ancestor functionality
     */
    protected void exportData() throws JspException {
        return;
    }

} // end class
