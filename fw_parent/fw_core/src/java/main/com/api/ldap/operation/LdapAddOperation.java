package com.api.ldap.operation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

/**
 * A class used for collecting the attributes that are to be assoicated with an
 * entry that is to be added to the LDAP repository.
 * 
 * @author rterrell
 * 
 */
public class LdapAddOperation extends LdapCommonUpdateOperation {

    /**
     * Default constructor.
     */
    public LdapAddOperation() {
        super();
    }

    /**
     * Builds an instance of Attributes that will be primarily used for
     * associating attributes to an entry selected to be added to the LDAP
     * repository.
     * 
     * @return An instance of Attributes. Returns null when there are not
     *         attribute mappings to process.
     */
    public Attributes buildAddEntryAttributes() {
        Map<String, Object> attrs = this.getAttr();
        if (attrs == null || attrs.isEmpty()) {
            return null;
        }

        Attributes entryAttrs = new BasicAttributes(true);

        // Cycle through the Map sequentially via the keys and build Attributes
        // instance
        Iterator<String> iter = attrs.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Object val = attrs.get(key);
            Attribute a = null;
            if (val instanceof List) {
                a = this.buildMultiValueAttribte(key,
                        (List<LdapAttributeUpdateInfo>) val);
            }
            else if (val instanceof LdapAttributeUpdateInfo) {
                a = this.buildSingleValueAttribute(key,
                        (LdapAttributeUpdateInfo) val);
            }
            if (a != null) {
                entryAttrs.put(a);
            }
        }
        return entryAttrs;
    }

}
