package com.api.jsp.taglib.bean;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.jsp.taglib.RMT2BodyTagSupportBase;
import com.api.jsp.taglib.bean.dao.BeanDao;
import com.api.jsp.taglib.bean.dao.BeanDaoFactory;

/**
 * A custom tag that iterates through an List of java beans and exposing each
 * object to its body content.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ForEachBeanTag extends RMT2BodyTagSupportBase {
    private static final long serialVersionUID = 9030330893980811772L;

    private List beanList;

    private int row = 0;

    private BeanDao dao;

    /**
     * The refeence to the java bean exposed to the body. This attribute is
     * required
     */
    protected String bean = null; // Required

    /** The list of objects to be iterated. This attribute is required */
    protected String list = null; // Required

    /**
     * The class name of the java bean that is exposed which makes the property,
     * bean, scriptable from within the JSP. This attribute is optional.
     */
    protected String beanClassId = null;

    /**
     * Sets the reference name for the bean property.
     * 
     * @param value
     */
    public void setBean(String value) {
        this.bean = value;
    }

    /**
     * Sets the list of objects.
     * 
     * @param value
     */
    public void setList(String value) {
        this.list = value;
    }

    /**
     * Sets the class name for the property, bean.
     * 
     * @param value
     */
    public void setBeanClassId(String value) {
        this.beanClassId = value;
    }

    /**
     * Entry point into the RMT2ForEachBeanTag. Verifies that the list
     * collection is of type ArrayList and begins to evaluate the body for the
     * first iteration.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if there is at least one item to
     *         process. IterationTag.SKIP_BODY when there is no more content to
     *         process.
     * @throws JspException
     *             If the list is not of type ArrayList.
     */
    public int doStartTag() throws JspException {
        this.obj = null;
        this.row = 0;
        this.obj = this.getObject();

        if (this.obj instanceof List) {
            this.beanList = (List) this.obj;
        }
        else {
            throw new JspException(
                    "Error: Property, list, must evaluate as type ArrayList for tag <ForEachBean>.   Class: RMT2ForEachBeanTag");
        }

        if (this.beanList.size() > 0) {
            try {
                this.dao = (BeanDao) BeanDaoFactory.createApi();
                this.dao.retrieve(this.beanList);
                if (!this.dao.nextRow()) {
                    return IterationTag.SKIP_BODY;
                }
                return IterationTag.EVAL_BODY_AGAIN;
            } catch (Exception e) {
                return IterationTag.SKIP_BODY;
            }
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
            if (this.dao.nextRow()) {
                this.exportData();
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
    }

    /**
     * This method is used to identify and obtain the list of objects that need
     * to be processed.
     * 
     * @return An Object disguised as an ArrayList.
     * @throws JspException
     *             If a problem occured getting the list collection.
     */
    private Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.list, this.objScope)
                : this.obj);
    }

    /**
     * Exports the object from the list of the current iteration to the
     * PageContext which will be accessible from the JSP. Also, the current
     * indexed row is made available to the JSP via the PageContext.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        // Publish the current bean object as "this.bean" at page scope
        pageContext.setAttribute(this.bean, this.dao, PageContext.PAGE_SCOPE);

        // Publish the current iteration count as "ROW" at page scope
        pageContext.setAttribute("ROW", String.valueOf(this.row),
                PageContext.PAGE_SCOPE);
    }

}