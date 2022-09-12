package com.api.jsp.taglib.general;

import javax.servlet.jsp.tagext.TagData;

/**
 * This class exports a custom tag atrribute declared as, <i>id</i>, into the
 * JSP as a scripting variable.&nbsp;&nbsp; <i>id</> is required to be defined
 * in the TLD as an attribute. This applies to most attributes that are common
 * to most custom tags.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ScritableSessionBeanTei extends RMT2ScritableVariableTei {

    /**
     * Sets the class name for the Session Bean.
     * 
     * @param data
     *            {@link TagData}
     */
    protected void addMoreVariables(TagData data) {
        this.className = "com.api.web.security.RMT2SessionBean";
        return;
    }
}
