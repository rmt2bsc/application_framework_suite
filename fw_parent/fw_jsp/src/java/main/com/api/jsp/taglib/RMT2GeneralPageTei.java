package com.api.jsp.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class exports atrributes defined in the TLD as scripting variables
 * within the JSP. This applies to most attributes that are common to most
 * custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2GeneralPageTei extends TagExtraInfo {

    /**
     * Provides scripting variable information in order to export the variable,
     * classId, to a JSP client.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] rc = new VariableInfo[1];
        rc[0] = new VariableInfo(data.getId(),
                (String) data.getAttribute("classId"), true,
                VariableInfo.AT_BEGIN);
        return rc;
    }
}
