package com.util.assistants;

import java.util.Arrays;

/**
 * <p>
 * <i>EqualityAssistant</i> is a utility for determining object equality and
 * implementing {@link java.lang.Object#equals(java.lang.Object) Object equals
 * methods}.
 * </p>
 * 
 * @author Roy Terrell
 */
public class EqualityAssistant {

    /**
     * <p>
     * Returns <code>true</code> if both objects are equal, which is determined by using the
     * 'equals' method. If both objects are <code>null</code>, then <code>true</code> is
     * returned. If one object is <code>null</code> and the other is not, then <code>false</code>
     * is returned.
     * </p>
     * 
     * @param obj1 The first object; if not <code>null</code>, this object's equals method is
     *        used.
     * @param obj2 The second object; this object is passed to the first's equals method.
     * 
     * @return A boolean value.
     */
    public static boolean equal(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }

        return (obj1 == null ? false : obj1.equals(obj2));
    }

    /**
     * <p>
     * Returns <code>true</code> if both objects are <b>not</b> equal. This is implemented as the
     * negation of a call to the {@link #equal(Object, Object)} method.
     * </p>
     * 
     * @param obj1 The first object.
     * @param obj2 The second object.
     * 
     * @return A boolean value.
     */
    public static boolean notEqual(Object obj1, Object obj2) {
        return !equal(obj1, obj2);
    }

    /**
     * <p>
     * Returns <code>true</code> if both arrays are equal, which is determined
     * by using the 'equals' method. If both objects are <code>null</code>, then
     * <code>true</code> is returned. If one object is <code>null</code> and the
     * other is not, then <code>false</code> is returned.
     * </p>
     * 
     * @param obj1
     *            The first object; if not <code>null</code>, this object's
     *            equals method is used.
     * @param obj2
     *            The second object; this object is passed to the first's equals
     *            method.
     * 
     * @return A boolean value.
     */
    public static boolean bytesEqual(byte[] obj1, byte[] obj2) {
        if (Arrays.equals(obj1, obj2)) {
            return true;
        }

        return (obj1 == null ? false : Arrays.equals(obj1, obj2));
    }

    /**
     * <p>
     * Returns <code>true</code> if both arrays are <b>not</b> equal. This is
     * implemented as the negation of a call to the
     * {@link #Arrays.equals(byte[], byte[])} method.
     * </p>
     * 
     * @param obj1
     *            The first object.
     * @param obj2
     *            The second object.
     * 
     * @return A boolean value.
     */
    public static boolean bytesNotEqual(byte[] obj1, byte[] obj2) {
        return !Arrays.equals(obj1, obj2);
    }

    private EqualityAssistant() {}
}
