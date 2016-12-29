package com.api.ldap.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchControls;

import com.api.ldap.LdapFilterPlaceholderException;
import com.api.ldap.LdapHelper;

/**
 * A class used for collecting parameters that is used for LDAP Compare
 * opertaions
 * 
 * @author rterrell
 * 
 */
public class LdapCompareOperation extends LdapOperation {

    /**
     * The search scope. For this class, this property is forced to be of Object
     * scope. Used as a property of JNDI SearchControls object.
     */
    private int scope;

    /**
     * The number of milliseconds to wait before a search opertaion returns.
     * Used as a property of JNDI SearchControls object.
     */
    private int timeLimit;

    /**
     * Indicates whether an attribute value is bound to the name of an entry.
     * Used as a property of JNDI SearchControls object.
     */
    private List<String> retAttributes;

    /**
     * The serach filter expression
     */
    private String searchFilter;

    /**
     * A Map of aruguments used to build or create a filter expression for the
     * JNDI query.
     */
    private Map<String, String> searchFilterArgs;

    private List<String> searchFilterPlaceholders;

    /**
     * Indicates whether or not the client has declared use a search filter for
     * attribute matching.
     */
    private boolean useSearchFilterExpression;

    /**
     * The fully qualified class name of the java bean used to map LDAP data.
     */
    private String mappingBeanName;

    /**
     * Default constructor with a default of search scope of "object" and an
     * empty return attribute list.
     */
    public LdapCompareOperation() {
        super();
        this.scope = SearchControls.OBJECT_SCOPE;
        this.searchFilterArgs = new HashMap<String, String>();
        this.searchFilterPlaceholders = new ArrayList<String>();
        this.retAttributes = new ArrayList<String>();
        this.useSearchFilterExpression = false;
    }

    /**
     * Returns the searching scope.
     * <p>
     * The following is a list of valid values:
     * <ul>
     * <li>Base Object - 0</li>
     * <li>Single Level - 1</li>
     * <li>Whole Subtree - 2</li>
     * </ul>
     * 
     * @return the scope
     */
    public int getScope() {
        return scope;
    }

    /**
     * Return value for the maximum number of seconds a query can take.
     * 
     * @return the timeLimit
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Set value for the maximum number of seconds a query can take.
     * <p>
     * A number of zero means that the client does not impose any time limit.
     * 
     * @param timeLimit
     *            the timeLimit to set
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * Return a List of attributes that should be reutned when the search filter
     * matches
     * <p>
     * This list can be used as a stand alone parameter for searches utilizing
     * matching attributes for filtering or as a property of JNDI SearchControls
     * object.
     * 
     * @return the retAttributes
     */
    public List<String> getRetAttributes() {
        return retAttributes;
    }

    /**
     * Return the filter expression that is used for the search.
     * 
     * @return the searchFilter
     */
    public String getSearchFilter() {
        if (this.searchFilter == null) {
            try {
                LdapHelper.buildFilterArgs(this);
            } catch (LdapFilterPlaceholderException e) {
                this.searchFilter = null;
            }
        }
        return searchFilter;
    }

    /**
     * Set the filter expression that is used for the search.
     * 
     * @param searchFilter
     *            the searchFilter to set
     */
    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    /**
     * Return the List of String argument values used to subtitute for the
     * varibales in a filter expression.
     * 
     * @return Map <String, String>
     */
    public Map<String, String> getSearchFilterArgs() {
        return searchFilterArgs;
    }

    /**
     * Set the List of String argument values used to subtitute for the
     * varibales in a filter expression.
     * 
     * @param searchFilterArgs
     *            Map <String, String>
     */
    public void setSearchFilterArgs(Map<String, String> searchFilterArgs) {
        this.searchFilterArgs = searchFilterArgs;
    }

    /**
     * @return the useSearchFilterExpression
     */
    public boolean isUseSearchFilterExpression() {
        return useSearchFilterExpression;
    }

    /**
     * @param useSearchFilterExpression
     *            the useSearchFilterExpression to set
     */
    public void setUseSearchFilterExpression(boolean useSearchFilterExpression) {
        this.useSearchFilterExpression = useSearchFilterExpression;
    }

    /**
     * @return the mappingBeanName
     */
    public String getMappingBeanName() {
        return mappingBeanName;
    }

    /**
     * @param mappingBeanName
     *            the mappingBeanName to set
     */
    public void setMappingBeanName(String mappingBeanName) {
        this.mappingBeanName = mappingBeanName;
    }

    /**
     * Return List of values to be used replace the place holders in search
     * filter expression
     * 
     * @return a List of Strings
     */
    public List<String> getSearchFilterPlaceholders() {
        return searchFilterPlaceholders;
    }

    /**
     * Set List of values to be used replace the place holders in search filter
     * expression
     * 
     * @param searchFilterPlaceholders
     *            a List of String values
     */
    public void setSearchFilterPlaceholders(
            List<String> searchFilterPlaceholders) {
        this.searchFilterPlaceholders = searchFilterPlaceholders;
    }

}
