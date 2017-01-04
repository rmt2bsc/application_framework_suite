package com.util;

import java.util.*;

/**
 * @author Roy Terrell
 * @version Revision: 1.1
 */
public class RMT2VectorUtilities {
    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static boolean isNull(Vector v) {
        return (v == null);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static boolean isNotNull(Vector v) {
        return (v != null);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static boolean isEmpty(Vector v) {
        boolean rt = false;

        if ((v != null) && (v.size() == 0)) {
            rt = true;
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static boolean isEmptyOrNull(Vector v) {
        boolean rt = false;

        if ((v == null) || (v.size() == 0)) {
            rt = true;
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static boolean isNotEmpty(Vector v) {
        boolean rt = false;

        if ((v != null) && (v.size() > 0)) {
            rt = true;
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param dest
     *            -
     * @param src
     *            -
     * 
     * @return -
     */
    public static boolean appendVector(Vector dest, Vector src) {
        int j = 0;
        Object curObj = null;
        boolean rt = true;

        if ((dest != null) && (src != null) && (src.size() > 0)) {
            for (j = 0; j < src.size(); j++) {
                // Get current object from the vector
                curObj = (Object) src.elementAt(j);

                if (curObj != null) {
                    // Add this object to the end of the destination
                    // vector
                    if (!dest.add(curObj)) {
                        rt = false;

                        break;
                    }
                }
            }
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static String vectorToString(Vector v) {
        String ret = "";

        if (v != null) {
            for (int i = 0; i < v.size(); i++) {
                if (i > 0) {
                    ret += ",";
                }

                ret += v.elementAt(i).toString();
            }
        }

        return (ret);
    }

    /**
     * --
     * 
     * @param sArr
     *            -
     * 
     * @return -
     */
    public static Vector stringsToVector(String[] sArr) {
        Vector ret = null;

        if (sArr != null) {
            ret = new Vector();

            for (int i = 0; i < sArr.length; i++) {
                ret.add(sArr[i]);
            }
        }

        return (ret);
    }

    /**
     * --
     * 
     * @param integerVector
     *            -
     * 
     * @return -
     */
    public static boolean compareVectorOfIntegers(Vector integerVector) {
        Integer jInteger;
        Integer kInteger;
        int j;
        int k;
        boolean rt = true;

        if ((integerVector != null) && (integerVector.size() > 0)) {
            for (j = 0; j < (integerVector.size() - 1); j++) {
                jInteger = (Integer) integerVector.elementAt(j);

                for (k = j + 1; k < integerVector.size(); k++) {
                    kInteger = (Integer) integerVector.elementAt(k);

                    if (jInteger.intValue() != kInteger.intValue()) {
                        rt = false;

                        break;
                    }
                }

                if (!rt) {
                    break;
                }
            }
        }
        else {
            rt = false;
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param a
     *            -
     * @param b
     *            -
     * @param status
     *            -
     * 
     * @return -
     */
    public static boolean compareUnOrderedVectorOfStrings(Vector a, Vector b,
            Vector status) {
        String aString = null;
        String bString = null;
        int numMatches = 0;
        int numCompares = 0;
        boolean rt = false;

        if ((a != null) && (a.size() > 0) && (b != null) && (b.size() > 0)) {
            if (a.size() == b.size()) {
                for (int j = 0; j < a.size(); j++) {
                    aString = (String) a.elementAt(j);

                    for (int k = 0; k < b.size(); k++) {
                        bString = (String) b.elementAt(k);

                        if (aString.compareTo(bString) == 0) {
                            numMatches++;
                        }
                        else {
                            if (aString.indexOf(bString) != -1) {
                                numCompares++;
                            }
                            else if (bString.indexOf(aString) != -1) {
                                numCompares++;
                            }
                        }
                    }
                }

                if ((numMatches + numCompares) >= a.size()) {
                    if (numMatches == a.size()) {
                        status.addElement(new Boolean(true));
                    }
                    else {
                        status.addElement(new Boolean(false));
                    }

                    if (numCompares > 0) {
                        status.addElement(new Boolean(true));
                    }
                    else {
                        status.addElement(new Boolean(false));
                    }

                    rt = true;
                }
            }
        }

        return (rt);
    }

    /**
     * --
     * 
     * @param v
     *            -
     * 
     * @return -
     */
    public static Vector collapseVector(Vector v) {
        Vector rt = null;
        int j = 0;

        if (RMT2VectorUtilities.isNotEmpty(v)) {
            rt = new Vector();

            for (j = 0; j < v.size(); j++) {
                if (v.elementAt(j) instanceof Vector) {
                    // Collapse current vector
                    Vector curFlat = RMT2VectorUtilities
                            .collapseVector((Vector) v.elementAt(j));

                    // Append flattened vector to current vector
                    if (!RMT2VectorUtilities.appendVector(rt, curFlat)) {
                        rt = null;

                        break;
                    }
                }
                else {
                    // Add element to return vector
                    rt.addElement(v.elementAt(j));
                }
            }
        }

        return (rt);
    }

}
