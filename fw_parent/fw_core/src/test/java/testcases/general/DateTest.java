package testcases.general;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.util.RMT2Date;

/**
 * Test the date/time framework utility classes
 * 
 * @author Roy TErrell
 * 
 */
public class DateTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testXmlDateStringConversion() {
        String dateStr = "2007-08-30T03:10:23.120-05:00";
        Date dt = RMT2Date.xmlDateStrToDate(dateStr);
        System.out.println(dt);
        String result = RMT2Date.xmlDateStrToDateStr(dateStr);
        System.out.println(result);
    }

    @Test
    public void testXmlDateConversion() {
        String dateStr = "2007-08-30T03:10:23.120-05:00";
        try {
            XMLGregorianCalendar cal = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(dateStr);
            GregorianCalendar gCal = cal.toGregorianCalendar();
            Date date = gCal.getTime();
            date = RMT2Date.stringToDate(dateStr);
            System.out.println(date.toString());
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testSecondConversion() {
        int seconds = 128;
        List<Integer> list = RMT2Date.convertSecondsToList(seconds);
        String format = RMT2Date.formatSecondsToHHMMSS(seconds);
        System.out.println(format);
    }

    @Test
    public void testStringToDecimal() {
        String source = "9/1/2009";
        DateFormat format = DateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "10/11/2009 03:30:40";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "2009-01-12";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "2009-01-12 03:30:40";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = new Date().toString();
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "22/44/2012";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "07/10/96 4:5 PM, PDT";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "May 15, 2009";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

        source = "S323.00";
        pos = new ParsePosition(0);
        dt = format.parseObject(source, pos);
        if (dt != null) {
            System.out.println("Results: " + dt.toString());
            System.out.println("String Length: " + source.length());
            System.out.println("Last Pos Parsed: " + pos.getIndex());
            System.out.println("Error Pos: " + pos.getErrorIndex());
        }

    }
    
    /**
     * Test of getXMLGergorianCalendar method, of class Utility.
     */
    @Test
    public void testGetXMLGergorianCalendarUsingJavaUtilDate() {
        Date dt = new Date();
        try {
            Assert.assertNotNull(RMT2Date.toXmlDate(dt));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    
    /**
     * Test of getXMLGergorianCalendar method, of class Utility.
     */
    @Test
    public void testGetXMLGergorianCalendarUsingNullDate() {
        Date nullDate = null;
        try {
            Assert.assertNull(RMT2Date.toXmlDate(nullDate));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
}
    
    
    @Test
    public void testNumberOfDaysBetweenDates() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(2010, 7, 23);
        end.set(2010, 8, 26);
        Date startDate = start.getTime();
        Date endDate = end.getTime();

        long days = RMT2Date.getTimeDiffDays(startDate, endDate);
        Assert.assertEquals(34, days);
    }

    @Test
    public void testNumberOfDaysBetweenDates_NullStartDate() {
        Calendar end = Calendar.getInstance();
        end.set(2010, 8, 26);
        Date endDate = end.getTime();

        try {
            long days = RMT2Date.getTimeDiffDays(null, endDate);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void testNumberOfDaysBetweenDates_NullEndDate() {
        Calendar start = Calendar.getInstance();
        start.set(2010, 7, 23);
        Date startDate = start.getTime();

        try {
            long days = RMT2Date.getTimeDiffDays(startDate, null);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }
}
