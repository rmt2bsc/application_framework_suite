package com.util.assistants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * Provides primitive data verification facilities to support runtime assertion
 * of predicates that must hold true.
 * 
 * @author Roy Terrell
 * 
 * @version $Revision: 43282 $
 */
public class Verifier {
    
    /**
     * Verify that the predicate {@code variant > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(int, String)
     * @see #verifyNotPositive(int)
     * @since 2.2
     */
    public static void verifyPositive(final int variant) {
        if (!(variant > 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(int)
     * @see #verifyNotPositive(int, String)
     * @since 2.2
     */
    public static void verifyPositive(final int variant, final String message) {
        if (!(variant > 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(int, String)
     * @see #verifyPositive(int)
     * @since 2.2
     */
    public static void verifyNotPositive(final int variant) {
        if (!(variant <= 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(int)
     * @see #verifyPositive(int, String)
     * @since 2.2
     */
    public static void verifyNotPositive(final int variant, final String message) {
        if (!(variant <= 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant > 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(long, String)
     * @see #verifyNotPositive(long)
     * @since 2.2
     */
    public static void verifyPositive(final long variant) {
        if (!(variant > 0L)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant > 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(long)
     * @see #verifyNotPositive(long, String)
     * @since 2.2
     */
    public static void verifyPositive(final long variant, final String message) {
        if (!(variant > 0L)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(long, String)
     * @see #verifyPositive(long)
     * @since 2.2
     */
    public static void verifyNotPositive(final long variant) {
        if (!(variant <= 0L)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(long)
     * @see #verifyPositive(long, String)
     * @since 2.2
     */
    public static void verifyNotPositive(final long variant, final String message) {
        if (!(variant <= 0L)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant > 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(double, String)
     * @see #verifyNotPositive(double)
     * @since 2.2
     */
    public static void verifyPositive(final double variant) {
        if (!(variant > 0D && !Double.isNaN(variant))) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant > 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(double)
     * @see #verifyNotPositive(double, String)
     * @since 2.2
     */
    public static void verifyPositive(final double variant, final String message) {
        if (!(variant > 0D && !Double.isNaN(variant))) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(double, String)
     * @see #verifyPositive(double)
     * @since 2.2
     */
    public static void verifyNotPositive(final double variant) {
        if (!(variant <= 0D && !Double.isNaN(variant))) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant <= 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(double)
     * @see #verifyPositive(double, String)
     * @since 2.2
     */
    public static void verifyNotPositive(final double variant, final String message) {
        if (!(variant <= 0D && !Double.isNaN(variant))) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(BigInteger, String)
     * @see #verifyNotPositive(BigInteger)
     * @since 2.2
     */
    public static void verifyPositive(final BigInteger variant) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) > 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     *
     * @see #verifyPositive(BigInteger)
     * @see #verifyNotPositive(BigInteger, String)
     * @since 2.2
     */
    public static void verifyPositive(final BigInteger variant, final String message) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) > 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(BigInteger, String)
     * @see #verifyPositive(BigInteger)
     * @since 2.2
     */
    public static void verifyNotPositive(final BigInteger variant) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) <= 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(BigInteger)
     * @see #verifyPositive(BigInteger, String)
     * @since 2.2
     */
    public static void verifyNotPositive(final BigInteger variant, final String message) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) <= 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(BigDecimal, String)
     * @see #verifyNotPositive(BigDecimal)
     * @since 2.2
     */
    public static void verifyPositive(final BigDecimal variant) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) > 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) > 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyPositive(BigDecimal)
     * @see #verifyNotPositive(BigDecimal, String)
     * @since 2.2
     */
    public static void verifyPositive(final BigDecimal variant, final String message) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) > 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(BigDecimal, String)
     * @see #verifyPositive(BigDecimal)
     * @since 2.2
     */
    public static void verifyNotPositive(final BigDecimal variant) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) <= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotPositive(BigDecimal)
     * @see #verifyPositive(BigDecimal, String)
     * @since 2.2
     */
    public static void verifyNotPositive(final BigDecimal variant, final String message) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate {@code variant < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(int, String)
     * @see #verifyNotNegative(int)
     * @since 2.2
     */
    public static void verifyNegative(final int variant) {
        if (!(variant < 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     *
     * @see #verifyNegative(int)
     * @see #verifyNotNegative(int, String)
     * @since 2.2
     */
    public static void verifyNegative(final int variant, final String message) {
        if (!(variant < 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(int, String)
     * @see #verifyNegative(int) 
     * @since 2.2
     */
    public static void verifyNotNegative(final int variant) {
        if (!(variant >= 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(int)
     * @see #verifyNegative(int, String)
     * @since 2.2
     */
    public static void verifyNotNegative(final int variant, final String message) {
        if (!(variant >= 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant < 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(long, String)
     * @see #verifyNotNegative(long)
     * @since 2.2
     */
    public static void verifyNegative(final long variant) {
        if (!(variant < 0L)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant < 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(long)
     * @see #verifyNotNegative(long, String)
     * @since 2.2
     */
    public static void verifyNegative(final long variant, final String message) {
        if (!(variant < 0L)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(long, String)
     * @see #verifyNegative(long) 
     * @since 2.2
     */
    public static void verifyNotNegative(final long variant) {
        if (!(variant >= 0L)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0L} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(long)
     * @see #verifyNegative(long, String)
     * @since 2.2
     */
    public static void verifyNotNegative(final long variant, final String message) {
        if (!(variant >= 0L)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant < 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(double, String)
     * @see #verifyNotNegative(double) 
     * @since 2.2
     */
    public static void verifyNegative(final double variant) {
        if (!(variant < 0D && !Double.isNaN(variant))) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant < 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(double)
     * @see #verifyNotNegative(double, String) 
     * @since 2.2
     */
    public static void verifyNegative(final double variant, final String message) {
        if (!(variant < 0D && !Double.isNaN(variant))) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(double, String)
     * @see #verifyNegative(double)
     * @since 2.2
     */
    public static void verifyNotNegative(final double variant) {
        if (!(variant >= 0D && !Double.isNaN(variant))) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant >= 0D && !Double.isNaN(variant)} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(double)
     * @see #verifyNegative(double, String)
     * @since 2.2
     */
    public static void verifyNotNegative(final double variant, final String message) {
        if (!(variant >= 0D && !Double.isNaN(variant))) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(BigInteger, String)
     * @see #verifyNotNegative(BigInteger)
     * @since 2.2
     */
    public static void verifyNegative(final BigInteger variant) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) < 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(BigInteger)
     * @see #verifyNotNegative(BigInteger, String)
     * @since 2.2
     */
    public static void verifyNegative(final BigInteger variant, final String message) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) < 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(BigInteger, String)
     * @see #verifyNegative(BigInteger)
     * @since 2.2
     */
    public static void verifyNotNegative(final BigInteger variant) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) >= 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigInteger.ZERO) >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(BigInteger)
     * @see #verifyNegative(BigInteger, String)
     * @since 2.2
     */
    public static void verifyNotNegative(final BigInteger variant, final String message) {
        if (!(variant != null && variant.compareTo(BigInteger.ZERO) >= 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(BigDecimal, String)
     * @see #verifyNotNegative(BigDecimal)
     * @since 2.2
     */
    public static void verifyNegative(final BigDecimal variant) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) < 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) < 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNegative(BigDecimal)
     * @see #verifyNotNegative(BigDecimal, String)
     * @since 2.2
     */
    public static void verifyNegative(final BigDecimal variant, final String message) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) < 0)) {
            throw new VerifyException(message);
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(BigDecimal, String)
     * @see #verifyNegative(BigDecimal)
     * @since 2.2
     */
    public static void verifyNotNegative(final BigDecimal variant) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) >= 0)) {
            throw new VerifyException();
        }
    }
    
    /**
     * Verify that the predicate {@code variant != null && variant.compareTo(BigDecimal.ZERO) >= 0} is {@code true}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNegative(BigDecimal)
     * @see #verifyNegative(BigDecimal, String)
     * @since 2.2
     */
    public static void verifyNotNegative(final BigDecimal variant, final String message) {
        if (!(variant != null && variant.compareTo(BigDecimal.ZERO) >= 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the given predicate is true.
     * 
     * @param predicate
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verify(boolean predicate) {
        if (!predicate) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the given predicate is true.
     * 
     * @param predicate
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verify(boolean predicate, String message) {
        if (!predicate) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant == null</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNull(Object variant) {
        if (!(variant == null)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == null</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNull(Object variant, String message) {
        if (!(variant == null)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotNull(Object variant) {
        if (!(variant != null)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotNull(Object variant, String message) {
        if (!(variant != null)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.length() == 0</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(String variant) {
        if (!(variant == null || variant.length() == 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.length() == 0</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(String variant, String message) {
        if (!(variant == null || variant.length() == 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.length() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(String variant) {
        if (!(variant != null && variant.length() > 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.length() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(String variant, String message) {
        if (!(variant != null && variant.length() > 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.size() == 0</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(Collection<?> variant) {
        if (!(variant == null || variant.size() == 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.size() == 0</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(Collection<?> variant, String message) {
        if (!(variant == null || variant.size() == 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.size() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(Collection<?> variant) {
        if (!(variant != null && variant.size() > 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.size() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(Collection<?> variant, String message) {
        if (!(variant != null && variant.size() > 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.size() == 0</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(Map<?, ?> variant) {
        if (!(variant == null || variant.size() == 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == null || variant.size() == 0</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyEmpty(Map<?, ?> variant, String message) {
        if (!(variant == null || variant.size() == 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.size() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(Map<?, ?> variant) {
        if (!(variant != null && variant.size() > 0)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant &#33;= null &amp;&amp; variant.size() &gt;
     * 0</code>
     * is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyNotEmpty(Map<?, ?> variant, String message) {
        if (!(variant != null && variant.size() > 0)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the given array is not empty.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNoNullElements(Object[], String)
     * @since 2.2
     */
    public static void verifyNotEmpty(final Object[] variant, final String message) {
        if (variant == null || variant.length <= 0) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the given array is {@link #verifyNotNull(Object, String) NOT null} and contains
     * no {@code null} elements.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNull(Object, String)
     * @since 2.2
     */
    public static void verifyNoNullElements(final Object[] variant, final String message) {
        verifyNotNull(variant, message);

        for (final Object element : variant) {
            if (element == null) {
                throw new VerifyException(message);
            }
        }
    }

    /**
     * Verify that the given {@link Iterable} is {@link #verifyNotNull(Object, String) NOT null} and
     * contains no {@code null} values.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNull(Object, String)
     * @since 2.2
     */
    public static void verifyNoNullValues(final Iterable<?> variant, final String message) {
        verifyNotNull(variant, message);

        for (final Object value : variant) {
            if (value == null) {
                throw new VerifyException(message);
            }
        }
    }

    /**
     * Verify that the given {@link Collection} is {@link #verifyNotNull(Object, String) NOT null} and
     * contains no {@code null} values.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNull(Object, String)
     * @since 2.2
     */
    public static void verifyNoNullValues(final Collection<?> variant, final String message) {
        verifyNoNullValues((Iterable<?>)variant, message);
    }

    /**
     * Verify that the given {@link Map} is {@link #verifyNotNull(Object, String) NOT null} and
     * contains no {@code null} keys.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNull(Object, String)
     * @since 2.2
     */
    public static void verifyNoNullKeys(final Map<?, ?> variant, final String message) {
        verifyNotNull(variant, message);
        verifyNoNullValues(variant.keySet(), message);
    }

    /**
     * Verify that the given {@link Map} is {@link #verifyNotNull(Object, String) NOT null} and
     * contains no {@code null} values.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotNull(Object, String)
     * @since 2.2
     */
    public static void verifyNoNullValues(final Map<?, ?> variant, final String message) {
        verifyNotNull(variant, message);
        verifyNoNullValues(variant.values(), message);
    }

    /**
     * Verify that the predicate {@code variant == null || variant.length() == 0 ||
     * isBlank(variant)} is {@code true}, where {@code isBlank} returns {@code true} if the string
     * contains only whitespace, as defined by {@link Character#isWhitespace(char)}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyBlank(String, String)
     * @see #verifyNotBlank(String)
     * @see #verifyNotBlank(String, String)
     * @see Character#isWhitespace(char)
     * @since 2.2
     */
    public static void verifyBlank(final String variant) {
        if (!(variant == null || variant.length() == 0 || isBlank(variant))) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant == null || variant.length() == 0 ||
     * isBlank(variant)} is {@code true}, where {@code isBlank} returns {@code true} if the string
     * contains only whitespace, as defined by {@link Character#isWhitespace(char)}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyBlank(String)
     * @see #verifyNotBlank(String)
     * @see #verifyNotBlank(String, String)
     * @see Character#isWhitespace(char)
     * @since 2.2
     */
    public static void verifyBlank(final String variant, final String message) {
        if (!(variant == null || variant.length() == 0 || isBlank(variant))) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.length() != 0 &&
     * !isBlank(variant)} is {@code true}, where {@code isBlank} returns {@code true} if the string
     * contains only whitespace, as defined by {@link Character#isWhitespace(char)}.
     * 
     * @param variant The variant to check.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotBlank(String, String)
     * @see #verifyBlank(String)
     * @see #verifyBlank(String, String)
     * @see Character#isWhitespace(char)
     * @since 2.2
     */
    public static void verifyNotBlank(final String variant) {
        if (!(variant != null && variant.length() != 0 && !isBlank(variant))) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate {@code variant != null && variant.length() != 0 &&
     * !isBlank(variant)} is {@code true}, where {@code isBlank} returns {@code true} if the string
     * contains only whitespace, as defined by {@link Character#isWhitespace(char)}.
     * 
     * @param variant The variant to check.
     * @param message The exception message to use if the check fails.
     * @throws VerifyException if the parameter conditions are not met.
     * 
     * @see #verifyNotBlank(String)
     * @see #verifyBlank(String)
     * @see #verifyBlank(String, String)
     * @see Character#isWhitespace(char)
     * @since 2.2
     */
    public static void verifyNotBlank(final String variant, final String message) {
        if (!(variant != null && variant.length() != 0 && !isBlank(variant))) {
            throw new VerifyException(message);
        }
    }

    /**
     * Determines if the given string is considered blank (consisting of only whitespace or empty).
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * 
     * @param string The String value to check for as blank (MUST NOT be {@code null}).
     * @return {@code true} if {@code string} is blank, otherwise {@code false}
     * @since 2.2
     */
    private static boolean isBlank(final String string) {
        assert string != null;

        final int numberOfCharacters = string.length();

        int charIndex = 0;

        while (charIndex < numberOfCharacters) {
            if (!Character.isWhitespace(string.charAt(charIndex))) {
                return false;
            }

            ++charIndex;
        }
        return true;
    }

    /**
     * Verify that the predicate <code>variant == true</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyTrue(boolean variant) {
        if (!(variant == true)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == true</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyTrue(boolean variant, String message) {
        if (!(variant == true)) {
            throw new VerifyException(message);
        }
    }

    /**
     * Verify that the predicate <code>variant == false</code> is true.
     * 
     * @param variant
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyFalse(boolean variant) {
        if (!(variant == false)) {
            throw new VerifyException();
        }
    }

    /**
     * Verify that the predicate <code>variant == false</code> is true.
     * 
     * @param variant
     * @param message
     * 
     * @throws VerifyException If the predicate is <code>false</code>.
     */
    public static void verifyFalse(boolean variant, String message) {
        if (!(variant == false)) {
            throw new VerifyException(message);
        }
    }

    private Verifier() {}
}
