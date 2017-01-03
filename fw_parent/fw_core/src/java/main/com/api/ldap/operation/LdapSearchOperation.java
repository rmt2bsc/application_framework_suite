package com.api.ldap.operation;

import java.util.Hashtable;
import java.util.Map;

/**
 * A class used for collecting parameters that is used for LDAP Search
 * opertaions
 * 
 * @author rterrell
 * 
 */
public class LdapSearchOperation extends LdapCompareOperation {

    /**
     * The search scope. Used as a property of JNDI SearchControls object.
     */
    private int scope;

    /**
     * Indicates if links should be dereferenced for searches. Used as a
     * property of JNDI SearchControls object.
     */
    private boolean derefAliases;

    /**
     * The maximum number of entries to return. Used as a property of JNDI
     * SearchControls object.
     */
    private long sizeLimit;

    /**
     * The identifiers of the attributes to return along with the entry. Used as
     * a property of JNDI SearchControls object.
     */
    private boolean attrOnly;

    /**
     * A List of attributes to search for.
     */
    private Map<String, String> matchAttributes;

    /**
     * Default constructor
     */
    public LdapSearchOperation() {
        super();
        this.matchAttributes = new Hashtable<String, String>();
        this.setSizeLimit(0);
        this.setDerefAliases(false);
        this.setAttrOnly(false);
    }

    /**
     * Return alias dereferencing flag
     * 
     * @return true when searh is to dereference links, and false otherwise.
     */
    public boolean isDerefAliases() {
        return derefAliases;
    }

    /**
     * Set flag to indicate how alias dereferencing should be handled.
     * 
     * @param derefAliases
     *            the derefAliases to set
     */
    public void setDerefAliases(boolean derefAliases) {
        this.derefAliases = derefAliases;
    }

    /**
     * Return value for the maximumn number of entries a query will return
     * 
     * @return the sizeLimit
     */
    public long getSizeLimit() {
        return sizeLimit;
    }

    /**
     * Set value for the maximum number of entries a query will return
     * <p>
     * A number of zero means that there is no size limit.
     * 
     * @param sizeLimit
     *            the sizeLimit to set
     */
    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    /**
     * Return the flag that indicates how the attribute list is returned.
     * 
     * @return true for attribute types only; false for attribute types and
     *         attribute values.
     */
    public boolean isAttrOnly() {
        return attrOnly;
    }

    /**
     * Set the flag that indicates how the attribute list is returned.
     * <p>
     * Set to true to return on attribute types. Set to false to return
     * attribute types and attribute values.
     * 
     * @param attrOnly
     *            the attrOnly to set
     */
    public void setAttrOnly(boolean attrOnly) {
        this.attrOnly = attrOnly;
    }

    /**
     * Return a Map of attributes that will be used build searching predicates
     * of equality regarding each name/value pair existing in the Map.
     * 
     * @return the matchAttributes
     */
    public Map<String, String> getMatchAttributes() {
        return matchAttributes;
    }

    /**
     * Set a Map of attributes that will be used build searching predicates of
     * equality regarding each name/value pair existing in the Map.
     * 
     * @param matchAttributes
     *            the matchAttributes to set
     */
    public void setMatchAttributes(Map<String, String> matchAttributes) {
        this.matchAttributes = matchAttributes;
    }

    /**
     * @return the scope
     */
    public int getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(int scope) {
        this.scope = scope;
    }

}
