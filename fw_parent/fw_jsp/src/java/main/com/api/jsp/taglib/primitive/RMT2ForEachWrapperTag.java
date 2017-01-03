package com.api.jsp.taglib.primitive;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.jsp.taglib.RMT2BodyTagSupportBase;
import com.util.RMT2Utility;

/**
 * A custom tag that iterates through an array of objects and exposing each
 * array element to its body content.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ForEachWrapperTag extends RMT2BodyTagSupportBase {
    private static final long serialVersionUID = 4095652455626850870L;

    private int row = 0;

    // private Object beanValue = null;
    private Object beanValues[];

    /**
     * The name assigned to each array element that is exported to the JSP.
     * Required
     */
    protected String bean = null;

    /** The name of the array of objects. Required */
    protected String list = null;

    /**
     * The class name of the java bean that is exposed which makes the property,
     * bean, scriptable from within the JSP. This attribute is optional.
     */
    protected String beanClassId = null;

    /**
     * Set the bean name that is to be assigned to the array element for each
     * iteration.
     * 
     * @param value
     */
    public void setBean(String value) {
        this.bean = value;
    }

    /**
     * Assings a name to the array of objects
     * 
     * @param value
     */
    public void setList(String value) {
        this.list = value;
    }

    /**
     * Sets the name of the class which identifies the data type of the
     * property, bean.
     * 
     * @param value
     */
    public void setBeanClassId(String value) {
        this.beanClassId = value;
    }

    /**
     * This is the entry point into the custom tag. Obtains the object
     * collection and verifies that it is valid.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if there is at least one item to
     *         process. IterationTag.SKIP_BODY when there is no more content to
     *         process or the data source is not an array.
     * @throws JspException
     *             If the array of objects equal null.
     */
    public int doStartTag() throws JspException {
        this.row = 0;
        this.obj = null;
        this.obj = this.getObject();
        if (this.obj != null) {
            if (RMT2Utility.isArray(this.obj)) {
                this.beanValues = (Object[]) this.obj;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        }
        else {
            return IterationTag.SKIP_BODY;
        }
        if (this.beanValues.length > 0) {
            return IterationTag.EVAL_BODY_AGAIN;
        }
        else {
            return IterationTag.SKIP_BODY;
        }
    }

    /**
     * This process is triggered once the remaining content of the current
     * iteration has been evaluated. The content is written to the JSP via the
     * JSP writer and the next iteration of body content is set to begin which
     * includes the exporting of data until all iterations have been exhausted.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN to process the next iteration of the
     *         body. IterationTag.SKIP_BODY when there is no more content to
     *         process.
     * @throws JspException
     *             unable to write body content to JSP.
     */
    public int doAfterBody() throws JspException {
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();

            // Export next bean provided index is not out of bounds of the
            // ArrayList object
            this.row++;
            if (this.row < this.beanValues.length) {
                this.exportData();
                return EVAL_BODY_AGAIN;
            }
            else {
                return SKIP_BODY;
            }
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }
    }

    /**
     * Get the array of objects stored either in the application, session,
     * request, or pageContext scopes.
     * 
     * @return the array objects
     * @throws JspException
     */
    private Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.list, this.objScope)
                : this.obj);
    }

    /**
     * This method is used to identify and obtain the array of objects that need
     * to be processed using _name and _scope. .
     * 
     * @param _name
     *            The name assigned to the List collection
     * @param _scope
     *            The servlet variable scope to locate list refernece as _name.
     * @return An Object disguised as an Objec[].
     * @throws JspException
     *             If unable to obtain a reference for the array using _name.
     */
    protected Object getObject(String _name, String _scope) throws JspException {
        try {
            return super.getObject(_name, _scope);
        } catch (JspException e) {
            return null;
        }
    }

    /**
     * Exports an array element from the current iteration to the PageContext
     * which will be accessible from the JSP. Also, the current indexed row is
     * made available to the JSP via the PageContext.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        // Publish the current bean object as "this.bean" at page scope
        pageContext.setAttribute(this.bean, this.beanValues[this.row],
                PageContext.PAGE_SCOPE);

        // Publish the current iteration count as "ROW" at page scope
        pageContext.setAttribute("ROW", String.valueOf(this.row),
                PageContext.PAGE_SCOPE);
    }

}