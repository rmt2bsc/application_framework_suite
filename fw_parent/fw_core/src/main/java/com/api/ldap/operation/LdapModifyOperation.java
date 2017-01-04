package com.api.ldap.operation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attribute;
import javax.naming.directory.ModificationItem;

/**
 * A class used for collecting the attributes that are to be assoicated with an
 * entry that is to be added to the LDAP repository.
 * 
 * @author rterrell
 * 
 */
public class LdapModifyOperation extends LdapCommonUpdateOperation {

    /**
     * Default constructor.
     */
    public LdapModifyOperation() {
        super();
    }

    /**
     * Builds ModificationItem array of Attribute instances that will be
     * primarily used for associating attributes changes to an entry selected
     * LDAP repository.
     * <p>
     * Attribute modifications include the addition, replacement, or removal of
     * an entry's attribute.
     * <p>
     * In regards to multi-valued attributes, the entire list of values for each
     * said attribute will be impacted during the modification. For example,
     * when the multi-valued attribute is modified with the
     * {@link com.api.ldap.LdapClient#MOD_OPERATION_REPLACE
     * MOD_OPERATION_REPLACE} indicator, all values of the previous list are
     * overwritten.
     * 
     * @return An array of {@link ModificationItem}.
     * @throws LdapAttributeOperationFailureException
     */
    public ModificationItem[] buildModificationEntryAttributes()
            throws LdapAttributeOperationFailureException {
        Map<String, Object> attrs = this.getAttr();
        if (attrs == null || attrs.isEmpty()) {
            return null;
        }

        // Initialize modifications array
        ModificationItem[] mods = new ModificationItem[attrs.size()];

        // Cycle through the Map sequentially via the keys and load
        // ModificationItem array
        Iterator<String> iter = attrs.keySet().iterator();
        int ndx = 0;
        while (iter.hasNext()) {
            String key = iter.next();
            Object val = attrs.get(key);
            if (val == null) {
                continue;
            }
            Attribute a = null;
            int modOp = 0;
            if (val instanceof List) {
                a = this.buildMultiValueAttribte(key,
                        (List<LdapAttributeUpdateInfo>) val);
                // Since this is a List, get the update type of the first item.
                modOp = ((List<LdapAttributeUpdateInfo>) val).get(0)
                        .getUpdateType();
            }
            else if (val instanceof LdapAttributeUpdateInfo) {
                modOp = ((LdapAttributeUpdateInfo) val).getUpdateType();
                a = this.buildSingleValueAttribute(key,
                        (LdapAttributeUpdateInfo) val);
            }

            // Load array
            if (a != null) {
                try {
                    mods[ndx++] = new ModificationItem(modOp, a);
                } catch (IllegalArgumentException e) {
                    this.msg = "Failed to create JNDI ModificationItem object with the following parameters [Attribute="
                            + a.getID()
                            + ", Modification Operator="
                            + modOp
                            + "]";
                    throw new LdapAttributeOperationFailureException(this.msg,
                            e);
                }
            }
        }
        return mods;
    }
}
