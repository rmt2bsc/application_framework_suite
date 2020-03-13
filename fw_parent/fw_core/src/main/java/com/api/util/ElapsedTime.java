package com.api.util;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Class for managing components of a time structure.
 * 
 * @author roy.terrell
 * 
 */
public class ElapsedTime extends RMT2BaseBean {
    private static final long serialVersionUID = 5205471229965481437L;

    private int hours;

    private int mins;

    private int secs;

    private int ms;

    /**
     * Constructs an ElapsedTime object with zero hours, minutes, seconds, and
     * millisecnds.
     * 
     * @throws SystemException
     */
    public ElapsedTime() throws SystemException {
    }

    /**
     * Sets the hours.
     * 
     * @param value
     *            int
     */
    public void setHours(int value) {
        this.hours = value;
    }

    /**
     * Gets the hours
     * 
     * @return int
     */
    public int getHours() {
        return this.hours;
    }

    /**
     * Sets the minutes
     * 
     * @param value
     *            int
     */
    public void setMins(int value) {
        this.mins = value;
    }

    /**
     * Gets the minutes
     * 
     * @return int
     */
    public int getMins() {
        return this.mins;
    }

    /**
     * Sets the seconds.
     * 
     * @param value
     *            int
     */
    public void setSecs(int value) {
        this.secs = value;
    }

    /**
     * Gets the seconds.
     * 
     * @return int
     */
    public int getSecs() {
        return this.secs;
    }

    /**
     * Sets the milliseconds
     * 
     * @param value
     *            int
     */
    public void setMs(int value) {
        this.ms = value;
    }

    /**
     * Gets the milliseconds
     * 
     * @return int
     */
    public int getMs() {
        return this.ms;
    }

    /**
     * Builds the time as a String in the format of hr:mins:sec:ms
     * 
     * @return String
     */
    public String toString() {
        return this.hours + ":" + this.mins + ":" + this.secs + ":" + this.ms;
    }

    /**
     * Builds the time as a String in an extended format like: xx Hours, xx
     * Minutes, xx Seconds, xx Milliseconds
     * 
     * @param _flag
     *            true will build the extended format and false will build the
     *            default format.
     * @return String
     */
    public String toString(boolean _flag) {
        if (_flag) {
            StringBuffer result = new StringBuffer(50);
            result.append(this.hours);
            result.append(" Hours, ");
            result.append(this.mins);
            result.append(" Minutes, ");
            result.append(this.secs);
            result.append(" Seconds, ");
            result.append(this.ms);
            result.append(" Milliseconds");
            return result.toString();
        }
        else {
            return this.toString();
        }
    }

    /**
     * No Action.
     */
    public void initBean() throws SystemException {
    }
}