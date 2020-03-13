package com;

import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;

import com.api.util.RMT2Date;

/**
 * A Comparison implementation to provide custom logic as to how two String date
 * values are compared to one another.
 * <p>
 * This Comparator can be useful in ways to provide precise control over the
 * sort order of String date values.
 * 
 * @author rterrell
 *
 */
public class DateStringComparator implements Comparator {

    private static final Logger logger = Logger
            .getLogger(DateStringComparator.class);

    /**
     * Create a default DateStringComparator
     */
    public DateStringComparator() {

    }

    /**
     * Compares two Date String arguments for ordering.
     * 
     * @param o1
     *            The first Date String to be compared.
     * @param o2
     *            The second Date String to be compared.
     * @return Returns a negative integer, zero, or a positive integer when the
     *         first argument is less than, equal to, or greater than the
     *         second, respectively.
     */
    public int compare(Object o1, Object o2) {
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
            Date dt1 = RMT2Date.stringToDate(o1.toString());
            Date dt2 = RMT2Date.stringToDate(o2.toString());
            return dt1.compareTo(dt2);

        } catch (Exception e) {
            logger.warn(
                    "Date String values are incompatigle to compare to each other ["
                            + o1.toString() + "] and [" + o2.toString()
                            + "].  Compare results will default to equals", e);
            return 0;
        }
    }

}
