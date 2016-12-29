package com.api.ldap.operation;

/**
 * @author rterrell
 * 
 */
public class LdapAttributeUpdateInfo {

    private Object value;

    /**
     * The type of update operation to perform on an attribute and its value.
     * <p>
     * Valid values are:<br>
     * <ul>
     * <li>None - 0</li>
     * <li>Add - 1</li>
     * <li>Replace - 2</li>
     * <li>Remove - 3</li>
     * </ul>
     */
    private int updateType;

    /**
     * Default Constructor
     */
    public LdapAttributeUpdateInfo() {
        return;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the updateType
     */
    public int getUpdateType() {
        return updateType;
    }

    /**
     * @param updateType
     *            the updateType to set
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

}
