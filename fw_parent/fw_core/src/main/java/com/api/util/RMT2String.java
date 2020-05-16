package com.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class contains a collection general purpose String utilities.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2String {
    /** Pad String with leading characters */
    public static final int PAD_LEADING = -1;

    /** Pad String with trailing characters */
    public static final int PAD_TRAILING = 1;

    /**
     * Creates a string of spaces totaling "count".
     * 
     * @param count
     *            total number of spaces to produce
     * @return String
     */
    public static final String spaces(int count) {

        String value = "";

        if (count < 0) {
            return null;
        }
        else if (count == 0) {
            return "";
        }
        for (int ndx = 1; ndx <= count; ndx++) {
            value += " ";
        }

        return value;
    }

    /**
     * Creates a string of ch totaling count.
     * 
     * @param ch
     *            Character to duplicate
     * @param count
     *            Total number of duplications
     * @return String
     */
    public static final String dupChar(char ch, int count) {

        String value = "";

        if (count <= 0) {
            return null;
        }
        for (int ndx = 1; ndx <= count; ndx++) {
            value += ch;
        }

        return value;
    }

    /**
     * Creates a string of ch totaling count.
     * 
     * @param value
     *            String to duplicate
     * @param count
     *            Total number of times to duplicatd
     * @return String
     */
    public static final String dupString(String value, int count) {

        String result = "";

        if (count <= 0) {
            return null;
        }
        for (int ndx = 1; ndx <= count; ndx++) {
            result += value;
        }

        return result;
    }

    /**
     * Adds leading or trailing zeros to an long value.
     * <p>
     * <p>
     * Example: to pad 1234 with 6 leading zeros - padInt(1234, 10,
     * RMT2Utility.PAD_LEADING).
     * 
     * @param source
     *            The number to pad
     * @param precision
     *            The total number of digits to exect as a result of this
     *            operation.
     * @param direction
     *            -1=leading and 1=trailing
     * @return Padded number.
     */
    public static final String padInt(long source, int precision, int direction) {
        String temp = String.valueOf(source);
        int addCount = precision - temp.length();
        String padding = RMT2String.dupChar('0', addCount);
        if (direction == RMT2String.PAD_LEADING) {
            temp = padding + temp;
        }
        if (direction == RMT2String.PAD_TRAILING) {
            temp += padding;
        }
        return temp;
    }

    /**
     * Parses _str by extracting embedded values separted by character values
     * that equal _delim. Each value extracted will be packaged in a ArrayList
     * which is returned to the caller.
     * 
     * @param str
     *            String that is to parsed for tokens
     * @param delim
     *            The delimiter separates the tokens
     * @return List<String> of each value extracted from _str or null if str was
     *         unable to be parsed.
     */
    public static final List<String> getTokens(String str, String delim) {

        List<String> tokens = new ArrayList<String>();
        String value = null;

        if (delim == null) {
            delim = RMT2String.spaces(1);
        }

        StringTokenizer target = new StringTokenizer(str, delim);
        while (target.hasMoreTokens()) {
            value = target.nextToken();
            tokens.add(value);
        }
        if (tokens.size() <= 0) {
            tokens = null;
        }

        return tokens;
    }

    /**
     * Parses _source by replacing the first occurrence of "_delim" with
     * "_replacement" and returns the results to the caller.
     * 
     * @param source
     *            String that is to parsed for for placeholders
     * @param replacement
     *            Value that will replace "_delim"
     * @param delim
     *            The place holder that is to be replaced
     * @return String
     */
    public static final String replace(String source, String replacement,
            String delim) {

        String value = null;
        int ndx;

        if (delim == null) {
            delim = RMT2String.spaces(1);
        }

        // Determine the position of the first occurrence of the delmimter
        ndx = source.indexOf(delim);
        if (ndx <= -1) {
            return source;
        }

        // Get portion of string before delimiter
        value = source.substring(0, ndx);

        // Add replacement
        value += replacement;

        // Get the portion of string beyond the delimiter.
        value += source.substring(ndx + delim.length());

        // Retrun the results
        return value;

    }

    /**
     * Parses _source by replacing the all occurrences of "_delim" with
     * "_replacement" and returns the results to the caller.
     * 
     * @param _source
     *            String that is to be parsed for for placeholders
     * @param _replacement
     *            Value that will replace "_delim"
     * @param _delim
     *            The place holder representing the value that is to be
     *            replaced.
     * @return String
     */
    public static final String replaceAll(String source, String replacement,
            String delim) {

        String newString = null;

        // Determine the number of place holders are required to be substituted.
        newString = source.replaceAll(delim, replacement);
        return newString;
    }

    /**
     * Parses _source by replacing the all occurrences of "_delim" with
     * "_replacement" and returns the results to the caller.
     * 
     * @param source
     *            String that is to be parsed for for placeholders
     * @param replacement
     *            Value that will replace "_delim"
     * @param delim
     *            The place holder representing the value that is to be
     *            replaced.
     * @return String
     */
    public static final String replaceAll2(String source, String replacement,
            String delim) {
        if (source == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        String parts[] = source.split(delim);
        for (int ndx = 0; ndx < parts.length; ndx++) {
            buf.append(parts[ndx]);
            if ((ndx + 1) < parts.length) {
                buf.append(replacement);
            }
        }
        return buf.toString();
    }

    /**
     * Counts the total occurrences of a character within a String. Returns the
     * total count to the caller.
     * 
     * @param source
     *            String that is the source of target character to be counted.
     * @param target
     *            Character that is being counted.
     * @return the number to occurrences _target was found.
     */
    public static final int countChar(String source, char target) {

        int totalFound = 0;
        int sourceSize = source.length();

        try {
            for (int ndx = 0; ndx < sourceSize; ndx++) {
                if (source.charAt(ndx) == target) {
                    totalFound++;
                } // end if
            } // end for

            return totalFound;
        } // end try

        catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * Capitalizes the first character of a string variable and returns the
     * capitalized string to the caller.
     * 
     * @param value
     *            The source String that is to be converted.
     * @return The results of the conversion as a String.
     */
    public static final String wordCap(String value) {
        if (value == null) {
            return null;
        }
        value = value.toLowerCase();
        StringBuffer capValue = new StringBuffer(100);

        for (int ndx = 0; ndx < value.length(); ndx++) {
            if (ndx == 0) {
                Character ch = new Character(value.charAt(ndx));
                capValue.append(ch.toString().toUpperCase());
            }
            else {
                Character ch = new Character(value.charAt(ndx));
                capValue.append(ch.toString());
            }
        }
        return capValue.toString();
    }

    /**
     * Creates a credit card mask only exposing the last f digits of the credit
     * card
     * 
     * @param ccNo
     * @return returns a String in the format of [xxxxxxxxx9999] or null if
     *         <i>ccNo</i> is null or empty.
     */
    public static String maskCreditCardNumber(String ccNo) {
        StringBuffer sb = new StringBuffer();
        String results = null;
        if (RMT2String2.isNotEmpty(ccNo)) {
            int length = ccNo.length();
            int maskPos = length;
            if (length > 7) {
                maskPos = length - 4;
                String mask = RMT2String.dupChar('x', maskPos);
                sb.append(mask);
                sb.append(ccNo.substring(maskPos));
            }
            else {
                // if account.length too short, do not show length and mark
                // as xxxxxN
                maskPos = length - 1;

                sb.append("xxxxx");
                sb.append(ccNo.substring(maskPos));
                if (length > 4) {
                    sb.replace(0, 1, String.valueOf(ccNo.charAt(0)));
                }
            }
            results = sb.toString();
        }
        return results;
    }

    /**
     * Formats a string to conform to the variable and method naming conventions of the
     * Javabean specification by converting the first character of the string to
     * upper case.
     * 
     * @param entityName
     *            Source to be converted.
     * @return The <i>entityName</i> in camel case form.
     */
    public static final String getCamelCase(String entityName) {

        StringBuffer propName = new StringBuffer(50);

        propName.append(entityName.substring(0, 1).toLowerCase());
        propName.append(entityName.substring(1));

        return propName.toString();
    }
} // end class

