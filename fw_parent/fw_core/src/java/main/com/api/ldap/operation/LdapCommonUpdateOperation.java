package com.api.ldap.operation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import com.api.ldap.LdapClient;

/**
 * A class providing common functionality for for collecting the attribute
 * information for the purpose of managing an entry for one of four LDAP update
 * operations.
 * 
 * @author rterrell
 * 
 */
public class LdapCommonUpdateOperation extends LdapOperation {

    /**
     * A Map of attributes that are to assoicated with the entry to be added to
     * the LDAP repository.
     */
    private Map<String, Object> attr;

    /**
     * Default constructor.
     */
    public LdapCommonUpdateOperation() {
        super();
        this.attr = new Hashtable<String, Object>();
    }

    /**
     * Returns the Map of attributes for an entry
     * 
     * @return the attr
     */
    public Map<String, Object> getAttr() {
        return attr;
    }

    /**
     * Adds a single attribute/value pair for an entry.
     * <p>
     * The update type will default to zero.
     * 
     * @param attrName
     * @param attrValue
     * @return {@link com.api.ldap.operation.LdapAttributeUpdateInfo
     *         LdapAttributeUpdateInfo}
     */
    public LdapAttributeUpdateInfo addAttribute(String attrName,
            Object attrValue) {
        return this.addAttribute(attrName, attrValue, 0);
    }

    /**
     * Adds a single attribute/value pair for an entry.
     * 
     * @param attrName
     * @param attrValue
     * @return {@link com.api.ldap.operation.LdapAttributeUpdateInfo
     *         LdapAttributeUpdateInfo}
     */
    public LdapAttributeUpdateInfo addAttribute(String attrName,
            Object attrValue, int updateType) {
        LdapAttributeUpdateInfo info = this.createAttributeInfo(attrValue,
                updateType);
        this.attr.put(attrName, info);
        return info;
    }

    /**
     * Adds a value to a multi valued attribute.
     * <p>
     * The update type will default to zero.
     * 
     * @param attrName
     * @param attrValue
     * @return List of {@link com.api.ldap.operation.LdapAttributeUpdateInfo
     *         LdapAttributeUpdateInfo}
     */
    public List<LdapAttributeUpdateInfo> addListAttribute(String attrName,
            Object attrValue) {
        return this.addListAttribute(attrName, attrValue, 0);
    }

    /**
     * Adds a value to a multi valued attribute.
     * <p>
     * If the attribte already exists, then its value is overriden by
     * <i>attrValue</i>.
     * 
     * @param attrName
     * @param attrValue
     * @return List of {@link com.api.ldap.operation.LdapAttributeUpdateInfo
     *         LdapAttributeUpdateInfo}
     */
    public List<LdapAttributeUpdateInfo> addListAttribute(String attrName,
            Object attrValue, int updateType) {
        Object curVal = this.attr.get(attrName);

        if (curVal == null) {
            curVal = new ArrayList<LdapAttributeUpdateInfo>();
        }
        List<LdapAttributeUpdateInfo> list = null;
        if (curVal instanceof List) {
            // Add attribute information to existing list
            list = (List<LdapAttributeUpdateInfo>) curVal;
        }
        else {
            // Create list, add attribute information to list, and assoicate new
            // list to attribute key
            list = new ArrayList<LdapAttributeUpdateInfo>();
        }

        // Add attribute info instance to list
        LdapAttributeUpdateInfo info = this.createAttributeInfo(attrValue,
                updateType);
        list.add(info);

        // Add attribute list to Attributes collection
        this.attr.put(attrName, list);
        return list;
    }

    /**
     * Creates an instance of LdapAttributeUpdateInfo initialized with
     * <i>attrValue</i> and <i>updateType</i>.
     * 
     * @param attrValue
     * @param updateType
     * @return {@link com.api.ldap.operation.LdapAttributeUpdateInfo
     *         LdapAttributeUpdateInfo}
     */
    protected LdapAttributeUpdateInfo createAttributeInfo(Object attrValue,
            int updateType) {
        LdapAttributeUpdateInfo info = new LdapAttributeUpdateInfo();

        // Default update type to zero if value is invalid
        switch (updateType) {
            case LdapClient.MOD_OPERATION_ADD:
            case LdapClient.MOD_OPERATION_REMOVE:
            case LdapClient.MOD_OPERATION_REPLACE:
            case LdapClient.MOD_OPERATION_NONE:
                break;
            default:
                updateType = 0;
        }
        info.setUpdateType(updateType);
        info.setValue(attrValue);
        return info;
    }

    /**
     * 
     * @param attrName
     * @param item
     * @return
     */
    protected Attribute buildSingleValueAttribute(String attrName,
            LdapAttributeUpdateInfo item) {
        if (item == null) {
            return null;
        }
        Attribute a = null;
        if (item.getUpdateType() == LdapClient.MOD_OPERATION_REMOVE) {
            // No need to include value since attribute is to be deleted from
            // LDAP entry
            a = new BasicAttribute(attrName);
        }
        else {
            a = new BasicAttribute(attrName, item.getValue());
        }
        return a;
    }

    /**
     * Creates an attribute capable of housing multiple values.
     * 
     * @param attrName
     *            The attribute name
     * @param list
     *            A List of {@link LdapAttributeUpdateInfo} objects representing
     *            the attribute values.
     * @return an instance of {@link Attribute}
     */
    protected Attribute buildMultiValueAttribte(String attrName,
            List<LdapAttributeUpdateInfo> list) {
        Attribute a = null;
        int count = 1;
        for (LdapAttributeUpdateInfo item : list) {
            if (count++ == 1) {
                if (item.getUpdateType() == LdapClient.MOD_OPERATION_REMOVE) {
                    // No need to include the value for attribute since it will
                    // be deleted.
                    a = new BasicAttribute(attrName);
                    break;
                }
                a = new BasicAttribute(attrName, item.getValue());
                continue;
            }
            a.add(item.getValue());
        }
        return a;
    }
}
