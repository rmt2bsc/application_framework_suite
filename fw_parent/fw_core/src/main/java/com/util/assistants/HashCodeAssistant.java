package com.util.assistants;

import java.util.Arrays;

/**
 * Facilitates computing hash codes from primitive values and combining individual hash codes into a cumulative hash.
 * <p />
 * Copyright 2008. Cerner Corporation
 * @author Millennium Services
 * @since 1.1
 */
public final class HashCodeAssistant
{
    private static final int DEFAULT_SEED_PRIME = 17;
    private static final int DEFAULT_SHIFT_PRIME = 31;

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashBoolean(final boolean value)
    {
        return value ? 1231 : 1237;
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashCharacter(final char value)
    {
        return value;
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashByte(final byte value)
    {
        return value;
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashShort(final short value)
    {
        return value;
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashInteger(final int value)
    {
        return value;
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashLong(final long value)
    {
        return (int) (value ^ value >>> 32);
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashFloat(final float value)
    {
        return Float.floatToIntBits(value);
    }

    /**
     * Compute a hash code for the given value.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashDouble(final double value)
    {
        return hashLong(Double.doubleToLongBits(value));
    }

    /**
     * Compute a hash code for the given value. If the object is null, {@code 17} is returned.
     * @param value Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashObject(final Object value)
    {
        return value == null ? DEFAULT_SEED_PRIME : value.hashCode();
    }

    /**
     * Compute a hash code for the given value. If the object is null,
     * {@code 17} is returned.
     * 
     * @param value
     *            Value to hash.
     * @return Hash code for the given value.
     */
    public static int hashByteArray(final byte[] value) {
        return value == null ? DEFAULT_SEED_PRIME : Arrays.hashCode(value);
    }

    /**
     * Combine the given hash codes into a cumulative hash. The values are
     * combined by starting with a seed value, {@code 17}. Then each individual
     * hash code is added to the previous cumulative result multiplied by
     * {@code 31}.
     * 
     * @param hashCodes
     *            Array of hash codes to combine, in order (cannot be null or
     *            empty).
     * @return Cumulative hash code computed from combining the given individual
     *         hash codes.
     * @throws IllegalArgumentException
     *             if the parameter conditions are not met.
     */
    public static int combineHashCodes(final int... hashCodes)
    {
        return combineHashCodesWithPrimes(DEFAULT_SEED_PRIME, DEFAULT_SHIFT_PRIME, hashCodes);
    }

    /**
     * Combine the given hash codes into a cumulative hash. The values are combined by starting with a seed value, {@code seedPrime}. Then each individual hash code is added to the previous
     * cumulative result multiplied by {@code shiftPrime}.
     * @param seedPrime Prime integer used to seed the composite hash value.
     * @param shiftPrime Prime integer used to shift the value before each time that another individual value is added in.
     * @param hashCodes Array of hash codes to combine, in order (cannot be null or empty).
     * @return Cumulative hash code computed from combining the given individual hash codes.
     * @throws IllegalArgumentException if the parameter conditions are not met.
     */
    public static int combineHashCodesWithPrimes(final int seedPrime, final int shiftPrime, final int... hashCodes)
    {
        if (hashCodes == null)
        {
            throw new IllegalArgumentException("hashCodes:null");
        }

        if (hashCodes.length == 0)
        {
            throw new IllegalArgumentException("hashCodes:empty");
        }

        int combinedHashCode = seedPrime;
        for (final int hashCode : hashCodes)
        {
            combinedHashCode = combinedHashCode * shiftPrime + hashCode;
        }
        return combinedHashCode;
    }

    /**
     * Constructor for HashCodeAssistant. Declared private to prevent direct instantiation.
     */
    // /CLOVER:OFF
    private HashCodeAssistant()
    {
        throw new UnsupportedOperationException("HashCodeAssistant should never be instantiated");
    }// /CLOVER:ON
}
