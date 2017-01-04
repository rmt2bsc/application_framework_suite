package com.api.jsp.taglib.general;

import java.io.IOException;

import java.util.Stack;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.IterationTag;

/**
 * This class represents the default case part of the Evaluate custom tag. This
 * tag encloses the HTML tags that are to be interpreted when the evaluate
 * expression equals that case value.
 * 
 * @author roy terrell
 */
public class RMT2EvaluateWhenElseTag extends RMT2AbstractEvaluateTag {

    private static final long serialVersionUID = -7232358207111623124L;

    /**
     * This serves as the entry point into the Evaluate When Else custom tag. If
     * the enclosing Evaluate tag expression has not been satisfied by the
     * previous when tags, then contents of this tag is recognized by default.
     */
    public int doStartTag() throws JspException {
        EvalData eval;

        // Get expression from the parent evaluate tag
        Object exprStack = pageContext.getAttribute(
                RMT2EvaluateExpressionTag.EXPRESSION_NAME,
                PageContext.PAGE_SCOPE);

        // Verify that parent expression and this tag's expression are of the
        // correct type
        if (!(exprStack instanceof Stack)) {
            return IterationTag.SKIP_BODY;
        }

        // Get evaluate expression data object from the stack
        eval = (EvalData) ((Stack) exprStack).peek();

        // Has evaluation been satisfied?
        if (eval.isFound()) {
            return IterationTag.SKIP_BODY;
        }
        else {
            return IterationTag.EVAL_BODY_AGAIN;
        }
    }

    /**
     * Processes the body content if it is determined that the evaluate and case
     * conditions have not been statisfied.
     */
    public int doAfterBody() throws JspException {
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
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