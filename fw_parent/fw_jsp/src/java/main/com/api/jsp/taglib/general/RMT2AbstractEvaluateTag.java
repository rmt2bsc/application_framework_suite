package com.api.jsp.taglib.general;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.IterationTag;

import com.SystemException;
import com.api.jsp.taglib.PageVariableHelper;
import com.api.jsp.taglib.RMT2BodyTagSupportBase;

/**
 * This abstract class provides functionality to accept and export expressions
 * for the Evaluate custom tag implementation and is responsible for managing
 * the expression that is to be evaluated.
 * 
 * @author roy terrell
 */
public abstract class RMT2AbstractEvaluateTag extends RMT2BodyTagSupportBase {

    private static final long serialVersionUID = 1L;

    private String expression;

    /**
     * Set expression.
     * 
     * @param value
     *            String expression
     */
    public void setExpression(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.expression = (String) helper.getValue(this.pageContext, value,
                    null, null);
        } catch (SystemException e) {
            this.expression = value;
        }
    }

    /**
     * Set expression using an int.
     * 
     * @param int expression
     */
    public void setExpression(int value) {
        this.expression = String.valueOf(value);
    }

    /**
     * Set expression using a double.
     * 
     * @param value
     *            double expression
     */
    public void setExpression(double value) {
        this.expression = String.valueOf(value);
    }

    /**
     * This serves as the entry point into the Evaluate custom tag. Obtain the
     * value of the property, "expression". The value of the property,
     * expression, can be a literal or a runtime expression. Furthermore, the
     * expression can also refer to the name of an attribute that resides on one
     * of the Servlet/JSP object scopes (application, session, request, or page)
     * in which the value of that attribute is returned.
     */
    public int doStartTag() throws JspException {
        // Check if temp is refering to an attribute on one of the scop
        // variables. If true, obtain
        // the value of the attribute. Otherwise, accept the value of temp
        // as-is.
        this.obj = null;
        try {
            this.obj = (this.obj == null ? this.getObject(this.expression,
                    this.objScope) : this.obj);
        } catch (JspException e) {
            this.obj = this.expression;
        }

        if (this.obj == null) {
            return IterationTag.SKIP_BODY;
        }
        else {
            return IterationTag.EVAL_BODY_AGAIN;
        }
    }

    /**
     * Ensures that the body is evaluated only one time.
     */
    public int doAfterBody() throws JspException {
        return IterationTag.SKIP_BODY;
    }

    /**
     * Sets a given value onto the page context with an object scopr of "page".
     * This makes the value accessible by other tag elements on the page.
     * 
     * @throws JspException
     */
    protected abstract void exportData() throws JspException;

    /**
     * Provides the data structure and accessor methods to keep track of an
     * evaluate tag.
     * 
     * @author roy.terrell
     * 
     */
    protected class EvalData {
        private Object value;

        private boolean found;

        protected EvalData(Object _expr) {
            value = _expr;
            found = false;
        }

        protected void setValue(Object _value) {
            value = _value;
        }

        protected Object getValue() {
            return value;
        }

        protected void setFound(boolean flag) {
            found = flag;
        }

        protected boolean isFound() {
            return found;
        }
    }

}