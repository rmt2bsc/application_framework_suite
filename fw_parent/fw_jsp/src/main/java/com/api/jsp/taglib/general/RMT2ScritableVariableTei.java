package com.api.jsp.taglib.general;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class exports a custom tag attribute declared as, <i>id</i>, into the
 * JSP as a scripting variable.&nbsp;&nbsp;<i>id</> is required to be defined in
 * the TLD as an attribute. This applies to most attributes that are common to
 * most custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ScritableVariableTei extends TagExtraInfo {
    protected String attr;

    protected String className;

    /**
     * Provides scripting variable information in order to export the variable,
     * classId, to a JSP client.
     * 
     * @param data
     *            {@link TagData} return {@link VariableInfo}[]
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] rc = new VariableInfo[1];
        this.attr = data.getId();
        this.className = "java.lang.String";
        this.addMoreVariables(data);
        rc[0] = new VariableInfo(this.attr, this.className, true, VariableInfo.AT_BEGIN);
        return rc;
    }

    /**
     * Override to add custom attribute initializations.
     * 
     * @param data
     *            {@link TagData}
     */
    protected void addMoreVariables(TagData data) {
        return;
    }
}
