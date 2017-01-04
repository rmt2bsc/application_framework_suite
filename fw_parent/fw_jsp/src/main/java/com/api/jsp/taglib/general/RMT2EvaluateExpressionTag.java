package com.api.jsp.taglib.general;

import java.io.IOException;

import java.util.Stack;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.jsp.taglib.general.RMT2AbstractEvaluateTag.EvalData;

/**
 * This class represents the evaluate expression that is responsible for
 * managing the expression that is to be evaluated for the Evaluate custom tag
 * component. This class requires that the method, exportData, be implemented.
 * 
 * @author roy terrell
 */
public class RMT2EvaluateExpressionTag extends RMT2AbstractEvaluateTag {

    private static final long serialVersionUID = 7523949500124570838L;

    /**
     * The exported attribute name that is used to locate the exported value of
     * the associated property, expression, via one of the Servlet/JSP object
     * scopes.
     */
    public static final String EXPRESSION_NAME = "evalexp";

    /**
     * Print the body content.
     */
    public int doAfterBody() throws JspException {
        // Write out body content if condition was met.
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }

        // Remove current expression from the statck.
        Stack exprStack = (Stack) pageContext.getAttribute(
                RMT2EvaluateExpressionTag.EXPRESSION_NAME,
                PageContext.PAGE_SCOPE);
        Object curEval = exprStack.pop();

        // If this is an inner evaluate statement, check if we need to flag the
        // outer
        // evaluate statement to abort all of its remaining "when" statements in
        // the
        // event condition has been satisfied.
        if (exprStack.size() > 0) {
            // There are more evaluate expressions to remove from the stack.
            if (curEval instanceof EvalData && ((EvalData) curEval).isFound()) {
                EvalData nextEval = (EvalData) ((Stack) exprStack).peek();
                nextEval.setFound(true);
            }
        }
        return IterationTag.SKIP_BODY;
    }

    /**
     * Sets the value keyed by "RMT2EvaluateExpressionTag.EXPRESSION_NAME" onto
     * the page context with page object scope making the value accessible by
     * other tag elements on the page.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        Object temp = null;

        // If stack of expressions does not exist, then create the stack
        // collection.
        temp = pageContext.getAttribute(
                RMT2EvaluateExpressionTag.EXPRESSION_NAME,
                PageContext.PAGE_SCOPE);
        if (temp == null || !(temp instanceof Stack)) {
            temp = new Stack();
        }

        // Create expression object to be used by the when tags
        EvalData data = new EvalData(this.obj);

        // Push value onto the stack
        ((Stack) temp).push(data);
        // Publish the stack of evaluate expression on the page context
        pageContext.setAttribute(RMT2EvaluateExpressionTag.EXPRESSION_NAME,
                temp, PageContext.PAGE_SCOPE);
    }

}