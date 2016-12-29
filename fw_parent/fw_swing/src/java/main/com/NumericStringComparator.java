package com;

import java.util.Comparator;

import org.apache.log4j.Logger;

/**
 * A Comparison implementation to provide custom logic as to how two String
 * numeric values are compared to one another.
 * <p>
 * This Comparator can be useful in ways to provide precise control over the
 * sort order of String numeric values.
 * <p>
 * This comparator goes through a three stage process to render a valid
 * comparison of two objects. The first phase deals with the comparison of both
 * objects in the event null values exists. The second phase involves comparing
 * the contents of the both objects in the event one or both are equal to
 * spaces. At the third and final stage, the contents of both objects should be
 * a non-null non-space value and both are compared accordingly.
 * 
 * @author rterrell
 *
 */
public class NumericStringComparator implements Comparator {

    private static final Logger logger = Logger
            .getLogger(NumericStringComparator.class);

    /**
     * Create a default NumericStringComparator
     */
    public NumericStringComparator() {

    }

    /**
     * Compares two numeric String arguments for ordering.
     * <p>
     * This method invloves the three phase approach when comparing two objects.
     * The first phase deals with the comparison of both objects in the event
     * null values exists. The second phase involves comparing the contents of
     * the both objects in the event one or both are equal to spaces. At the
     * third and final stage, the contents of both objects should be a non-null
     * non-space value and both are compared accordingly.
     * 
     * @param o1
     *            The first numeric String to be compared.
     * @param o2
     *            The second numeric String to be compared.
     * @return Returns a negative integer, zero, or a positive integer when the
     *         first argument is less than, equal to, or greater than the
     *         second, respectively.
     */
    public int compare(Object o1, Object o2) {
        // First, Evaluate the actual objects in the event of nulls
        if (o1 == null && o2 != null) {
            return -1;
        }
        if (o1 != null && o2 == null) {
            return 1;
        }
        if (o1 == null && o2 == null) {
            return 0;
        }
        try {
            // Secondly, evaluate the String contents of each object in the
            // event of spaces
            String val1 = o1.toString();
            String val2 = o2.toString();

            if (val1.equals("") && !val2.equals("")) {
                return -1;
            }
            if (!val1.equals("") && val2.equals("")) {
                return 1;
            }
            if (val1.equals("") && val2.equals("")) {
                return 0;
            }

            // Lastly, evaluate the String contents of each object that are not
            // spaces.
            double num1 = Double.parseDouble(val1);
            double num2 = Double.parseDouble(val2);
            if (num1 < num2) {
                return -1;
            }
            if (num1 == num2) {
                return 0;
            }
            if (num1 > num2) {
                return 1;
            }
        } catch (NumberFormatException e) {
            logger.warn(
                    "Nuermic String values are incompatigle to compare to each other ["
                            + o1.toString() + "] and [" + o2.toString()
                            + "].  Compare results will default to equals", e);
            return 1;
        }
        return 0;
    }

}
