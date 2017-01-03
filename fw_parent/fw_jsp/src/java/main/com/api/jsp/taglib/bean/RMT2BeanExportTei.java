package com.api.jsp.taglib.bean;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class exports atrributes defined in the TLD as scripting variables
 * within the JSP that utilizes the RMT2Bean* custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2BeanExportTei extends TagExtraInfo {
    /**
     * Provides scripting variable information in order to export a variable to
     * a JSP client. The name of the variable is targeted by the property, bean,
     * and the variable type is targted by the property, beanClassId property.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] rc = new VariableInfo[1];
        String beanName = null;
        String beanClassName = null;

        beanName = (String) data.getAttribute("bean");
        beanClassName = (String) data.getAttribute("beanClassId") == null ? "java.lang.Object"
                : (String) data.getAttribute("beanClassId");

        rc[0] = new VariableInfo(beanName, beanClassName, true,
                VariableInfo.AT_BEGIN);
        return rc;
    }
}
