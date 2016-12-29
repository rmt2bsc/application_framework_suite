package com.api.ldap;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.InvalidDataException;
import com.SystemException;
import com.api.ldap.operation.LdapBindOperation;
import com.api.persistence.DatabaseException;

/**
 * Abstract class for providing common functionality to interrogate, update,
 * add, and remove serializable and/or referenceable objects that reside as
 * stored Java objects in the LDAP repository.
 * <p>
 * 
 * @author rterrell
 * 
 */
class StoredObjectClientImpl extends AbstractLdapAuthenticationImpl implements
        LdapClient {

    private static Logger logger;

    private List<Object> results;

    private ListIterator<Object> cursorList;

    private Object currentObject;

    /**
     * Creates a RMT2AbstractLdapImpl without having any assoication with the
     * LDAP server.
     */
    public StoredObjectClientImpl() {
        logger = Logger.getLogger(StoredObjectClientImpl.class);
        logger.info("Logger is initialized");
        return;
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
            this.currentObject = this.cursorList.next();
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
            this.currentObject = this.cursorList.previous();
            return true;
        }
        return false;
    }

    /**
     * @return the currentObject
     */
    public Object getCurrentObject() {
        return currentObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#findData(java.lang.Object)
     */
    public Object[] findData(Object ormBean) throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#retrieve(java.lang.Object)
     */
    public Object[] retrieve(Object obj) throws DatabaseException {
        try {
            this.validateOperation(obj);
        } catch (InvalidDataException e) {
            throw new DatabaseException(e);
        }
        LdapBindOperation op = (LdapBindOperation) obj;
        try {
            Object ldapObj = this.getDirContext().lookup(op.getDn());
            Object results[] = new Object[1];
            results[0] = ldapObj;
            return results;
        } catch (NamingException e) {
            throw new RMT2LdapException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#insertRow(java.lang.Object, boolean)
     */
    public int insertRow(Object obj, boolean autoKey) throws DatabaseException {
        try {
            this.validateOperation(obj);
        } catch (InvalidDataException e) {
            throw new DatabaseException(e);
        }
        LdapBindOperation op = (LdapBindOperation) obj;
        try {
            this.getDirContext().bind(op.getDn(), op.getLdapObject());
            return 1;
        } catch (NamingException e) {
            throw new RMT2LdapException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#updateRow(java.lang.Object)
     */
    public int updateRow(Object obj) throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.DaoApi#deleteRow(java.lang.Object)
     */
    public int deleteRow(Object obj) throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Validates <i>opParms</i> as a LdapBindOperation instance.
     * 
     * @param opParms
     *            an object that should resolve to an instance of
     *            LdapBindOperation.
     * 
     * @throws InvalidDataException
     *             <i>opParms</i> is null, or does not contain a DN, or is not
     *             of type LdapBindOperation.
     */
    @Override
    protected void validateOperation(Object opParms)
            throws InvalidDataException {
        super.validateOperation(opParms);
        if (!(opParms instanceof LdapBindOperation)) {
            this.msg = "LDAP bind operation instance must be of type, "
                    + LdapBindOperation.class.getName();
            logger.error(this.msg);
            throw new InvalidDataException(this.msg);
        }
    }
}
