package com.api.persistence.db.orm;

import java.util.Hashtable;

/**
 * Class containing DataSource related constants.
 * 
 * @author roy.terrell
 * 
 */
public class DataSourceConst {

    /** Text Lookup type */
    public static final int LOOKUPTYPE_TEXT = 1;

    /** Lookup type for HTML Input text types */
    public static final int LOOKUPTYPE_INPUT_TEXT = 2;

    /** Lookup type for HTML Select input types */
    public static final int LOOKUPTYPE_DROPDOWN = 3;

    /** Lookup type for HTML radio button input types */
    public static final int LOOKUPTYPE_RADIO = 4;

    /** Hashtable of key/value pair lookup types */
    public static final Hashtable LOOKUP_TYPES = new Hashtable();
    static {
        LOOKUP_TYPES.put(new Integer(LOOKUPTYPE_TEXT),
                "Non-Updatable Single Value");
        LOOKUP_TYPES.put(new Integer(LOOKUPTYPE_INPUT_TEXT),
                "HTML Input Text Control");
        LOOKUP_TYPES.put(new Integer(LOOKUPTYPE_DROPDOWN),
                "HTML Select Control");
        LOOKUP_TYPES.put(new Integer(LOOKUPTYPE_RADIO), "HTML Radio Buttons");
    }
    /**
     * The xml tag that is to be used as the root of a xml document derived from
     * a SQL query
     */
    public static final String XML_TAG_START = "<dataitem>";

    /**
     * The xml tag that is to be used as the end of a xml document derived from
     * a SQL query
     */
    public static final String XML_TAG_END = "</dataitem>";
}