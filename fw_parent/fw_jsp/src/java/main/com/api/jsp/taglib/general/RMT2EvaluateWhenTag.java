package com.api.jsp.taglib.general;

import java.io.IOException;

import java.util.Stack;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.IterationTag;

/**
 * This class represents the case part of the Evaluate custom tag. This tag
 * encloses the HTML tags that are to be interpreted when the evaluate
 * expression equals that case value.
 * 
 * @author roy terrell
 */
public class RMT2EvaluateWhenTag extends RMT2AbstractEvaluateTag {

    private static final long serialVersionUID = 1L;

    private boolean evalTrue = false;

    private EvalData evalObj;

    /**
     * Obtains the evaluate expsression and the case expression to determine
     * equality. The evaluate expression resides on the pageContext as an
     * attribute, and the case expression is processed for the first time via
     * the "expression" property. The evaluation will fail if either expression
     * is found to not be of type String. Both expressions are treated as case
     * sensitive.
     */
    public int doStartTag() throws JspException {
        // Get expression from the parent evaluate tag
        Object exprStack = pageContext.getAttribute(
                RMT2EvaluateExpressionTag.EXPRESSION_NAME,
                PageContext.PAGE_SCOPE);

        // Verify that parent expression and this tag's expression are of the
        // correct type
        if (!(exprStack instanceof Stack)) {
            this.evalTrue = false;
            return IterationTag.SKIP_BODY;
        }

        this.evalObj = (EvalData) ((Stack) exprStack).peek();
        if (this.evalObj.isFound()) {
            return IterationTag.SKIP_BODY;
        }

        if (super.doStartTag() == IterationTag.SKIP_BODY) {
            return IterationTag.SKIP_BODY;
        }

        Object evalValue = null;
        evalValue = this.evalObj.getValue();
        if (!(evalValue instanceof String)) {
            this.evalTrue = false;
            return IterationTag.SKIP_BODY;
        }

        if (!(this.obj instanceof String)) {
            this.evalTrue = false;
            return IterationTag.SKIP_BODY;
        }

        // Do both expressions equate?
        this.evalTrue = evalValue.toString().equals(this.obj.toString());
        if (this.evalTrue) {
            return IterationTag.EVAL_BODY_AGAIN;
        }
        return IterationTag.SKIP_BODY;
    }

    /**
     * Processes the body content if it is determined that the evaluate and case
     * expressions are equal. Regardless of the outcome of the evaluation of the
     * two expressions, the body is skipped after the first pass.
     */
    public int doAfterBody() throws JspException {

        // Write out body content if condition was met.
        if (this.evalTrue) {
            // Set found flag of EvalData inner class to true which will be
            // reflected on the stack.
            this.evalObj.setFound(true);
            try {
                this.getBodyContent().writeOut(this.getPreviousOut());
                this.getBodyContent().clear();
            } catch (IOException e) {
                throw new JspException(
                        "Error: IOExeption while writing body content to the user");
            }
        }
        return IterationTag.SKIP_BODY;
    }

    /**
     * Not applicable since there is no data to publish.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        return;
    }

}