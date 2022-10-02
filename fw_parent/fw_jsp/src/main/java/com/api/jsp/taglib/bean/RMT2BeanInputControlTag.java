package com.api.jsp.taglib.bean;

import javax.servlet.jsp.JspException;

import com.SystemException;
import com.api.jsp.taglib.PageVariableHelper;
import com.api.jsp.taglib.RMT2AbstractInputControl;
import com.api.jsp.taglib.bean.dao.BeanDao;
import com.api.jsp.taglib.bean.dao.BeanDaoFactory;
import com.api.persistence.DatabaseException;

/**
 * This class builds HTML input controls from a java bean (POJO) data source as
 * well as primitive wrapper classes.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2BeanInputControlTag extends RMT2AbstractInputControl {
    private static final long serialVersionUID = 159466380424986828L;

    /**
     * This method builds the options statements of the HTML Select control by
     * cycling through a List of Java Bean objects, which serves as the external
     * data provider, and obtaining and assigning each appropriately mapped java
     * bean value to an select option.
     * 
     * @return HTML as a String
     * @throws SystemException
     */
    protected String buildSelectControlData() throws SystemException {
        BeanDao dao = null;
        String displayValue = null;
        String codeValue = null;
        StringBuffer html = new StringBuffer(100);

        // Get DAO object from JSP page which contains the data needed to build
        // the HTML Select control
        // Check data type of incoming data object and cast accordingly.
        if (this.obj == null) {
            throw new SystemException("HTML Select control tag must have a valid Javabean or POJO");
        }
        if (this.obj instanceof BeanDao) {
            dao = (BeanDao) this.obj;
        }
        else {
            try {
                dao = (BeanDao) BeanDaoFactory.createApi();
                dao.retrieve(this.obj);
            } catch (DatabaseException e) {
                throw new SystemException("HTML Select control must be constructed using a List or BeanDao instance");
            }
        }

        try {
            while (dao.nextRow()) {
                displayValue = dao.getColumnValue(this.displayProperty);
                codeValue = dao.getColumnValue(this.codeProperty);
                displayValue = this.formatBeanValue(dao, this.displayProperty, displayValue);

                // Build HTML select control option clause.
                html.append("\n\t<option value=\"");
                html.append(codeValue);
                html.append("\"");
                if (this.selectedValue != null && codeValue.equalsIgnoreCase(this.selectedValue)) {
                    html.append(" selected ");
                }
                html.append("> ");
                html.append(displayValue);
            }
            return html.toString();
        } catch (Exception e) {
            throw new SystemException(e);
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
        BeanDao dao = null;

        // Get DAO object from JSP page which contains the data needed to build
        // the HTML Select control
        // Check data type of incoming data object and cast accordingly.
        if (this.obj == null) {
            throw new SystemException(
                    "HTML Select control must receive as input a valid data source");
        }
        if (this.obj instanceof BeanDao) {
            dao = (BeanDao) this.obj;
        }
        else {
            try {
                dao = (BeanDao) BeanDaoFactory.createApi();
                dao.retrieve(this.obj);
            } catch (DatabaseException e) {
                throw new SystemException(
                        "HTML Select control must be constructed using a List or BeanDao instance");
            }
        }

        try {
            // Begin building radio buttons
            int ndx = 0;
            while (dao.nextRow()) {
                displayValue = dao.getColumnValue(this.displayProperty);
                codeValue = dao.getColumnValue(this.codeProperty);
                displayValue = this.formatBeanValue(dao, this.displayProperty,
                        displayValue);

                // Create HTML for radio button and the table row that contains
                // it.
                ndx++;
                String temp = this.buildRadioControlHtml(codeValue,
                        displayValue, ndx);
                html.append(temp);
            }
            return html.toString();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Applies datetime or numeric formatting to the value of the custom tag
     * based on the value of property, format.
     * 
     * @param dao
     *            {@link com.api.bean.BeanDao BeanDao}
     * @param propName
     *            The name of the property that is to be formated.
     * @param propValue
     *            The value of the property that is to be formated.
     * @return The formated value as a String.
     * @throws JspException
     */
    protected String formatBeanValue(BeanDao dao, String propName, String propValue) throws JspException {
        String result = null;
        int javaType = 0;

        javaType = dao.getJavaType(propName);
        if (propValue == null) {
            return "";
        }

        PageVariableHelper valueHelper = new PageVariableHelper();
        try {
            result = valueHelper.formatValue(propValue, javaType, this.format);
            return result;
        } catch (SystemException e) {
            throw new JspException(e.getMessage());
        }
    }

} // end class
