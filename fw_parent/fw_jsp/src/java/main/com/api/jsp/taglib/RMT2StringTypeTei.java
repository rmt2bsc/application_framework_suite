package com.api.jsp.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class exports the implicit "id" atrribute as scripting variable within
 * the JSP. This applies to most attributes that are common to most custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2StringTypeTei extends TagExtraInfo {

    /**
     * Provides scripting variable information in order to export the variable,
     * id, to a JSP client.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] rc = new VariableInfo[1];
        rc[0] = new VariableInfo(data.getId(), "java.lang.String", true,
                VariableInfo.AT_BEGIN);
        return rc;
    }
}
