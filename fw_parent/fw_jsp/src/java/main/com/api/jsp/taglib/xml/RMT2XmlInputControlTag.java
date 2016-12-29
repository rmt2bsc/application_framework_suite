package com.api.jsp.taglib.xml;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.SystemException;
import com.util.RMT2Utility;

import com.api.DaoApi;
import com.api.jsp.taglib.PageVariableHelper;
import com.api.jsp.taglib.RMT2AbstractInputControl;
import com.api.xml.XmlApiFactory;

/**
 * JSP custom tag implementation that extracts the value of an XML source stored
 * on one of the JSP variable scopes: application, session, request, or
 * pageContext. It is designed to render HTML output as a single value of plain
 * text or as one of the many HTML form input controls for the presentation
 * layer.
 * <p>
 * This class uses XPath searching techniques to search a XML document for an
 * element value. When normal HTML input controls are desired, the user must
 * supply the <i>type</i> attribute detailing the actual control to generate.
 * <p>
 * The source of the XML element can be in the form of a org.w3c.dom.Node or as
 * an XML document in String format. If the data source is of an XML document,
 * XPath is used as the querying method to target a specifie element.
 * <p>
 * For example, to obtain the value from a tag named, title, in an XML document
 * (xmlDocRef) stored in one of the JSP variable scopes:<br>
 * &lt;xml:InputControl query="book[@year=1995]" value="#xmlDocRef.title"/&gt;
 * <p>
 * To obtain the value of tag named, title, from an XML Node object
 * (xmlNodeRef): <br>
 * &lt;xml:InputControl value="#xmlDocRef.title"/&gt;
 * 
 * 
 * @author roy.terrell
 * 
 */
public class RMT2XmlInputControlTag extends RMT2AbstractInputControl {
    private static final long serialVersionUID = -5516057147536211745L;

    private static Logger logger = Logger
            .getLogger(RMT2XmlInputControlTag.class);

    private int dataTypeInt;

    /**
     * This is the datasource used to obtain XML data from an external provider.
     */
    private DaoApi dso = null;

    /**
     * The format string to be applied to property value of the HTML input
     * control. Uses basic Java Masks
     */
    protected String dataType = null;

    /** The XPath expression used to query the XML document */
    protected String query;

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Sets the data type property.
     * 
     * @param value
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
        this.dataTypeInt = RMT2Utility.getJavaType(this.dataType);
        try {
            this.dataTypeInt = Integer.parseInt(dataType);
        } catch (NumberFormatException e) {
            this.dataTypeInt = 0;
        }
    }

    /**
     * Locates the data source object that is assoicated with this custom tag
     * using _name and _scope.
     * 
     * @param _name
     *            The name of the datasource object.
     * @param _scope
     *            The JSP scope the datasource object can be found. When null,
     *            all scopes are probed.
     * @return Object
     * @throws JspException
     *             If datasource is not found
     */
    // protected Object getObject(String xml, String _scope) throws JspException
    // {
    // if (xml == null) {
    // xml = "";
    // }
    // return xml;
    // }
    /**
     * The entry point into this tag that creates the appropriate HTML input
     * control using either a XML DaoApi object or a XML String as the data
     * provider.
     * <ul>
     * <li>Determines if the request is to build a normal HTML input control or
     * to build either a select control or radiobutton group by examining the
     * <i>dataSource</i> input attributeproperty.</li>
     * <li>If a normal HTML input control is desired, uses the property name to
     * extract the value and data type from the data source.</li>
     * <li>Apply any formatting to the value, if applicable</li>
     * <li>Builds the appropriate HTML input control based on the Type attribute
     * of the custom tag.</li>
     * <li>Outputs the control to the JSP page via the ServletResponse object.</li>
     * </ul>
     * 
     * @return IterationTag.SKIP_BODY
     * @throws JspException
     *             for I/O errors, System errors, Database errors, or references
     *             made to properties that do not exist
     */
    public int startUp() throws JspException {
        super.startUp();
        boolean xpathQuery = false;
        PageVariableHelper valueHelper = new PageVariableHelper();
        String xpath = null;

        try {
            if (this.value != null && this.property != null) {
                throw new JspException(
                        "The \"value\" and \"property\" properties must be mutually exclusive");
            }

            // Obtain the appropriate datasource. The data source can be either
            // a DaoApi object
            // or an XML String.
            if (this.obj != null) {
                // For normal HTML input controls, the datasource is a valid
                // DaoApi
                // which base data has already been retrieved using the query
                // attribute.
                if (this.obj instanceof DaoApi) {
                    this.dso = (DaoApi) this.obj;
                }
                // For select and radiobutton HTML controls, the datasource
                // should be a XML String.
                // Query XML document using the query attribute. The property
                // attribute should be null.
                if (this.obj instanceof String) {
                    // Setup XML document data as an InputSource
                    this.dso = XmlApiFactory.createXmlDao(this.obj.toString());
                    // Bind the values of any Varialbe Expression Language
                    // variables that may exist in the XPath query.
                    xpath = valueHelper
                            .bindTokenValues(pageContext, this.query);
                    // Get look up data base on XPath query.
                    this.dso.retrieve(xpath);
                    xpathQuery = true;
                }
            }

            // A valid property indicates that we are trying to build a normal
            // HTML input control.
            boolean rowFound = false;
            String msg = null;
            if (this.dso != null && this.property != null) {
                if (xpathQuery) {
                    // Get XML node using XPath query
                    rowFound = this.dso.nextRow();
                    if (!rowFound) {
                        msg = "Datasource could not find value for property, "
                                + this.property + ", using XPath query ===> "
                                + xpath;
                        RMT2XmlInputControlTag.logger.log(Level.WARN, msg);
                        return IterationTag.SKIP_BODY;
                    }
                }
                this.value = this.dso.getColumnValue(this.property);
                if (this.value == null) {
                    this.value = "";
                }
                this.value = valueHelper.formatValue(this.value,
                        this.dataTypeInt, this.format);
            }
            else {
                if (this.dso != null && this.property == null
                        && this.value != null) {
                    if (this.value.equalsIgnoreCase("rowid")) {
                        this.value = (String) pageContext.getAttribute("ROW");
                    }
                }
            }
            return IterationTag.SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    /**
     * Resets the variables that possibly hold values to be output to the page.
     * This action will prevent these variable values from being used in the
     * execution of a subsequent custom tag on the page.
     * 
     * @throws JspException
     */
    protected void cleanUp() throws JspException {
        super.cleanUp();
        this.value = null;
        this.property = null;
        return;
    }

    /**
     * This method builds the options statements of the HTML Select control by
     * using a DaoApi iterface to cycle through an external data provider that
     * services XML data.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildSelectControlData() throws SystemException {
        StringBuffer html = new StringBuffer(100);
        String displayValue = null;
        String codeValue = null;

        if (this.dso == null) {
            return "";
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
     * This method dynamically builds HTML radio button input controls using XML
     * as the data source. The radio buttons are grouped together using a HTML
     * table.
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
