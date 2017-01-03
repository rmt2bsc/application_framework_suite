package com.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author Roy Terrell
 * 
 *         RMT2String2.java
 */
public class RMT2String2 {
    /**
     * @param s
     * @return
     */
    public static boolean isNull(String s) {
        return (s == null);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNullValue(String s) {
        if (!isNullOrEmpty(s)) {
            return (s.trim().toUpperCase().equals("NULL"));
        }
        else {
            return true;
        }
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNotNull(String s) {
        return (s != null);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isEmpty(final String s) {
        return (null == s || s.trim().length() == 0);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNotEmpty(final String s) {
        return (!isEmpty(s));
    }

    public static String checkNull(final Object s) {
        if (null == s)
            return ("");

        return (s.toString());
    }

    public static Integer checkNullInteger(final Object i) {
        if (null == i)
            return (new Integer(0));

        if (i instanceof String) {
            try {
                return (new Integer(Integer.parseInt((String) i)));
            } catch (final NumberFormatException e) {
                return (new Integer(0));
            }
        }

        return ((Integer) i);
    }

    public static String trimSpaces(final String s) {
        if (null == s)
            return ("");

        return (s.trim());
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static boolean areEqual(String a, String b) {
        boolean rt = false;
        if (a == b)
            return (true);

        if ((a != null) && (b != null)) {
            if (a.compareTo(b) == 0) {
                rt = true;
            }
        }
        return (rt);
    }

    public static boolean areEqualIgnoreCase(String a, String b) {
        boolean rt = false;
        if (a == b)
            return (true);

        if ((a != null) && (b != null)) {
            if (a.compareToIgnoreCase(b) == 0) {
                rt = true;
            }
        }

        return (rt);
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static boolean areNotEqual(String a, String b) {
        boolean rt = false;
        if ((a != null) && (b != null)) {
            if (a.compareTo(b) != 0) {
                rt = true;
            }
        }
        else if ((a == null) ^ (b == null)) {
            rt = true;
        }

        return (rt);
    }

    public static String eliminateStringListDups(final String stringList) {
        if (isEmpty(stringList))
            return (stringList);

        final String[] codes = stringList.split(",");
        final Set /* <String code> */newStringList = new TreeSet();
        for (int i = 0; i < codes.length; ++i) {
            newStringList.add(codes[i]);
        }

        final StringBuffer buf = new StringBuffer();
        for (Iterator i = newStringList.iterator(); i.hasNext();) {
            final String code = (String) i.next();
            if (buf.length() > 0)
                buf.append(",");

            buf.append(code);
        }

        return (buf.toString() + ",");
    }

    public static String padLeft(final String str, final int len, final char pad) {
        if (null == str || str.length() >= len)
            return (str);

        final StringBuffer buf = new StringBuffer(str);
        while (buf.length() < len)
            buf.insert(0, pad);

        return (buf.toString());
    }

    public static String padRight(final String str, final int len,
            final char pad) {
        if (null == str || str.length() >= len)
            return (str);

        final StringBuffer buf = new StringBuffer(str);
        while (buf.length() < len)
            buf.append(pad);

        return (buf.toString());
    }

    private static final String varStartStr = "${";

    private static final String varEndStr = "}";

    /**
     * Formats a string message replacing named variables with values provided.
     * Variables in the string message are delimited by ${ and }. For example,
     * 
     * <code>
     *           ${fileName}
     * </code>
     * 
     * will be replaced by the property named "fileName" in the msgParams
     * object. If there is no key property by this name the variable in the
     * message string will be returned unchanged.
     * 
     * @param msg
     *            The message string to be operated upon.
     * @param msgParams
     *            Properties object which contains the definitions of the
     *            variables to be replaced in the message string.
     * 
     * @return The modified string message.
     * 
     * @author Bob Withers nmrww3
     */
    public static String formatMessage(final String msg,
            final Properties msgParams) {
        // if there are no parameters or the message contains no variables
        // there is nothing for us to do.
        if (msgParams == null || msgParams.size() == 0
                || msg.indexOf(varStartStr) < 0)
            return (msg);

        final StringBuffer buf = new StringBuffer(msg);
        int pos = 0;
        while (pos < buf.length()) {
            final int varStartPos = buf.indexOf(varStartStr, pos);
            if (varStartPos < 0)
                break;

            pos = varStartPos + varStartStr.length();
            final int varEndPos = buf.indexOf(varEndStr, pos);
            if (varEndPos > 0) {
                final String varName = buf.substring(pos, varEndPos);
                if (msgParams.containsKey(varName))
                    buf.replace(varStartPos, varEndPos + varEndStr.length(),
                            msgParams.getProperty(varName));

                pos = varEndPos + varEndStr.length();
            }
        }

        return (buf.toString());
    }

    /**
     * @param value
     * @return
     */
    public static boolean containsDigit(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (Character.isDigit(value.charAt(i))) {
                    result = true;
                    break;
                }
            }
        }
        return (result);
    }

    /**
     * @param value
     * @return
     */
    public static boolean containsOnlyDigits(String value) {
        boolean result = false;

        if (isNotEmpty(value)) {
            boolean fndNonDigit = false;

            for (int i = 0; i < value.length(); i++) {
                if (!Character.isDigit(value.charAt(i))) {
                    fndNonDigit = true;
                    break;
                }
            }

            if (!fndNonDigit) {
                result = true;
            }
        }

        return (result);
    }

    /**
     * @param value
     * @return
     */
    public static boolean containsWhiteSpace(String value) {
        boolean rt = false;

        if (isNotEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (Character.isWhitespace(value.charAt(i))) {
                    rt = true;
                    break;
                }
            }
        }

        return (rt);
    }

    /**
     * @param value
     * @return
     */
    public static boolean containsSpaces(String value) {
        boolean rt = false;

        if (isNotEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);

                if ((Character.isWhitespace(c)) || (Character.isSpaceChar(c))) {
                    rt = true;
                    break;
                }
            }
        }

        return (rt);
    }

    public static String formatInteger(String number, String grpSize,
            String maxInts, String minInts) {
        String rt = "null";
        DecimalFormat dFormat = new DecimalFormat();

        if ((dFormat != null) && (isNotEmpty(number)) && (isNotEmpty(grpSize))
                && (isNotEmpty(maxInts)) && (isNotEmpty(minInts))) {
            try {
                Integer numInt = new Integer(number);
                if ((dFormat != null) && (numInt != null)) {
                    Integer gSize = new Integer(grpSize);
                    Integer maxI = new Integer(maxInts);
                    Integer minI = new Integer(minInts);

                    if ((gSize != null) && (maxI != null) && (minI != null)) {
                        StringBuffer numSB = new StringBuffer();
                        dFormat.setGroupingSize(gSize.intValue());
                        dFormat.setDecimalSeparatorAlwaysShown(false);
                        dFormat.setMaximumIntegerDigits(maxI.intValue());
                        dFormat.setMinimumIntegerDigits(minI.intValue());
                        if (numSB != null) {
                            numSB = dFormat.format(numInt, numSB,
                                    new FieldPosition(
                                            NumberFormat.INTEGER_FIELD));
                            rt = numSB.toString();
                        }
                    }
                }
            } catch (NumberFormatException nFEx) {
                rt = "null";
            } catch (NullPointerException nPEx) {
                rt = "null";
            } catch (Exception eX) {
                rt = "null";
            }
        }

        return (rt);
    }

    public static String formatString(String str, String fmt, char fmtChar) {
        String rt = null;

        if ((isNotEmpty(str)) && (isNotEmpty(fmt))) {
            StringBuffer rtB = new StringBuffer();
            int strInd = 0;
            int strLen = str.length();

            for (int j = 0; j < fmt.length(); j++) {
                if (strInd < strLen) {
                    if (fmt.charAt(j) == fmtChar) {
                        rtB.append(str.charAt(strInd));
                        strInd++;
                    }
                    else {
                        rtB.append(fmt.charAt(j));
                    }
                }
                else {
                    break;
                }
            }

            if (rtB.length() > 0) {
                rt = rtB.toString();
            }
        }

        return (rt);
    }

    /**
     * @param str
     * @return
     */
    public static String removeCtrlCharactersFromString(String str) {
        String rt = null;

        if (isNotEmpty(str)) {
            StringBuffer rtB = new StringBuffer();
            int strLen = str.length();

            for (int j = 0; j < strLen; j++) {
                char c = str.charAt(j);

                // Check to make sure character is not an iso control
                // Also allow slashes in for date storage - caviar requirements
                if ((c == '/') || (!Character.isISOControl(c))) {
                    // Final filter on characters checks to allow spaces,
                    // letters, digits, or
                    // the date slash for caviar requirements
                    if ((Character.isSpaceChar(c))
                            || (Character.isLetterOrDigit(c)) || (c == '/')) {
                        rtB.append(c);
                    }
                }
            }

            rt = rtB.toString();
        }

        return (rt);
    }

    /**
     * Decode a string so that null strings always return the keyword null, and
     * non-null strings return themselves
     * 
     * @param s
     * @return
     */
    public static String decodeNull(String s) {
        if (s != null) {
            return (s);
        }
        else {
            return ("null");
        }
    }

    /**
     * Decode a string so that empty strings always return the keyword empty,
     * and non-empty strings return the output from decodeNull on the passed in
     * string object
     * 
     * @param s
     * @return
     */
    public static String decodeEmpty(String s) {
        if (isEmpty(s)) {
            return ("empty");
        }
        else {
            return (decodeNull(s));
        }
    }

    /**
     * Output data from java beans that contain bean patterned methods Must at
     * least have the get method for output, this is obvious as you cannot get
     * any data from a set method. NOTE: This does not qualify the bean methods
     * against actual fields in the object, so a method named getX without a
     * field X will be called by this method, even thought that technically
     * invalidates the "true" definition of a java bean method "design pattern".
     * 
     * @param o
     * @return
     */
    public static StringBuffer dumpBeanProperties(Object o) {
        StringBuffer rt = null;
        Method[] methods = null;

        // If the object passed in is not null
        if (o != null) {
            // Get the object's public methods via reflection
            methods = o.getClass().getMethods();

            // If the object has methods that are public, see if
            // the object has any bean methods
            if ((methods != null) && (methods.length > 0)) {
                StringBuffer buf = null;
                HashMap beanMethodsMap = new HashMap();

                // Loop through all of the methods in the object
                for (int j = 0; j < methods.length; j++) {
                    // Get the name of the current method via reflection
                    String methName = methods[j].getName();

                    // If the method name is valid
                    if (isNotEmpty(methName)) {
                        // See if there is a get substring in the method name
                        int gIndex = methName.indexOf("get");

                        // See if there is a set substring in the method name
                        int sIndex = methName.indexOf("set");

                        // See of there is an is substring in the method name
                        int iIndex = methName.indexOf("is");

                        // If a get substring was found at the beginning of
                        // the method name, then we have a qualified get bean
                        // method
                        if (gIndex == 0) {
                            // Get the rest of the method name in the get,
                            // should
                            // refer to the property it is getting
                            String restMethName = methName.substring(3);

                            // If there is more to the method than the get
                            if (isNotEmpty(restMethName)) {
                                // See if there is already a vector started in
                                // the map using
                                // the rest of the method name as the key
                                Vector nV = (Vector) beanMethodsMap
                                        .get(restMethName);
                                if (nV == null) {
                                    // If not, start one
                                    nV = new Vector();
                                }

                                // Add the get identifier string to this vector,
                                // so we
                                // know that the index is pointing to a get
                                // method without
                                // having to re-examine it later
                                nV.addElement("get");

                                // Add in the index to the method array that
                                // corresponds to this
                                // get method
                                nV.addElement(new Integer(j));

                                // Put the vector back into the map with the
                                // rest of the method name
                                // as the key
                                beanMethodsMap.put(restMethName, nV);
                            }
                        }
                        // If a set substring was found at the beginning of
                        // the method name, then we have a qualified set bean
                        // method
                        else if (sIndex == 0) {
                            // Get the rest of the method name in the set,
                            // should
                            // refer to the property it is setting
                            String restMethName2 = methName.substring(3);

                            // If there is more to the method than the set
                            if (isNotEmpty(restMethName2)) {
                                // See if there is already a vector started in
                                // the map using
                                // the rest of the method name as the key
                                Vector nV2 = (Vector) beanMethodsMap
                                        .get(restMethName2);
                                if (nV2 == null) {
                                    // If not, start one
                                    nV2 = new Vector();
                                }

                                // Add the set identifier string to this vector,
                                // so we
                                // know that the index is pointing to a set
                                // method without
                                // having to re-examine it later
                                nV2.addElement("set");

                                // Add in the index to the method array that
                                // corresponds to this
                                // set method
                                nV2.addElement(new Integer(j));

                                // Put the vector back into the map with the
                                // rest of the method name
                                // as the key
                                beanMethodsMap.put(restMethName2, nV2);
                            }
                        }
                        else if (iIndex == 0) {
                            // Get the rest of the method name in the get,
                            // should
                            // refer to the property it is getting
                            String restMethName3 = methName.substring(2);

                            // If there is more to the method than the get
                            if (isNotEmpty(restMethName3)) {
                                // See if there is already a vector started in
                                // the map using
                                // the rest of the method name as the key
                                Vector nV3 = (Vector) beanMethodsMap
                                        .get(restMethName3);
                                if (nV3 == null) {
                                    // If not, start one
                                    nV3 = new Vector();
                                }

                                // Add the get identifier string to this vector,
                                // so we
                                // know that the index is pointing to a get
                                // method without
                                // having to re-examine it later
                                nV3.addElement("is");

                                // Add in the index to the method array that
                                // corresponds to this
                                // get method
                                nV3.addElement(new Integer(j));

                                // Put the vector back into the map with the
                                // rest of the method name
                                // as the key
                                beanMethodsMap.put(restMethName3, nV3);
                            }
                        }
                    }
                }

                // Output bean method get returns
                if (!beanMethodsMap.isEmpty()) {
                    // Store all values in the map in a vector
                    Vector mBeanNames = new Vector(beanMethodsMap.values());

                    // If the vector of values is valid
                    if (RMT2VectorUtilities.isNotEmpty(mBeanNames)) {
                        // Create the string buffer for the return
                        buf = new StringBuffer();

                        // Start the buffer with the class name and a line
                        // return
                        buf.append(o.getClass().getName() + ": " + " \r\n");

                        for (int j = 0; j < mBeanNames.size(); j++) {
                            Vector v = (Vector) mBeanNames.elementAt(j);
                            // If the parent vector contained as a value in the
                            // map, which
                            // is now in the mBeanNames vector is not null, try
                            // to get
                            // the method data from it
                            if (v != null) {
                                Integer methIndexOne = null, methIndexTwo = null;
                                String methStringOne = null, methStringTwo = null;

                                // If the vector is at least a cardinality of
                                // two,
                                // grab the first method set for examination
                                if (v.size() >= 2) {
                                    methStringOne = (String) v.elementAt(0);
                                    methIndexOne = (Integer) v.elementAt(1);
                                }

                                // If the vector is a cardinality of four,
                                // grab the other method set as well
                                if (v.size() == 4) {
                                    methStringTwo = (String) v.elementAt(2);
                                    methIndexTwo = (Integer) v.elementAt(3);
                                }

                                // Method grab parameters
                                int methIndexOneVal = -1, methIndexTwoVal = -1;
                                Method mOne = null, mTwo = null;

                                // If the first index is non-null, grab the
                                // method
                                if (methIndexOne != null) {
                                    methIndexOneVal = methIndexOne.intValue();
                                    mOne = methods[methIndexOneVal];
                                }

                                // If the second index is non-null, grab the
                                // method
                                if (methIndexTwo != null) {
                                    methIndexTwoVal = methIndexTwo.intValue();
                                    mTwo = methods[methIndexTwoVal];
                                }

                                // If we have at least one valid non-null bean
                                // method,
                                // attempt to invoke the method to gain the
                                // value
                                if ((mOne != null) || (mTwo != null)) {
                                    boolean isMethTwo = false;
                                    try {
                                        // Invoke the methods that start with
                                        // get
                                        // Since get bean methods require no
                                        // parameters,
                                        // they can be invoked rather easily
                                        if (RMT2String2.areEqual(methStringOne,
                                                "get")
                                                || RMT2String2.areEqual(
                                                        methStringOne, "is")) {
                                            Object invResultOne = mOne.invoke(
                                                    o, (Object[]) null);
                                            String invResultOneRType = mOne
                                                    .getReturnType().getName();
                                            int subLength = (methStringOne
                                                    .charAt(0) == 'i') ? 2 : 3;
                                            if (invResultOne != null) {
                                                buf.append("- ("
                                                        + mOne.getName()
                                                                .substring(
                                                                        subLength)
                                                        + " = "
                                                        + invResultOne
                                                                .toString()
                                                        + ") \r\n");
                                            }
                                        }
                                        else if (RMT2String2.areEqual(
                                                methStringTwo, "get")
                                                || RMT2String2.areEqual(
                                                        methStringTwo, "is")) {
                                            isMethTwo = true;
                                            Object invResultTwo = mTwo.invoke(
                                                    o, (Object[]) null);
                                            String invResultTwoRType = mTwo
                                                    .getReturnType().getName();
                                            int subLength = (methStringTwo
                                                    .charAt(0) == 'i') ? 2 : 3;
                                            if (invResultTwo != null) {
                                                buf.append("- ("
                                                        + mTwo.getName()
                                                                .substring(
                                                                        subLength)
                                                        + " = "
                                                        + mTwo.invoke(o,
                                                                (Object[]) null)
                                                        + ") \r\n");
                                            }
                                        }
                                    }
                                    // Catch the exception that could be thrown
                                    // if we are not
                                    // allowed Java language wise to run the
                                    // method
                                    catch (IllegalAccessException iAE) {
                                        if (!isMethTwo)
                                            buf.append("- (" + mOne.getName()
                                                    + " = null) \r\n");
                                        else
                                            buf.append("- (" + mTwo.getName()
                                                    + " = null) \r\n");

                                    }
                                    // Catch the exception that could be thrown
                                    // if the number
                                    // of arguments that we specified to the
                                    // invoke is incorrect
                                    catch (IllegalArgumentException iAgE) {
                                        if (!isMethTwo)
                                            buf.append("- (" + mOne.getName()
                                                    + " = null) \r\n");
                                        else
                                            buf.append("- (" + mTwo.getName()
                                                    + " = null) \r\n");
                                    }
                                    // Catch any exceptions thrown by the
                                    // invoked target method
                                    catch (InvocationTargetException iTE) {
                                        if (!isMethTwo)
                                            buf.append("- (" + mOne.getName()
                                                    + " = null) \r\n");
                                        else
                                            buf.append("- (" + mTwo.getName()
                                                    + " = null) \r\n");
                                    }
                                    // Catch a null pointer exception for an
                                    // object passed in as null
                                    catch (NullPointerException nEx) {
                                        if (!isMethTwo)
                                            buf.append("- (" + mOne.getName()
                                                    + " = null) \r\n");
                                        else
                                            buf.append("- (" + mTwo.getName()
                                                    + " = null) \r\n");
                                    }
                                }
                            }
                        }

                        // If we appended to the buffer, return it as a string
                        if ((buf != null) && (buf.length() > 0)) {
                            rt = buf;
                        }
                    }
                }
            }
        }
        return (rt);
    }

    /**
     * Adds a String value to a delimited String and returns the results. Only
     * adds the String if its value does not already exist in the delimited
     * String. Also does a rudimentary max length check and trims the results if
     * necessary. This method can be useful when working with properties of type
     * String, but whose value needs to be set as a delimited list of values.
     */
    public static String addValueToDelimitedString(String valueToAdd,
            String delimitedString, String delimiter, int maxLength) {
        if ((delimitedString == null) || (delimitedString.trim().equals(""))) {
            delimitedString = "";
        }

        if ((valueToAdd == null) || (valueToAdd.trim().equals(""))) {
            return delimitedString;
        }

        String[] tokens = valueToAdd.split(delimiter);

        for (int i = 0; i < tokens.length; i++) {
            // only add value if it does not already exist
            if (delimitedString.indexOf(tokens[i] + delimiter) == -1) {
                delimitedString = delimitedString + tokens[i] + delimiter;

                if (delimitedString.length() > maxLength) {
                    delimitedString = delimitedString.substring(0, maxLength);
                }
            }
        }

        return delimitedString;
    }

    /**
     * Removes a String value from a delimited String and returns the results.
     * Also does a rudimentary max length check and trims the results if
     * necessary. This method can be useful when working with properties of type
     * String, but whose value needs to be set as a delimited list of values.
     */
    public static String removeValueFromDelimitedString(String valueToRemove,
            String delimitedString, String delimiter, int maxLength) {
        if ((delimitedString == null) || (delimitedString.trim().equals(""))) {
            delimitedString = "";
        }

        if ((valueToRemove == null) || (valueToRemove.trim().equals(""))) {
            return delimitedString;
        }

        if (delimitedString.indexOf(valueToRemove + delimiter) != -1) {
            int beginIndex = delimitedString.indexOf(valueToRemove + delimiter);
            int endIndex = beginIndex + (valueToRemove + delimiter).length();

            StringBuffer temp = new StringBuffer(delimitedString.substring(0,
                    beginIndex));
            temp.append(delimitedString.substring(endIndex));
            delimitedString = temp.toString();

            if (delimitedString.length() > maxLength) {
                delimitedString = delimitedString.substring(0, maxLength);
            }
        }

        return delimitedString;
    }

    /**
     * Splits the string into parts assuming delimiter is a comma and then
     * returns the parts in a list.
     * 
     * @param string
     * @return
     */
    public static List makeList(String string) {
        return makeList(string, ",", false);
    }

    /**
     * Splits the string into parts assuming delimiter is a comma and then
     * returns the parts in a list. It also converts the string to upper case
     * based on the convertToUpperCase parameter.
     * 
     * @param string
     * @param convertToUpperCase
     * @return
     */
    public static List makeList(String string, boolean convertToUpperCase) {
        return makeList(string, ",", convertToUpperCase);
    }

    /**
     * Splits the string into parts using the regularExpression and then returns
     * the parts in a list.
     * 
     * @param string
     * @param regularExpression
     * @return
     */
    public static List makeList(String string, String regularExpression) {
        return makeList(string, regularExpression, false);
    }

    /**
     * Splits the string into parts using the regularExpression and then returns
     * the parts in a list. It will also upper case the string based on the
     * convertToUpper parameter.
     * 
     * @param string
     * @param regularExpression
     * @param convertToUpper
     * @return
     */
    public static List makeList(String string, String regularExpression,
            boolean convertToUpper) {
        List returnValue = null;

        if (string != null) {

            if (convertToUpper) {
                string = string.toUpperCase();
            }

            String[] tokens = string.split(regularExpression);

            returnValue = Arrays.asList(tokens);
        }
        else {
            returnValue = new ArrayList();
        }

        return returnValue;
    }

    public static String getElement(String string, String regularExpression,
            int element) {
        List l = makeList(string, regularExpression);
        String returnValue = null;
        try {
            returnValue = (String) l.get(element);
        } catch (Exception e) {
            // do nothing, return null;
        }
        return returnValue;
    }

    public static String makeString(List collection) {
        StringBuffer returnValue = new StringBuffer();

        if (collection != null && collection.size() > 0) {
            Iterator iterator = collection.iterator();
            String value = (String) iterator.next();
            returnValue.append(value);
            while (iterator.hasNext()) {
                value = (String) iterator.next();
                returnValue.append(", ").append(value);
            }
        }

        return returnValue.toString();
    }

    public static String stringToHTMLString(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                }
                else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            }
            else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("&lt;br/&gt;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

}
