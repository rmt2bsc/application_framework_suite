package com.api.ldap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.NotFoundException;
import com.SystemException;
import com.api.ldap.operation.LdapAddOperation;
import com.api.ldap.operation.LdapAttributeOperationFailureException;
import com.api.ldap.operation.LdapCompareOperation;
import com.api.ldap.operation.LdapDeleteOperation;
import com.api.ldap.operation.LdapModifyOperation;
import com.api.ldap.operation.LdapSearchOperation;
import com.api.persistence.DatabaseException;
import com.util.RMT2BeanUtility;

/**
 * Concrete class for providing common functionality to interrogate, update,
 * add, and remove non-serializable and non-referenceable objects that reside as
 * stored attriutes in the LDAP repository.
 * <p>
 * 
 * @author rterrell
 * 
 */
class StoredAttributeClientImpl extends AbstractLdapAuthenticationImpl
        implements LdapClient {

    private static final Logger logger = Logger
            .getLogger(StoredAttributeClientImpl.class);

    private List<Object> results;

    private ListIterator<Object> cursorList;

    private Object currentItem;

    /**
     * Creates a RMT2AbstractLdapImpl without having any assoication with the
     * LDAP server.
     */
    public StoredAttributeClientImpl() {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#deleteRow(java.lang.Object)
     */
    public int deleteRow(Object opParms) throws DatabaseException {
        try {
            this.validateUpdateOperation(opParms);
        } catch (InvalidDataException e) {
            throw new RMT2LdapException(e);
        }
        int rc = 0;
        if (opParms instanceof LdapDeleteOperation) {
            LdapDeleteOperation op = (LdapDeleteOperation) opParms;
            rc = (Integer) this.doDeleteOperation(op);
        }
        return rc;
    }

    private Object doDeleteOperation(LdapDeleteOperation opParms)
            throws RMT2LdapException {
        String dn = this.buildDistinguishedName(opParms.getDn());
        try {
            this.getDirContext().unbind(dn);
            return 1;
        } catch (NamingException e) {
            throw new RMT2LdapException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#insertRow(java.lang.Object, boolean)
     */
    public int insertRow(Object opParms, boolean autoKey)
            throws DatabaseException {
        try {
            this.validateUpdateOperation(opParms);
        } catch (InvalidDataException e) {
            throw new RMT2LdapException(e);
        }

        if (opParms instanceof LdapAddOperation) {
            LdapAddOperation op = (LdapAddOperation) opParms;
            this.doAddOperation(op);
        }
        return 1;
    }

    private Object doAddOperation(LdapAddOperation opParms)
            throws RMT2LdapException {
        String dn = this.buildDistinguishedName(opParms.getDn());
        Attributes a = opParms.buildAddEntryAttributes();
        try {
            this.getDirContext().bind(dn, null, a);
            return null;
        } catch (NamingException e) {
            throw new RMT2LdapException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#updateRow(java.lang.Object)
     */
    public int updateRow(Object opParms) throws DatabaseException {
        try {
            this.validateUpdateOperation(opParms);
        } catch (InvalidDataException e) {
            throw new RMT2LdapException(e);
        }

        if (opParms instanceof LdapModifyOperation) {
            LdapModifyOperation op = (LdapModifyOperation) opParms;
            this.doModifyOperation(op);
        }
        return 1;
    }

    private Object doModifyOperation(LdapModifyOperation opParms)
            throws RMT2LdapException {
        String dn = this.buildDistinguishedName(opParms.getDn());
        ModificationItem[] mods = null;
        try {
            mods = opParms.buildModificationEntryAttributes();
            this.getDirContext().modifyAttributes(dn, mods);
            return mods;
        } catch (NamingException e) {
            throw new RMT2LdapException(e);
        } catch (LdapAttributeOperationFailureException e) {
            this.msg = "Error setting up a Modify Operation object";
            throw new RMT2LdapException(this.msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#findData(java.lang.Object)
     */
    public Object[] findData(Object opParms) throws DatabaseException {
        return this.retrieve(opParms);
    }

    /**
     * Retrieves data from the LDAP server using via either a Search operation
     * or a Compare operation and returns the result as an arbitary Java object.
     * 
     * @param opParms
     *            A reference to either a {@link LdapSearchOperation} or
     *            {@link LdapCompareOperation} which contains the data needed to
     *            issue a query to LDAP.
     * @return Object[] An array of Object containing only one element or null
     *         if no data is found. The lone element is a List of arbitrary
     *         objects which the runtime type of the objects are specified in
     *         the mappingBeanName property of <i>opParms</i>.
     * @throws RMT2LdapException
     */
    public Object[] retrieve(Object opParms) throws RMT2LdapException {
        try {
            this.validateRetrieveOperation(opParms);
        } catch (InvalidDataException e) {
            throw new RMT2LdapException(e);
        }

        // Perform a Search or Compare LDAP operation
        Object results = null;
        if (opParms instanceof LdapSearchOperation) {
            LdapSearchOperation op = (LdapSearchOperation) opParms;
            results = this.doSearchOperation(op);
        }
        else {
            LdapCompareOperation op = (LdapCompareOperation) opParms;
            results = this.doCompareOperation(op);
        }

        if (results == null) {
            return null;
        }
        // Package and return the results
        Object data[] = new Object[1];
        data[0] = results;
        return data;
    }

    /**
     * Tests for the presence of a particular attribute in an entry with a given
     * distinguished name in the directory.
     * <p>
     * Unlike the "doSearchOpeation", this method does not return any element.
     * Instead it reports "true" when a match is found and "false" when a match
     * is not found. The LDAP "compare" operatio allows a client to aske the
     * server whether the named entry has a attribute/value pare. This allows
     * the server to keep certain attribute/value pairs secret. To accomplish
     * this in JNDI, use suitably constrained arguments for the search filter
     * expression related search methods.
     * <p>
     * First, the filte must fo the form "(name=vlaue)". Second, the serach
     * scope must of Object scope. Finally, you must request that no attributes
     * be returned.
     * 
     * @param opParms
     * @return boolean true when a match is found and false if a match is not
     *         found.
     * @throws RMT2LdapException
     */
    private boolean doCompareOperation(LdapCompareOperation opParms)
            throws RMT2LdapException {
        boolean rc = false;
        String dn = this.buildDistinguishedName(opParms.getDn());
        Object filterArgs[] = null;
        SearchControls ctrl = null;

        // Build the appropriate JNDI paramters for the search method call based
        // on the "parms" parameter.
        ctrl = this.buildSearchControls(opParms);
        this.buildReturnAttributes(opParms.getRetAttributes());
        try {
            filterArgs = LdapHelper.buildFilterArgs(opParms);
        } catch (LdapFilterPlaceholderException e) {
            throw new RMT2LdapException(e);
        }

        // Prepare to search
        DirContext ctx = this.getDirContext();
        NamingEnumeration ldapResults = null;

        // Invoke the approripate "search" method.
        try {
            if (filterArgs == null) {
                ldapResults = ctx.search(dn, opParms.getSearchFilter(), ctrl);
            }
            else {
                ldapResults = ctx.search(dn, opParms.getSearchFilter(),
                        filterArgs, ctrl);
            }
        } catch (NamingException e) {
            this.msg = "Unable to perform LDAP search for entry, " + dn
                    + ", due to a directory naming error";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }

        // Map results to Java bean
        List<Object> list = this.mapSearchResults(ldapResults,
                opParms.getMappingBeanName());
        if (list == null || list.size() == 0) {
            rc = false;
        }
        else {
            rc = true;
        }
        return rc;
    }

    /**
     * Looks up a part of the LDAP directory for entries matching a condition
     * specified in <i>opParms</i> using a LDAP Search operation.
     * <p>
     * It is possible to specify how deep the search should traverse the
     * directory information tree (DIT), the point in the directory where the
     * search should begin searching, the attributes that are to be returned,
     * and etc. All of this and more can be declared in the parameter,
     * <i>opParms</i>.
     * 
     * @param parms
     * @return A List of arbitrary objects which the class type is specified in
     *         the mappingBeanName property of <i>opParms</i>.
     * @throws RMT2LdapException
     */
    private Object doSearchOperation(LdapSearchOperation opParms)
            throws RMT2LdapException {
        String dn = this.buildDistinguishedName(opParms.getDn());
        Attributes matchAttr = null;
        String retAttr[] = null;
        Object filterArgs[] = null;
        SearchControls ctrl = null;

        // Build the appropriate JNDI paramters for the search method call based
        // on the "parms" parameter.
        if (opParms.isUseSearchFilterExpression()) {
            ctrl = this.buildSearchControls(opParms);
        }
        else {
            matchAttr = this.buildMatchingAttributes(opParms
                    .getMatchAttributes());
        }
        if (opParms.getRetAttributes().size() > 0) {
            retAttr = this.buildReturnAttributes(opParms.getRetAttributes());
        }
        if (opParms.getSearchFilterArgs().size() > 0) {
            try {
                filterArgs = LdapHelper.buildFilterArgs(opParms);
            } catch (LdapFilterPlaceholderException e) {
                throw new RMT2LdapException(e);
            }
        }

        // Prepare to search
        DirContext ctx = this.getDirContext();
        NamingEnumeration ldapResults = null;

        // Invoke the approripate "search" method.
        logger.info("Query LDAP server using DN: " + dn);
        try {
            if (opParms.isUseSearchFilterExpression()) {
                String filter = opParms.getSearchFilter() == null ? "(objectClass=*)"
                        : opParms.getSearchFilter();
                if (filterArgs == null) {
                    ldapResults = ctx.search(dn, filter, ctrl);
                }
                else {
                    ldapResults = ctx.search(dn, filter, filterArgs, ctrl);
                }
            }
            else {
                if (retAttr == null) {
                    ldapResults = ctx.search(dn, matchAttr);
                }
                else {
                    ldapResults = ctx.search(dn, matchAttr, retAttr);
                }
            }
        } catch (NamingException e) {
            this.msg = "Unable to perform LDAP search for entry, " + dn
                    + ", due to a directory naming error";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }

        // Map results to Java bean
        return this.mapSearchResults(ldapResults, opParms.getMappingBeanName());
    }

    /**
     * Creates an Attributes instance fromt he matching attributes property of
     * <i>attr</i>.
     * 
     * @param attr
     * @return
     */
    private Attributes buildMatchingAttributes(Map<String, String> attr) {
        Attributes a = new BasicAttributes(true); // ignore attribute name case
        Iterator<String> keys = attr.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            String val = attr.get(key);
            a.put(new BasicAttribute(key, val));
        }
        return a;
    }

    /**
     * Creates a String array of attribute names from <i>attr</i> for the
     * purpose of identifying to the Search opertaion the attributes that are to
     * be returned.
     * 
     * @param attr
     * @return
     */
    private String[] buildReturnAttributes(List<String> attr) {
        String a[] = new String[attr.size()];
        attr.toArray(a);
        return a;
    }

    // /**
    // * Builds an Object array of values that will substitute the variables or
    // place holders contained
    // * in search filter expression.
    // *
    // * @param parms
    // * an instance of {@link LdapCompareOperation} containing a Map of filter
    // arguments
    // * @return int value representing the total number of arguments processed.
    // */
    // private Object[] buildFilterArgs(LdapCompareOperation parms) throws
    // LdapFilterPlaceholderException {
    // Map<String, String> args = parms.getSearchFilterArgs();
    // int totArgs = args.size();
    // int placeHolderCount = 0;
    // Iterator<String> keys = args.keySet().iterator();
    // StringBuffer buf = new StringBuffer();
    // if (totArgs > 1) {
    // buf.append("(&");
    // }
    // while (keys.hasNext()) {
    // String key = keys.next();
    // String val = args.get(key);
    // if (LdapHelper.isValidPlaceHolder(val)) {
    // placeHolderCount++;
    // }
    // buf.append("(");
    // buf.append(key);
    // buf.append("=");
    // buf.append(val);
    // buf.append(")");
    // }
    // String filter = null;
    // if (totArgs > 1) {
    // filter = "(&" + buf + ")";
    // }
    //
    // // Set serach filter
    // parms.setSearchFilter(filter);
    //
    // // Verify that total number of place holders found equal the total number
    // of place holder values
    // if (parms.getSearchFilterPlaceholders() != null &&
    // parms.getSearchFilterPlaceholders().size() > 0) {
    // if (placeHolderCount > 0) {
    // if (placeHolderCount != parms.getSearchFilterPlaceholders().size()) {
    // throw new
    // LdapFilterPlaceholderException("Total number of place holder does not match the total number substitution arguments");
    // }
    // // Create search argument substitute values
    // Object a[] = new Object[parms.getSearchFilterPlaceholders().size()];
    // parms.getSearchFilterPlaceholders().toArray(a);
    // return a;
    // }
    // }
    // return null;
    // }

    /**
     * Builds an instance of SearchControls from <i>opParms</i> for a Search
     * operation.
     * <p>
     * This object will be used to control the results of search operation that
     * uses a search filter expression to match attributes in the directory.
     * 
     * @param op
     * @return
     */
    private SearchControls buildSearchControls(LdapSearchOperation opParms) {
        SearchControls ctrl = new SearchControls();
        ctrl.setCountLimit(opParms.getSizeLimit());
        ctrl.setTimeLimit(opParms.getTimeLimit());
        ctrl.setSearchScope(opParms.getScope());
        ctrl.setReturningObjFlag(opParms.isAttrOnly());
        ctrl.setDerefLinkFlag(opParms.isDerefAliases());

        // Prevent a Compare operation here. When the String array is empty, it
        // is possible
        // that the entry is found, but will report null values for its
        // properties.
        if (!opParms.getRetAttributes().isEmpty()) {
            ctrl.setReturningAttributes(this.buildReturnAttributes(opParms
                    .getRetAttributes()));
        }
        return ctrl;
    }

    /**
     * Builds an instance of SearchControls from <i>opParms</i> for a Compare
     * operation.
     * <p>
     * This object will be used to control the results of the compare operation
     * that uses a search filter expression to match attributes in the
     * directory.
     * 
     * @param opParms
     * @return
     */
    private SearchControls buildSearchControls(LdapCompareOperation opParms) {
        SearchControls ctrl = new SearchControls();
        ctrl.setTimeLimit(opParms.getTimeLimit());
        // The search scope is required to be "Object" at all times.
        ctrl.setSearchScope(opParms.getScope());
        // This should always return an empty array in order to force the values
        // to be reported as null.
        ctrl.setReturningAttributes(this.buildReturnAttributes(opParms
                .getRetAttributes()));
        return ctrl;
    }

    /**
     * Validates the Search/Compare LDAP parameters represented as
     * <i>opParms</i>.
     * <p>
     * 
     * @param opParms
     * @throws InvalidDataException
     */
    protected void validateRetrieveOperation(Object opParms)
            throws InvalidDataException {
        // perform base validations.
        super.validateOperation(opParms);

        boolean validType = (opParms instanceof LdapSearchOperation || opParms instanceof LdapCompareOperation);
        if (!validType) {
            this.msg = "LDAP Search/Compare operation parameter object must be of type: "
                    + LdapSearchOperation.class.getName()
                    + " or "
                    + LdapCompareOperation.class.getName();
            logger.error(this.msg);
            throw new InvalidDataException(this.msg);
        }

        if (opParms instanceof LdapSearchOperation) {
            LdapSearchOperation searchOp = null;
            searchOp = (LdapSearchOperation) opParms;

            // Verify that the client has specified the ability to search for
            // one or more attributes.
            if (searchOp.getMatchAttributes().isEmpty()
                    && searchOp.getSearchFilter() == null) {
                // User did not supply any search options or fileters
                return;

                // this.msg =
                // "LDAP Search operation parameter object must possess election criteria identified as either matching attributes map or search filter String";
                // logger.error(this.msg);
                // throw new InvalidDataException(this.msg);
            }

            // Verify that the class name of the mapping java bean has been
            // included.
            if (searchOp.getMappingBeanName() == null) {
                this.msg = "LDAP Search operation parameter object must declare the name of the Java class that will hold the LDAP data results";
                logger.error(this.msg);
                throw new InvalidDataException(this.msg);
            }
            // Flag which attribute matching method to use.
            if (!searchOp.getMatchAttributes().isEmpty()
                    && searchOp.getSearchFilter() != null) {
                // Select search filter when both types of attribute matching
                // mechanisms have been delcared.
                searchOp.setUseSearchFilterExpression(true);
            }
            if (!searchOp.getMatchAttributes().isEmpty()
                    && searchOp.getSearchFilter() == null) {
                searchOp.setUseSearchFilterExpression(false);
            }
            if (searchOp.getMatchAttributes().isEmpty()
                    && searchOp.getSearchFilter() != null) {
                searchOp.setUseSearchFilterExpression(true);
            }
        }
        else if (opParms instanceof LdapCompareOperation) {
            LdapCompareOperation compareOp = null;
            compareOp = (LdapCompareOperation) opParms;

            if (compareOp.getSearchFilter() == null) {
                this.msg = "LDAP compare operation requires the usage of a search filter express";
                logger.error(this.msg);
                throw new InvalidDataException(this.msg);
            }
            if (compareOp.getScope() != SearchControls.OBJECT_SCOPE) {
                this.msg = "LDAP compare operation requires that the search scope is Object";
                logger.error(this.msg);
                throw new InvalidDataException(this.msg);
            }
            if (!compareOp.getRetAttributes().isEmpty()) {
                this.msg = "LDAP Compare operation is required to request that no attributes are returned";
                logger.error(this.msg);
                throw new InvalidDataException(this.msg);
            }
            compareOp.setUseSearchFilterExpression(true);
        }
    }

    protected void validateUpdateOperation(Object opParms)
            throws InvalidDataException {
        // perform base validations.
        super.validateOperation(opParms);

        if (opParms instanceof LdapAddOperation) {

        }
    }

    /**
     * Creates a List of arbitrary objects from a LDAP search resultset.
     * 
     * @param results
     * @param beanName
     * @return
     * @throws RMT2LdapException
     */
    protected List mapSearchResults(NamingEnumeration searchResults,
            String beanName) throws RMT2LdapException {
        // validate the input parameters
        if (searchResults == null) {
            this.msg = "The NamingEnumeration instance, which represents the LDAP search results, cannot be null";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }
        if (beanName == null) {
            this.msg = "The name of the mapping bean that is to be associated with the LDAP search results cannot be blank or null";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }

        // Begin to map results
        try {
            if (!searchResults.hasMore()) {
                this.msg = "There were no entries to process from the LDAP NamngEnumeration results collection";
                logger.warn(this.msg);
                return null;
            }
            List list = new ArrayList();
            while (searchResults.hasMore()) {
                SearchResult result = (SearchResult) searchResults.next();
                Object bean = this.mapSearchResults(result, beanName);
                list.add(bean);
            }
            this.results = list;
            this.cursorList = list.listIterator();
            return list;
        } catch (NamingException e) {
            this.msg = "Unable to map LDAP search results due to general directory naming error";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }
    }

    /**
     * Maps the contents of a directory/LDAP SearchResult instance to an
     * arbitrary object.
     * <p>
     * An instance of the arbitrary object is dynamically created using the
     * fully qualified class name, <i>beanName</i>.
     * 
     * @param item
     * @param beanName
     * @return
     * @throws RMT2LdapException
     */
    protected Object mapSearchResults(SearchResult item, String beanName)
            throws RMT2LdapException {
        RMT2BeanUtility beanUtil = new RMT2BeanUtility();
        Object bean = null;
        try {
            bean = beanUtil.createBean(beanName);
        } catch (SystemException e) {
            this.msg = "Error occurred trying to create instance of "
                    + beanName + " in order to map LDAP search results";
            logger.error(this.msg);
            throw new RMT2LdapException(this.msg, e);
        }
        this.mapSearchResults(item, bean);
        return bean;
    }

    /**
     * Maps the contents of a directory/LDAP SearchResult instance to an
     * arbitrary object.
     * 
     * @param result
     * @param bean
     * @throws RMT2LdapException
     */
    protected void mapSearchResults(SearchResult item, Object bean)
            throws RMT2LdapException {
        Attributes nameValuePairs;
        Attribute attribute;
        String setMethodName;

        if (item == null) {
            this.msg = "LDAP to Bean mapping failed due to invalid search result item";
            StoredAttributeClientImpl.logger.error(this.msg);
            throw new RMT2LdapException(this.msg);
        }
        nameValuePairs = item.getAttributes();

        Object[] beanParams = null;
        Class partypes[] = new Class[1];

        NamingEnumeration nEnum = nameValuePairs.getAll();

        try {
            while (nEnum.hasMore()) {
                attribute = (Attribute) nEnum.next();
                logger.debug("Attribute: " + attribute.getID());
                boolean multiValueAttr = false;

                try {
                    DirContext def = attribute.getAttributeDefinition();
                    Attributes prop = def.getAttributes("");
                    Attribute a = prop.get("SINGLE-VALUE");
                    multiValueAttr = (a == null);
                } catch (NameNotFoundException e) {
                    // LDAP vendor probably does not support obtaining
                    // definition of attribute.
                    this.msg = "The following attribute was not found in LDAP schema: "
                            + attribute.getID();
                    logger.warn(this.msg);
                }

                // Skip processing attribute if it is encryted with algorithms
                // such as sha or ssha
                if (attribute.getID().toLowerCase().indexOf("password") > -1) {
                    continue;
                }
                setMethodName = "set"
                        + attribute.getID().substring(0, 1).toUpperCase()
                        + attribute.getID().substring(1);
                // System.out.println(attribute);//x
                if (attribute != null) {
                    int attrValCount = attribute.size();
                    if (!multiValueAttr) {
                        // Handle single-value attribute
                        beanParams = new Object[1];
                        beanParams[0] = attribute.get();
                        partypes[0] = String.class;
                    }
                    else {
                        // Handle multi-value attribute
                        List<String> list = new ArrayList<String>();
                        for (int ndx = 0; ndx < attrValCount; ndx++) {
                            list.add((String) attribute.get(ndx));
                        }
                        beanParams = new Object[1];
                        beanParams[0] = list;
                        partypes[0] = List.class;
                    }

                    try {
                        Method setMethod = bean.getClass().getMethod(
                                setMethodName, partypes);
                        setMethod.invoke(bean, beanParams);
                    } catch (IllegalAccessException iae) {
                        logger.warn("Method could not invoked due to Illegal Access Error: "
                                + setMethodName);
                    } catch (InvocationTargetException ite) {
                        logger.warn("Method could not invoked due to Inovication Target Error: "
                                + setMethodName);
                    } catch (NoSuchMethodException nsme) {
                        logger.warn("Method could not invoked due to it does not exists: "
                                + setMethodName);
                    }
                }
            }
        } catch (NamingException ne) {
            this.msg = "A Naming error occurred processing LDAP bean mapping request";
            StoredAttributeClientImpl.logger.error(this.msg);
            throw new RMT2LdapException(this.msg, ne);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#firstRow()
     */
    public boolean firstRow() throws DatabaseException, SystemException {
        if (this.results == null) {
            return false;
        }
        this.cursorList = this.results.listIterator();
        return this.nextRow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#lastRow()
     */
    public boolean lastRow() throws DatabaseException, SystemException {
        if (this.results == null) {
            return false;
        }

        // TODO: Think about creating a Thread to position List to the last
        // element
        // Go Forward
        if (this.cursorList.hasNext()) {
            try {
                for (;;) {
                    this.cursorList.next();
                }
            } catch (NoSuchElementException e) {
                // Should be at the end of list
            }
        }
        return this.previousRow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#nextRow()
     */
    public boolean nextRow() throws DatabaseException, SystemException {
        if (this.results == null) {
            return false;
        }
        if (this.cursorList.hasNext()) {
            this.currentItem = this.cursorList.next();
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#previousRow()
     */
    public boolean previousRow() throws DatabaseException, SystemException {
        if (this.results == null) {
            return false;
        }
        if (this.cursorList.hasPrevious()) {
            this.currentItem = this.cursorList.previous();
            return true;
        }
        return false;
    }

    /**
     * @return the currentItem
     */
    public Object getCurrentItem() {
        return currentItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApiStub#getColumnValue(java.lang.String)
     */
    public String getColumnValue(String attrName) throws DatabaseException,
            NotFoundException, SystemException {
        if (this.currentItem == null) {
            return null;
        }
        RMT2BeanUtility util = new RMT2BeanUtility(this.currentItem);
        Object val = util.getPropertyValue(attrName);
        return val.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#getColumnBinaryValue(java.lang.String)
     */
    public Object getColumnBinaryValue(String property)
            throws DatabaseException, NotFoundException, SystemException {
        // TODO Auto-generated method stub
        return null;
    }
}
