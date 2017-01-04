package com.util;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.StringTokenizer;

/**
 * Helper class for performing some common hashing methods
 * 
 * @author Roy Terrell
 * 
 */
public class RMT2HashUtility {
    /**
     * Used to convert a byte array in to a java.lang.String object
     * 
     * @param bytes
     *            the array of bytes to be converted
     * @return the String representation
     */
    private static String getStringHash(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int ndx = 0; ndx < bytes.length; ndx++) {
            byte b = bytes[ndx];
            sb.append(0x00FF & b);
            if (ndx + 1 < bytes.length) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    /**
     * Used to convert a java.lang.String in to a byte array
     * 
     * @param str
     *            the String to be converted
     * @return the byte array representation of the passed in String
     */
    protected static byte[] getBytes(String str) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(str, "-", false);
        while (st.hasMoreTokens()) {
            int i = Integer.parseInt(st.nextToken());
            bos.write((byte) i);
        }
        return bos.toByteArray();
    }

    /**
     * Converts a java.lang.String in to a MD5 hashed String
     * 
     * @param source
     *            the source String
     * @return the MD5 hashed version of the string
     */
    public static String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            return getStringHash(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a java.lang.String in to a SHA hashed String
     * 
     * @param source
     *            the source String
     * @return the MD5 hashed version of the string
     */
    public static String sha(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            return getStringHash(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
