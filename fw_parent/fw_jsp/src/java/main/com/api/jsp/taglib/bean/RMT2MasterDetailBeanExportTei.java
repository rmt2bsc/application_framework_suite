package com.api.jsp.taglib.bean;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class exports atrributes defined in the TLD as scripting variables
 * within the JSP that utilizes the RMT2ForEachmasterDeail custom tag.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2MasterDetailBeanExportTei extends TagExtraInfo {

    /**
     * Provides scripting variable information in order to export the
     * attributes, masterItemName and masterItemClassId, as variables to a JSP
     * client. Both variables are declared as a String data type.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] rc = new VariableInfo[1];
        String beanName = null;
        String beanClassName = null;

        beanName = (String) data.getAttribute("masterItemName");
        beanClassName = (String) data.getAttribute("masterItemClassId") == null ? "java.lang.Object"
                : (String) data.getAttribute("masterItemClassId");

        rc[0] = new VariableInfo(beanName, beanClassName, true,
                VariableInfo.AT_BEGIN);
        return rc;
    }
}
