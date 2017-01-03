package com.api.ldap;

import java.util.Iterator;
import java.util.Map;

import com.api.ldap.operation.LdapCompareOperation;

/**
 * Contains helper funtions for LDAP operations.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapHelper {

    /**
     * 
     */
    public LdapHelper() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Builds an Object array of values that will substitute the variables or
     * place holders contained in search filter expression.
     * 
     * @param parms
     *            an instance of {@link LdapCompareOperation} containing a Map
     *            of filter arguments
     * @return int value representing the total number of arguments processed.
     */
    public static final Object[] buildFilterArgs(LdapCompareOperation parms)
            throws LdapFilterPlaceholderException {
        Map<String, String> args = parms.getSearchFilterArgs();
        int totArgs = args.size();
        int placeHolderCount = 0;
        Iterator<String> keys = args.keySet().iterator();
        StringBuffer buf = new StringBuffer();
        while (keys.hasNext()) {
            String key = keys.next();
            String val = args.get(key);
            if (LdapHelper.isValidPlaceHolder(val)) {
                placeHolderCount++;
            }
            buf.append("(");
            buf.append(key);
            buf.append("=");
            buf.append(val);
            buf.append(")");
        }
        String filter = null;
        if (buf.length() == 0) {
            parms.setSearchFilter(null);
            return null;
        }
        if (totArgs > 1) {
            filter = "(&" + buf + ")";
        }
        else {
            filter = buf.toString();
        }

        // Set serach filter
        parms.setSearchFilter(filter);

        // Verify that total number of place holders found equal the total
        // number of place holder values
        if (parms.getSearchFilterPlaceholders() != null
                && parms.getSearchFilterPlaceholders().size() > 0) {
            if (placeHolderCount > 0) {
                if (placeHolderCount != parms.getSearchFilterPlaceholders()
                        .size()) {
                    throw new LdapFilterPlaceholderException(
                            "Total number of place holder does not match the total number substitution arguments");
                }
                // Create search argument substitute values
                Object a[] = new Object[parms.getSearchFilterPlaceholders()
                        .size()];
                parms.getSearchFilterPlaceholders().toArray(a);
                return a;
            }
        }
        return null;
    }

    /**
     * 
     * @param arg
     * @return
     */
    public static final boolean isValidPlaceHolder(String arg) {
        if (arg == null) {
            return false;
        }
        String startChar = arg.substring(0, 1);
        String endChar = arg.substring(arg.length() - 1);
        if (startChar.equals("{") && endChar.equals("}")) {
            // validate body which should be a number
            String body = arg.substring(1, arg.length() - 1);
            try {
                Integer.parseInt(body);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }
}
