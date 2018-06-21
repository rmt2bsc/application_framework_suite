package com.api.constants;

/**
 * JSP custom tag constants.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2TagConst {
    /**
     * Code that identifies the master row id which can be reference from some
     * arbitrary data a object as a named attribute.
     */
    public static final String ROW_ID_MASTERDETAIL = "MASTERROW";

    /**
     * Code that identifies the row id which can be reference from some
     * arbitrary data a object as a named attribute.
     */
    public static final String ROW_ID_REGULAR = "ROW";

    /**
     * Reserved HTML input control name for creating and tracking row id within
     * a list of information.
     */
    public static final String HTML_ROWID = "rowid";

    /**
     * Reserved HTML input control name for creating and tracking row id within
     * lists of information presented in the form of master/detail layout.
     */
    public static final String HTML_MASTER_ROWID = "masterrowid";
}