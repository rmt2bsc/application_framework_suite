package com.api.ldap.beans;

import java.util.List;

/**
 * A ORM bean for capturing and managing common LDAP properties.
 * <p>
 * Using the java bean naming specification, the accessor and mutator method
 * names can be used to determine the name of the LDAP attribute in which they
 * manage. For example, the method <i>getCn()</i> accessing the LDAP attribute,
 * <i>cn</i> at a particular DN.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapCommonEntity {

    private List<String> cn;

    private List<String> description;

    private List<String> ou;

    /**
     * Creates a LdapCommonEntity object withou intializing any of its
     * properties.
     */
    public LdapCommonEntity() {
        return;
    }

    /**
     * Get the common name.
     * 
     * @return List of String
     */
    public List<String> getCn() {
        return cn;
    }

    /**
     * Set the common name
     * 
     * @param cn
     *            List of String
     */
    public void setCn(List<String> cn) {
        this.cn = cn;
    }

    /**
     * Get the description.
     * 
     * @return List of String
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Set the decription
     * 
     * @param description
     *            List of String
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * Get the organizational unit name
     * 
     * @return List of String
     * 
     */
    public List<String> getOu() {
        return ou;
    }

    /**
     * Set the organizational unit name
     * 
     * @param ou
     *            List of String
     */
    public void setOu(List<String> ou) {
        this.ou = ou;
    }
}
