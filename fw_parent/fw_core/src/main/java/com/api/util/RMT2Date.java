package com.api.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.NotFoundException;
import com.SystemException;
import com.api.DataSourceApi;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.persistence.DatabaseException;

/**
 * Class contains a collection general purpose Date utilities.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2Date {

    private static Logger logger = Logger.getLogger(RMT2Date.class);

    /**
     * List of the days of the week in abbreviated form. The first element = Sun
     * and the last = Sat.
     */
    public static final String DAYS_OF_WEEK[] = { "Sun", "Mon", "Tue", "Wed",
            "Thu", "Fri", "Sat" };

    /** First day of the week */
    public static final int DOW_FIRST = 1;

    /** Last day of the week */
    public static final int DOW_LAST = 7;

    /** Total number days in the week */
    public static final int DOW_TOTAL = 7;

    /**
     * Different date formats.
     */
    private static ArrayList<String> dateFormats = new ArrayList<String>();
    static {
        dateFormats.add("EEE MMM d HH:mm:ss z yyyy");
        dateFormats.add("yyyy-MM-dd HH:mm:ss");
        dateFormats.add("yyyy-MMM-dd HH:mm:ss");
        dateFormats.add("yyyy-MMMM-dd HH:mm:ss");
        dateFormats.add("MM-dd-yyyy HH:mm:ss");
        dateFormats.add("MMM-dd-yyyy HH:mm:ss");
        dateFormats.add("MM/dd/yyyy HH:mm:ss");
        dateFormats.add("yyyy/MM/dd HH:mm:ss");
        dateFormats.add("MMM/dd/yyyy HH:mm:ss");
        dateFormats.add("MMM dd, yyyy HH:mm:ss");
        dateFormats.add("MMMM dd, yyyy HH:mm:ss");
        dateFormats.add("yyyy-MM-dd hh:mm:ss a");
        dateFormats.add("yyyy-MMM-dd hh:mm:ss a");
        dateFormats.add("yyyy-MMMM-dd hh:mm:ss a");
        dateFormats.add("MM-dd-yyyy hh:mm:ss a");
        dateFormats.add("MMM-dd-yyyy hh:mm:ss a");
        dateFormats.add("MM/dd/yyyy hh:mm:ss a");
        dateFormats.add("yyyy/MM/dd hh:mm:ss a");
        dateFormats.add("MMM/dd/yyyy hh:mm:ss a");
        dateFormats.add("MMM dd, yyyy hh:mm:ss a");
        dateFormats.add("MMMM dd, yyyy hh:mm:ss a");
        dateFormats.add("yyyy-MM-dd hh:mm a");
        dateFormats.add("yyyy-MMM-dd hh:mm a");
        dateFormats.add("yyyy-MMMM-dd hh:mm a");
        dateFormats.add("MM-dd-yyyy hh:mm a");
        dateFormats.add("MMM-dd-yyyy hh:mm a");
        dateFormats.add("MM/dd/yyyy hh:mm a");
        dateFormats.add("yyyy/MM/dd hh:mm a");
        dateFormats.add("MMM/dd/yyyy hh:mm a");
        dateFormats.add("MMM dd, yyyy hh:mm a");
        dateFormats.add("MMMM dd, yyyy hm a");
        dateFormats.add("yyyy-MM-dd");
        dateFormats.add("yyyy-MMM-dd");
        dateFormats.add("yyyy-MMMM-dd");
        dateFormats.add("MM-dd-yyyy");
        dateFormats.add("MMM-dd-yyyy");
        dateFormats.add("MM/dd/yyyy");
        dateFormats.add("yyyy/MM/dd");
        dateFormats.add("MMM/dd/yyyy");
        dateFormats.add("MMM dd, yyyy");
        dateFormats.add("MMMM dd, yyyy");
        dateFormats.add("h:m a");
        dateFormats.add("H:m");
        dateFormats.add("H:m:s");
        dateFormats.add("hh:mm a");
        dateFormats.add("hh:mma");
        dateFormats.add("E MM/dd");
        dateFormats.add("EEEE MM/dd");
    }

    /**
     * Accepts a date as a String and converts it to java.util.Date. The date
     * will be converted based on the date format passed as "format". If format
     * is null, then "value" is assumed to be in the format of "yyyy-MM-dd".
     * 
     * @param value
     *            String value to convert
     * @param format
     *            Date Format
     * @return java.util.Date
     * @throws SystemException
     *             if "format" is not an acceptable java date format
     */
    public static final java.util.Date stringToDate(String value, String format)
            throws SystemException {

        SimpleDateFormat dateFormatter = null;
        try {
            if (value == null) {
                return null;
            }
            if (value.length() <= 0) {
                return null;
            }

            if (format == null) {
                dateFormatter = new SimpleDateFormat();
            }
            else {
                try {
                    dateFormatter = new SimpleDateFormat(format);
                } catch (IllegalArgumentException e) {
                    throw new SystemException("Date Format is invalid");
                }
            }
            dateFormatter.setLenient(false);
            java.util.Date nativeDate = dateFormatter.parse(value);
            if (nativeDate == null) {
                throw new SystemException(
                        RMT2SystemExceptionConst.MSG_INVALID_STRING_DATE_VALUE
                                + ": " + value + " formatted as: " + format,
                        RMT2SystemExceptionConst.RC_INVALID_STRING_DATE_VALUE);
            }

            return nativeDate;
        } catch (ParseException e) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_INVALID_STRING_DATE_VALUE
                            + ": " + value + " formatted as: " + format,
                    RMT2SystemExceptionConst.RC_INVALID_STRING_DATE_VALUE);
        }
    }

    /**
     * Accepts a date as a String and converts it to java.util.Date. The format
     * of, "value", is expected to match one of the date formats in the
     * ArrayList, "dateFormats".
     * 
     * @param value
     *            String value to convert
     * @return java.util.Date
     * @throws SystemException
     *             if a date format match is not found.
     */
    public static final java.util.Date stringToDate(String value)
            throws SystemException {
        String format = null;

        if (value == null || value.equalsIgnoreCase("null") || value.equals("")) {
            return null;
        }
        if (value.length() <= 0) {
            return null;
        }

        for (int ndx = 0; ndx < dateFormats.size(); ndx++) {
            format = (String) dateFormats.get(ndx);
            try {
                return stringToDate(value, format);
            } catch (SystemException ee) {
                // continue to the next date format, if available
            }
        }

        // throw exceptoin if date string could not be successfully converted
        String msg = "The date value entered is invalid, " + value;
        throw new SystemException(msg,
                RMT2SystemExceptionConst.RC_INVALID_STRING_DATE_VALUE);
    }

    /**
     * Creates a date object from a long value
     * 
     * @param dateInMillSecs
     * @return instance of {@link java.util.Date}
     */
    public static final Date toDate(long dateInMillSecs) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateInMillSecs);
        return c.getTime();
    }
    
    /**
     * Converts a String date in the form of W3C XML lexical representation to a
     * date String in the format of <i>yyyy-MM-dd HH:mm:ss</i>.
     * 
     * @param xmlDateStr
     *            lexical representation of the String date to convert
     * @return String String date in the format of <i>yyyy-MM-dd HH:mm:ss</i>
     * 
     * @throws SystemException
     */
    public static final String xmlDateStrToDateStr(String xmlDateStr)
            throws SystemException {
        java.util.Date dt = RMT2Date.xmlDateStrToDate(xmlDateStr);
        String result = RMT2Date.formatDate(dt, "yyyy-MM-dd HH:mm:ss");
        return result;
    }

    /**
     * Converts a String date in the form of W3C XML lexical representation to a
     * java.util.Date type.
     * 
     * @param xmlDateStr
     *            lexical representation of the String date to convert
     * @return java.util.Date
     * @throws SystemException
     */
    public static final java.util.Date xmlDateStrToDate(String xmlDateStr)
            throws SystemException {
        if (xmlDateStr == null) {
            return null;
        }
        try {
            XMLGregorianCalendar cal = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(xmlDateStr);
            GregorianCalendar gCal = cal.toGregorianCalendar();
            Date date = gCal.getTime();
            return date;
        } catch (DatatypeConfigurationException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Converts a java.util.Date instance to a XMLGregorianCalendar instance.
     * 
     * @param date
     *            instance of java.util.Date
     * @return XMLGregorianCalendar
     * @throws SystemException
     */
    public static final XMLGregorianCalendar toXmlDate(Date date) throws SystemException {
        if (date == null) {
            return null;
        }
        GregorianCalendar gDate = new GregorianCalendar();
        XMLGregorianCalendar xDate = null;
        try {
            if (date != null) {
                gDate.setTime(date);
                xDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDate);
            }
            return xDate;
        } catch (DatatypeConfigurationException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Converts a typical date String suitable for an instance of java.util.Date
     * to an instance of XMLGregorianCalendar.
     * 
     * @param dateStr
     *            a String representing a date suitable for java.util.Date
     *            instance.
     * @return XMLGregorianCalendar
     * @throws SystemException
     */
    public static final XMLGregorianCalendar toXmlDate(String dateStr)
            throws SystemException {
        Date dt = RMT2Date.stringToDate(dateStr);
        return RMT2Date.toXmlDate(dt);
    }

    /**
     * Accepts a java.util.Date object and converts it to a String in the format
     * of "_format". If _format is null, then _value is passed back to the
     * caller as is without any converting.
     * 
     * @param _value
     *            Date value to be converted.
     * @param _format
     *            The date format to be used for the conversion.
     * @return A String representation of _value.
     * @throws SystemException
     *             If _format is invalid or an unacceptable date format.
     */
    public static final String formatDate(java.util.Date _value, String _format)
            throws SystemException {
        if (_value == null) {
            return null;
        }
        if (_format == null) {
            return _value.toString();
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat(_format);
        return dateFormatter.format(_value);
    }

    /**
     * Combines the date parts of the current timestamp in the form of an String
     * and returns returns the results to the caller.
     * 
     * @param _date
     *            The target date to combine its parts.
     * @return An eight-byte string in the format of <yyyymmdd>. Returns null if
     *         _date is invalid.
     */
    public static final String combineDateParts(java.util.Date _date) {
        String result = null;
        String temp = null;
        int datePart = 0;
        if (_date == null) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(_date);
        datePart = gc.get(Calendar.YEAR);
        result = Integer.toString(datePart);

        datePart = gc.get(Calendar.MONTH);
        temp = Integer.toString(datePart + 1);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }
        result += temp;

        datePart = gc.get(Calendar.DAY_OF_MONTH);
        temp = Integer.toString(datePart);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }
        result += temp;

        return result;
    }

    /**
     * Obtains the actual dates of a given week from Sunday to Saturday (7 days)
     * using the current week.
     * 
     * @return An array of java.util.Date objects, seven to be exact, ordered
     *         from Sunday to Saturday.
     * @throws SystemException
     */
    public static final Date[] getWeekDates() throws SystemException {
        return RMT2Date.getWeekDates(new java.util.Date(), 0);
    }

    /**
     * Obtains the actual dates of a given week from Sunday to Saturday (7
     * days). The target week is determined based on the date _anchor is set.
     * 
     * @param _anchor
     *            The date used to target a particular week.
     * @return An array of java.util.Date objects, seven to be exact, ordered
     *         from Sunday to Saturday.
     * @throws SystemException
     */
    public static final Date[] getWeekDates(Date _anchor)
            throws SystemException {
        return RMT2Date.getWeekDates(_anchor, 0);
    }

    /**
     * Obtains the actual dates of a week which _anchor determines the week
     * targeted. _mode instructs this method to gather a subset of dates before
     * or after _anchor or gather all the dates of the targeted week. The target
     * week is determined based on the date _anchor is set.
     * 
     * @param _anchor
     *            The date used to target a particular week.
     * @param _mode
     *            Indicates the range of dates to obtain for the target week. 0
     *            = all dates, 1 = all dates after _anchor, and -1 all date
     *            before _anchor.
     * @return An array of java.util.Date objects.
     * @throws SystemException
     *             if _anchor is invalid.
     */
    public static final Date[] getWeekDates(Date _anchor, int _mode)
            throws SystemException {
        final int MODE_ALL = 0;
        final int MODE_PRIOR = -1;
        final int MODE_SUBSEQUENT = 1;
        int anchorDow = 0;
        int priorDowCount = 0;
        int subsequentDowCount = 0;
        int count = 0;
        int dowIncr = 0;
        int dateCount = 0;
        Calendar anchorDate = Calendar.getInstance();

        // Convert anchor date to Calendar type
        if (_anchor == null) {
            throw new SystemException(
                    "Anchor date is invalid for method getDayOfWeekDates");
        }

        // Do initializations
        anchorDate.setTime(_anchor);
        anchorDow = anchorDate.get(Calendar.DAY_OF_WEEK);
        priorDowCount = anchorDow - RMT2Date.DOW_FIRST;
        subsequentDowCount = RMT2Date.DOW_LAST - anchorDow;

        switch (_mode) {
            case MODE_ALL:
                dateCount = RMT2Date.DOW_TOTAL;
                break;
            case MODE_PRIOR:
                dateCount = priorDowCount;
                break;
            case MODE_SUBSEQUENT:
                dateCount = subsequentDowCount;
                break;
        }

        Date dates[] = new Date[dateCount];

        // Get dates prior to anchor day of the week
        if (_mode == MODE_ALL || _mode == MODE_PRIOR) {
            dowIncr = priorDowCount * -1;
            for (count = 0; count < priorDowCount; count++) {
                if (count > 0) {
                    dowIncr = 1;
                }
                anchorDate.add(Calendar.DATE, dowIncr);
                dates[count] = anchorDate.getTime();
            }
        }

        // Add anchor date to list of dates
        if (_mode == MODE_ALL) {
            dates[count++] = _anchor;
            anchorDate.add(Calendar.DATE, 1);
        }

        // Get dates subsequent to anchor day of the week
        if (_mode == MODE_ALL || _mode == MODE_SUBSEQUENT) {
            dowIncr = 1;
            for (int ndx = 0; ndx < subsequentDowCount; ndx++) {
                anchorDate.add(Calendar.DATE, dowIncr);
                dates[count++] = anchorDate.getTime();
            }
        }
        return dates;
    }

    // /**
    // * Calls doRowTimeStamp(sessionBean.getLoginId(), _dso, isNew) to
    // construct
    // * a timestamp value and assign that value to a Datasource object.
    // *
    // * @param _request
    // * The java request object.
    // * @param _dso
    // * {@link RMT2DataSourceApi) object.
    // * @param isNew
    // * true=create session, false=session exist.
    // * @throws SystemException
    // * @throws NotFoundException
    // * @throws DatabaseException
    // */
    // public static final void doRowTimeStamp(Request _request,
    // DataSourceApi _dso, boolean isNew) throws SystemException,
    // NotFoundException, DatabaseException {
    // Session session = _request.getSession();
    //
    // // Get User's Session Bean
    // RMT2SessionBean sessionBean = (RMT2SessionBean) session
    // .getAttribute(RMT2ServletConst.SESSION_BEAN);
    // doRowTimeStamp(sessionBean.getLoginId(), _dso, isNew);
    // }

/**
     * Constructs a timestamp value and assigns that value to a Datasource
     * object. Columns effected are base on whether the dso record already exist
     * (DateUpdated and Userid) or if the record is new (DateCreated,
     * DateUpdated, and Userid).
     * 
     * @param _userId
     *            The user id which timestamp is to be associated.
     * @param _dso
     *            {@link RMT2DataSourceApi) object.
     * @param isNew
     *            true=create session, false=session exist.
     * @throws SystemException
     *             When date format is invalid, or a problem existing setting
     *             column values of _dso.
     * @throws NotFoundException
     *             When invalid column name references are made towards _dso.
     * @throws DatabaseException
     *             An error occurred accessing _dso.
     */
    public static final void doRowTimeStamp(String _userId, DataSourceApi _dso,
            boolean isNew) throws SystemException, NotFoundException,
            DatabaseException {

        java.util.Date currentDate = new java.util.Date();

        if (isNew) {
            _dso.setColumnValue("DateCreated",
                    formatDate(currentDate, "MM-dd-yyyy H:m:s"));
        }
        _dso.setColumnValue("DateUpdated",
                formatDate(currentDate, "MM-dd-yyyy H:m:s"));
        _dso.setColumnValue("UserId", _userId);
    }

    /**
     * Creates a UserTimestamp object using the user's login id.
     * <p>
     * The login id will be set to "Unknown" when <i>loginId</i> equals null.
     * 
     * @param loginId
     *            The user's login id.
     * @return {@link UserTimestamp} conatining the data needed to satisfy a
     *         user timestamp.
     */
    public static final UserTimestamp getUserTimeStamp(String loginId) {
        if (loginId == null) {
            loginId = "Unknown";
        }
        java.util.Date currentDate = new java.util.Date();
        UserTimestamp ut = UserTimestamp.getInstance();
        ut.setDateCreated(currentDate);
        ut.setLoginId(loginId);
        return ut;
    }

    /**
     * Obtains the timestamp of the current date in milliseconds.
     * 
     * @return long.
     */
    public static final long getTimeStamp() {
        // Obtain timeStamp as a long
        long utilDate = (new java.util.Date()).getTime();
        return utilDate;
    }

    /**
     * Concatenates the input values "datePart" and "timePart" into a
     * java.util.Date value and return the results to the caller. Both parts of
     * the datetime value are validated.
     * 
     * @param datePart
     *            The date portion of a timestamp.
     * @param timePart
     *            The time portion of a timestamp.
     * @return java.util.Date
     * @throws BusinessException
     *             When validation(s) fail.
     * @throws SystemException
     *             When the derived timestamp value is unable to be converted to
     *             a Date object.
     */
    public static final java.util.Date combineDateTime(String datePart,
            String timePart) throws SystemException {
        String dtValue;

        // Determine if the date part was passed to this method
        if (datePart == null || datePart.length() <= 0) {
            throw new SystemException(
                    "Date is invalid.   Date part cannot be null or balnk",
                    -400);
        }

        // Determine if the date part was passed to this method
        if (datePart == null || datePart.length() <= 0) {
            throw new SystemException(
                    "Date is invalid.   TIme part cannot be null or balnk",
                    -401);
        }

        // combine date part with time part to yield a complete timestamp
        dtValue = datePart + " " + timePart;
        return RMT2Date.stringToDate(dtValue);

    }

    /**
     * Returns the difference in time between two dates as an ElapseTime object.
     * Caller is responsible for deciphering the contents of the return value.
     * Example: 5,600,000 milliseconds should return 1 hour, 33 minutes and 20
     * seconds.
     * 
     * @param _startDate
     *            The beginning of the date range.
     * @param _endDate
     *            The ending of the date range.
     * @return {@link ElapsedTime) object with positive values when _endDate is
     *         greater than 0r equal to _startDate. Otherwise, the ElapseTime
     *         object will contain negative values.
     */
    public static final ElapsedTime getDateDiff(Date _startDate, Date _endDate) {
        ElapsedTime et = null;
        long msInHr = 3600000; // Total number of milliseconds in 1 hour
        long msInMin = 60000; // Total number of milliseconds in 1 minute
        long msInSec = 1000; // Total number of milliseconds in 1 second
        double dateDiffMs = 0; // Elapsed time in milliseconds
        BigDecimal totalTime;
        double timeRemain = 0;
        int temp;
        int quotientScale = 5;

        try {
            et = new ElapsedTime();
        } catch (SystemException e) {
        }

        dateDiffMs = getDateDiff(_startDate, _endDate, "ms");

        // if (dateDiffMs == -1) {
        // return null;
        // }
        // Starting time
        totalTime = new BigDecimal(dateDiffMs);

        // Hours
        if (Math.abs(totalTime.intValue()) >= msInHr) {
            // Get left over minutes from hour calculation
            timeRemain = totalTime.intValue() % msInHr;

            // Hours
            totalTime = totalTime.divide(new BigDecimal(msInHr), quotientScale,
                    BigDecimal.ROUND_DOWN);
            temp = totalTime.intValue();
            et.setHours(temp);

            // Reset total time
            totalTime = new BigDecimal(timeRemain);
        }

        // Minutes
        if (Math.abs(totalTime.intValue()) >= msInMin) {
            // Get left over seconds from minute calculaion
            timeRemain = totalTime.intValue() % msInMin;

            // Calculate total minutes
            totalTime = totalTime.divide(new BigDecimal(msInMin),
                    quotientScale, BigDecimal.ROUND_DOWN);
            temp = totalTime.intValue();
            et.setMins(temp);

            // Reset total time
            totalTime = new BigDecimal(timeRemain);
        }

        // Seconds
        if (Math.abs(totalTime.intValue()) >= msInSec) {
            // Get left over milliseconds from second calculation
            timeRemain = totalTime.intValue() % msInSec;

            // Calculate total seconds
            totalTime = totalTime.divide(new BigDecimal(msInSec),
                    quotientScale, BigDecimal.ROUND_DOWN);
            temp = totalTime.intValue();
            et.setSecs(temp);

            // Reset total time
            totalTime = new BigDecimal(timeRemain);
        }

        // Set Milliseconds to remaining total time
        et.setMs(totalTime.intValue());

        return et;
    }

    /**
     * Returns the difference in time between two dates. The different types
     * time intervals returned can be:
     * <p>
     * <table border="1">
     * <tr>
     * <th align="left">Type</th>
     * <th align="left">Input Argument Value</th>
     * </tr>
     * <tr>
     * <td>milliseconds</td>
     * <td>ms</td>
     * </tr>
     * <tr>
     * <td>seconds</td>
     * <td>sec</td>
     * </tr>
     * <tr>
     * <td>minutes</td>
     * <td>min</td>
     * </tr>
     * <tr>
     * <td>hours</td>
     * <td>hr</td>
     * </tr>
     * <tr>
     * <td>days</td>
     * <td>d</td>
     * </tr>
     * </table>
     * 
     * @param _startDate
     *            The beginning of the date range.
     * @param _endDate
     *            The ending of the date range.
     * @param _intervalType
     *            The time interval type.
     * @return The time interval
     */
    public static final double getDateDiff(Date _startDate, Date _endDate,
            String _intervalType) {
        long endDate_ms;
        long startDate_ms;
        double dateDiff;
        double dateDiffSec;
        double dateDiffMin;
        double dateDiffHr;
        // int hours;
        // int mins;
        // float minsFraction;
        // BigDecimal temp;

        // Get start and end date times in milliseconds
        endDate_ms = _endDate.getTime();
        startDate_ms = _startDate.getTime();
        dateDiff = endDate_ms - startDate_ms; // get total elapsed time in
        // milliseconds
        dateDiffSec = dateDiff / 1000; // get total elapsed time in seconds
        dateDiffMin = dateDiffSec / 60; // get total elapsed time in minutes
        dateDiffHr = dateDiffMin / 60; // get get total elapsed time in hours

        if (_intervalType == null || _intervalType.equalsIgnoreCase("ms")) {
            return dateDiff;
        }
        if (_intervalType.equalsIgnoreCase("sec")) {
            return dateDiffSec;
        }
        if (_intervalType.equalsIgnoreCase("min")) {
            return dateDiffMin;
        }
        if (_intervalType.equalsIgnoreCase("hr")) {
            return dateDiffHr;
        }

        return -1;
        /*
         * // Parse the right and left sides of the deciimal point of dateDiff
         * hours = new Float(dateDiffHr).intValue(); // Extract hours
         * minsFraction = dateDiffHr - hours; // Extract minutes fraction temp =
         * new BigDecimal(minsFraction); // Convert fractional portion to actual
         * minutes temp = temp.divide(new BigDecimal(1), 2,
         * BigDecimal.ROUND_DOWN); minsFraction = temp.floatValue(); // Get
         * Fractional notation of minutes mins = new Float(minsFraction *
         * 60).intValue(); // Calulate actual minutes // Determine what quarter
         * of an hour mins represents such as // 15 minutes after the hour, 30
         * minutes after the hour, or // 45 minutes after the hour. Afterwards,
         * concatenate the hours // and minutes to return the total time. if
         * (mins >= 0 & mins < 15) { return hours + .0; } if (mins >= 15 & mins
         * < 30) { return hours + .25; } if (mins >= 30 & mins < 45) { return
         * hours + .5; } if (mins >= 45 & mins <= 59) { return hours + .75; }
         * 
         * return 0;
         */
    }

    /**
     * Increments a Date instance by a specified value. The date can be
     * incremented by <i>incAmt</i> and <i>dateField</i> determines if
     * <i>incAmt</i> increments the date on the basis of years, months, or days.
     * 
     * @param dt
     *            A valid insatnce of java.util.Date that is to be incremented.
     * @param dateField
     *            Specifies what date field is targeted to be incremented. Valid
     *            values are Calendar.YEAR, Calendar.MONTH, or Calendar.DATE.
     * @param incAmt
     *            A positive integer representing the amount to increment the
     *            speicified date field by. A negative amount will be changed to
     *            its absolute value.
     * @return long The incremented time in milli seconds, -1 when <i>dt</i> is
     *         invalid or null, or -2 when <i>dateField</i> is of an invalid
     *         value.
     */
    public static final long incrementDate(Date dt, int dateField, int incAmt) {
        if (dt == null) {
            logger.log(Level.ERROR,
                    "Problem incrementing date...Date instance is invalid");
            return -1;
        }
        if (dateField != Calendar.DATE && dateField != Calendar.MONTH
                && dateField != Calendar.YEAR) {
            logger.log(Level.ERROR,
                    "Problem incrementing date...Specified date field is invalid");
            return -2;
        }
        if (incAmt < 0) {
            logger.log(Level.WARN, "found a negative increment value ["
                    + incAmt + "] and converted to positive");
            incAmt = Math.abs(incAmt);
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(dateField, incAmt);
        return cal.getTimeInMillis();
    }

    /**
     * Decrements a Date instance by a specified value. The date can be
     * decremented by <i>decAmt</i> and <i>dateField</i> determines if
     * <i>decAmt</i> decrements the date on the basis of years, months, or days.
     * 
     * @param dt
     *            A valid insatnce of java.util.Date that is to be decremented.
     * @param dateField
     *            Specifies what date field is targeted to be decremented. Valid
     *            values are Calendar.YEAR, Calendar.MONTH, or Calendar.DATE.
     * @param decAmt
     *            A negative integer representing the amount to decrement the
     *            speicified date field by. A positive amount will be changed to
     *            a negative value.
     * @return long The decremented time in milli seconds, -1 when <i>dt</i> is
     *         invalid or null, or -2 when <i>dateField</i> is of an invalid
     *         value.
     */
    public static final long decrementDate(Date dt, int dateField, int decAmt) {
        if (dt == null) {
            logger.log(Level.ERROR,
                    "Problem decrementing date...Date instance is invalid");
            return -1;
        }
        if (dateField != Calendar.DATE && dateField != Calendar.MONTH
                && dateField != Calendar.YEAR) {
            logger.log(Level.ERROR,
                    "Problem decrementing date...Specified date field is invalid");
            return -2;
        }
        if (decAmt >= 0) {
            logger.log(Level.WARN, "found a positive decrement value ["
                    + decAmt + "] and converted to negative");
            decAmt *= -1;
        }
        Calendar cal = new GregorianCalendar();
        cal.add(dateField, decAmt);
        return cal.getTimeInMillis();
    }

    /**
     * Determines if <i>format</i> is a valid date format
     * 
     * @param format
     *            the date format to test.
     * @return boolean true when <i>format</i> matches, otherwise false is
     *         returned.
     */
    public static boolean isFormat(String format) {
        for (int ndx = 1; ndx < dateFormats.size(); ndx++) {
            if (((String) dateFormats.get(ndx)).equals(format)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes an n amount of seconds and converts to a List where each List
     * element is a parsed representation of the time in HH:MM:SS format.
     * 
     * @param seconds
     * @return
     */
    public static List<Integer> convertSecondsToList(int seconds) {
        int remain = 0;
        int hours = seconds / 3600;
        remain = seconds % 3600;
        int mins = remain / 60;
        int secs = remain % 60;

        List<Integer> list = new ArrayList<Integer>();

        list.add(hours);
        list.add(mins);
        list.add(secs);
        return list;
    }

    /**
     * Takes an n amount of seconds and converts to a String in HH:MM:SS format.
     * First, the hour is obtained by dividing the total seconds by 3600, then
     * take the remainder of 3600 and divide that by 60 to get minutes, and get
     * the same remainder's remainder to get seconds. The last step is to figure
     * out if each value for the hour, minute, and second requires a leading
     * zero (when value is less than 10, add a 0 to it).
     * 
     * @param seconds
     * @return
     */
    public static String formatSecondsToHHMMSS(int seconds) {
        int remain = 0;
        int hours = seconds / 3600;
        remain = seconds % 3600;
        int mins = remain / 60;
        int secs = remain % 60;

        StringBuffer strResult = new StringBuffer();
        strResult.append((hours < 10 ? "0" : "") + hours);
        strResult.append(":");
        strResult.append((mins < 10 ? "0" : "") + mins);
        strResult.append(":");
        strResult.append((secs < 10 ? "0" : "") + secs);

        return strResult.toString();
    }

    /**
     * Get current timezone based on server's current UTC.
     * 
     * @return timezone id such as <i>America/Chicago</i> or <i>US/Arizona</i>
     */
    public static String getCurrentTimezoneId() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getID();
    }
    
    /**
     * Calculate and return the number of days between two dates.
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws RuntimeException
     *             when <i>startDate</i> or <i>endDate</i> is null.
     */
    public static long getTimeDiffDays(Date startDate, Date endDate) {
        if (startDate == null) {
            throw new RuntimeException("Start date is required");
        }
        if (endDate == null) {
            throw new RuntimeException("End date is required");
        }
        long diff = 0;
        long timeDiff = Math.abs(startDate.getTime() - endDate.getTime());
        diff = TimeUnit.MILLISECONDS.toDays(timeDiff);
        return diff;
    }
} // end class

