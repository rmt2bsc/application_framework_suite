package com.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;

import com.InvalidDataException;
import com.SystemException;
import com.constants.RMT2SystemExceptionConst;

/**
 * Class designed to translate numbers into words.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2Money {

    private final int HUNDREDS = 1;

    private final int THOUSANDS = 2;

    private final int MILLIONS = 3;

    private final int BILLIONS = 4;

    private final int TRILLIONS = 5;

    // private final int NUMTYPE_MONEY = 1;
    //
    // private final int NUMTYPE_WEIGHT = 2;
    //
    // private final int NUMTYPE_METRIC = 3;

    private int numberType;

    /**
     * Different number formats
     */
    private static ArrayList numberFormats = new ArrayList();
    static {
        numberFormats.add("$#,##0.00");
        numberFormats.add("$###0.00");
        numberFormats.add("$###0");
        numberFormats.add("$#,##0.00;($#,##0.00)");
        numberFormats.add("$###0.00;($###0.00)");
        numberFormats.add("$###0;($###0)");
        numberFormats.add("($###0.00)");
        numberFormats.add("($###0)");
        numberFormats.add("(###0)");
        numberFormats.add("(###0.00)");
        numberFormats.add("###0.00");
        numberFormats.add("$###0");
        numberFormats.add("###0");
        numberFormats.add("###0.00");
    }

    /**
     * The default constructor
     * 
     */
    public RMT2Money() {
    }

    /**
     * Constructs a RMT2Money object using the number type.
     * 
     * @param _numberType
     *            int
     */
    public RMT2Money(int _numberType) {
        this();
        this.numberType = _numberType;
    }

    /**
     * Translates a decimal value into words.
     * 
     * @param value
     *            String value of the number to translate
     * @return The word translation in English
     * @throws Exception
     */
    public String toWords(String value) throws Exception {

        String spelling = " ";
        String dollars = null;
        String cents = null;
        String strValue = null;
        String unitOfMeasure = "";
        String prev = null;
        int ndx = 0;
        int sections = 0;
        int partialSectionCount = 0;
        int len = 0;
        boolean dollarCalcComplete = false;

        try {
            // Get the expression to be evaluated
            strValue = new BigDecimal(value).toString();

            // Parse the left and right of decimal.
            ndx = strValue.indexOf(".");

            // If the decimal was found store the whole and
            // fractional values of the decimal number.
            // Otherwise, just store the whole value.
            if (ndx > -1) {
                dollars = strValue.substring(0, ndx);
                cents = strValue.substring(ndx + 1, ndx + 3);
            }
            else {
                dollars = strValue;
                cents = null;
            }

            // If the whole value is less than 100, translate that value
            // and flag the whole number translation process as being
            // completed. If the number is greater than 100 then determine
            // the total number of sections of the whole number part. Example:
            // 54,678,666.00 will have two complete sections and one that is two
            // thirds
            // of a section. A complete section is one that comprises digits in
            // groups
            // of three's such as "666" and "678".
            len = dollars.length();
            switch (len) {
                case 2:
                    spelling = this.xlateTens(dollars);
                    dollarCalcComplete = true;
                    break;
                case 1:
                    spelling = this.xlateOnesHundreds(dollars);
                    dollarCalcComplete = true;
                    break;
                case 0:
                    spelling = "";
                default:
                    sections = len / 3;
                    partialSectionCount = len % 3;
            }

            // If the whole number translation process is complete,
            // then translate the fractional portion of the number,
            // if applicable, combine both translations, and return
            // to caller.
            if (dollarCalcComplete) {
                spelling = this.finalizeSpelling(cents, spelling);
                return spelling;
            }

            // If both completed and partial sections exist, then recursively
            // translate
            // all sections to the right of the partial section.
            if (sections >= 1 && partialSectionCount > 0) {
                prev = " "
                        + this.toWords(dollars.substring(partialSectionCount));
            }

            // If only complete sections exist, then recursivley processs the
            // remaining
            // completed sections from right to left until there are no more
            // sections to
            // process.
            if (sections > 1 && partialSectionCount <= 0) {
                prev = " " + this.toWords(dollars.substring(3));
            }

            // Get the unit of measure for the left most section of the portion
            // of the whole
            // being processed. Example: Trillion, Billion, Million, and
            // Thousands.
            unitOfMeasure = this.determineDollarMeasurement(sections,
                    partialSectionCount);

            // Perform the actual word translation of a complete section.
            if (partialSectionCount == 0) {
                spelling = this.xlateOnesHundreds(dollars.substring(0, 1))
                        + "Hundred ";
                spelling += this.xlateTens(dollars.substring(1, 3));
            }

            // Perform the actual word translation of a partial section with
            // only one digit.
            if (partialSectionCount == 1) {
                spelling = this.xlateOnesHundreds(dollars.substring(0, 1));
            }
            // Perform the actual word translation of a partial section with
            // only two digits.
            if (partialSectionCount == 2) {
                spelling += this.xlateTens(dollars.substring(0, 2));
            }

            // Append the unit of measure to the wording of section just
            // translated.
            spelling += unitOfMeasure;

            // Append the word translations of previously translated sections
            // from recursive calls.
            spelling += (prev == null ? "" : prev);

            // Unite the dollar and cents translations
            spelling = finalizeSpelling(cents, spelling);

            return spelling;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Concatenates the literal, "dollar", with the dollar translation as well
     * as the literal, "cents", with the cents translation. Lastly, both dollar
     * and cents translations are combined, if applicable.
     * 
     * @param _cents
     *            The decimal portion of the number.
     * @param _spelling
     *            The significant portion of the translation
     * @return The finalized translation.
     * @throws Exception
     */
    private String finalizeSpelling(String _cents, String _spelling)
            throws Exception {
        String temp = "";
        if (_cents != null) {
            _spelling = (_spelling.equals("") ? "" : _spelling + " dollars ");
            temp = this.xlateTens(_cents);
            temp = (temp.equals("") ? "" : temp + " cents");
            _spelling = (!_spelling.equals("") && !temp.equals("") ? _spelling
                    + " and " + temp : _spelling + temp);
        }
        return _spelling;
    }

    /**
     * Translates the first two right most digits of a section into words.
     * Example: 432 - processes (32)
     * 
     * @param value
     *            Two digit values that represent the ones and tens place of a
     *            section
     * @return String Translation
     * @throws Exception
     */
    protected String xlateTens(String value) throws Exception {

        String spelling = null;

        if (value.length() != 2) {
            throw new Exception(
                    "Error: value must be a length of 2 in order to translate the tens position.");
        }

        try {
            switch (new Integer(value.substring(0, 1)).intValue()) {

            // If the first digit is zero then translate values from 1 through 9
                case 0:
                    spelling = this.xlateOnesHundreds(value.substring(1));
                    break;

                // If the first digit is one then translate values from 10
                // through
                // 19
                case 1:
                    switch (new Integer(value.substring(1)).intValue()) {
                        case 0:
                            spelling = "Ten ";
                            break;
                        case 1:
                            spelling = "Eleven ";
                            break;
                        case 2:
                            spelling = "Twelve ";
                            break;
                        case 3:
                            spelling = "Thirteen ";
                            break;
                        case 4:
                            spelling = "Fourteen ";
                            break;
                        case 5:
                            spelling = "Fifteen ";
                            break;
                        case 6:
                            spelling = "Sixteen ";
                            break;
                        case 7:
                            spelling = "Seventeen ";
                            break;
                        case 8:
                            spelling = "Eighteen ";
                            break;
                        case 9:
                            spelling = "Nineteen ";
                            break;
                    }
                    break;

                // If the first digit is 2 throug 9, then translate 20, 30,
                // 40...nth
                // into words
                case 2:
                    spelling = "Twenty";
                    break;
                case 3:
                    spelling = "Thirty";
                    break;
                case 4:
                    spelling = "Forty";
                    break;
                case 5:
                    spelling = "Fifty";
                    break;
                case 6:
                    spelling = "Sixty";
                    break;
                case 7:
                    spelling = "Seventy";
                    break;
                case 8:
                    spelling = "Eighty";
                    break;
                case 9:
                    spelling = "Ninty";
                    break;
            }

            // If the first digit is 2 through 9, then concatenate the
            // translation result of
            // the last digit.
            String temp = this.xlateOnesHundreds(value.substring(1));
            temp = (temp.equals("") ? " " : "-" + temp);

            switch (new Integer(value.substring(0, 1)).intValue()) {
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    spelling += temp;
            }

            return spelling;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Translates the ones postion of a section into words. This include the
     * numbers: 1,2,3,4,5,6,7,8,and 9. Zero will be interpretted as a blank
     * value.
     * 
     * @param value
     *            One digit values that represent the ones place of a section
     * @return English Translation
     * @throws Exception
     */
    protected String xlateOnesHundreds(String value) throws Exception {

        String spelling = null;

        if (value.length() > 1) {
            throw new Exception(
                    "Error: value is too long to translate either the ones or the hundreds position.");
        }

        try {
            switch (new Integer(value).intValue()) {
                case 0:
                    spelling = "";
                    break;
                case 1:
                    spelling = "One ";
                    break;
                case 2:
                    spelling = "Two ";
                    break;
                case 3:
                    spelling = "Three ";
                    break;
                case 4:
                    spelling = "Four ";
                    break;
                case 5:
                    spelling = "Five ";
                    break;
                case 6:
                    spelling = "Six ";
                    break;
                case 7:
                    spelling = "Seven ";
                    break;
                case 8:
                    spelling = "Eight ";
                    break;
                case 9:
                    spelling = "Nine ";
                    break;
            }

            return spelling;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Determines a unit of measurement number based on the total number of
     * complete sections for a number as well as the the total number of digits
     * that comprises the number's partial section.
     * 
     * @param sections
     *            total number of complete sectionstotal number of complete
     *            sections
     * @param remainder
     *            total number digits that make up a partial section
     * @return measurement translation
     */
    protected String determineDollarMeasurement(int sections, int remainder) {

        int totalSections = 0;
        String measurement = null;

        if (sections > 0 && remainder > 0) {
            totalSections = sections + 1;
        }
        if (sections > 0 && remainder <= 0) {
            totalSections = sections;
        }

        switch (totalSections) {
            case HUNDREDS:
                measurement = "";
                break;
            case THOUSANDS:
                measurement = "Thousand";
                break;
            case MILLIONS:
                measurement = "Million";
                break;
            case BILLIONS:
                measurement = "Billion";
                break;
            case TRILLIONS:
                measurement = "Trillion";
                break;
        }

        return measurement;
    }

    /**
     * Set the number type
     * 
     * @param value
     *            number type
     */
    public void setNumberType(int value) {
        this.numberType = value;
    }

    /**
     * Get the number type.
     * 
     * @return number type
     */
    public int getNumberType() {
        return this.numberType;
    }

    /**
     * Accepts a Number object and converts it to a String in the format of
     * "_format". If _format is null, then _value is passed back to the caller
     * as is without any converting.
     * 
     * @param _value
     *            The number for convert.
     * @param _format
     *            The format to apply to the conversion.
     * @return The String representation of _value.
     * @throws SystemException
     *             If _format is invalid or an unacceptable date format.
     */
    public static final String formatNumber(Number _value, String _format)
            throws SystemException {
        if (_value == null) {
            return null;
        }
        if (_format == null) {
            return _value.toString();
        }

        try {
            DecimalFormat decFormatter = new DecimalFormat(_format);
            return decFormatter.format(_value.doubleValue());
        } catch (IllegalArgumentException e) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_INVALID_DATE_FORMAT + ": "
                            + _format);
        }
    }

    /**
     * Accepts a number as a String and converts it to the java wrapper class,
     * Number. The format of, "_value", is expected to match one of the numeric
     * formats in the ArrayList, "numberFormats".
     * 
     * @param _value
     *            String value to convert
     * @return Number
     * @throws SystemException
     *             if a number format match is not found
     */
    public static final Number stringToNumber(String _value)
            throws SystemException {
        try {
            if (_value == null) {
                return null;
            }
            if (_value.length() <= 0) {
                return null;
            }
            DecimalFormat decFormatter = new DecimalFormat();
            return decFormatter.parse(_value);
        } catch (ParseException e) {
            String format = null;
            for (int ndx = 0; ndx < numberFormats.size(); ndx++) {
                format = (String) numberFormats.get(ndx);
                try {
                    return stringToNumber(_value, format);
                } catch (SystemException e2) {
                }
            }

            // throw exceptoin if number string could not be successfully
            // converted
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_INVALID_STRING_NUMBER_VALUE
                            + ": " + _value,
                    RMT2SystemExceptionConst.RC_INVALID_STRING_NUMBER_VALUE);
        }
    }

    /**
     * Accepts a number as a String and converts it to the java wrapper class,
     * Number. The number will be converted based on the numeric format passed
     * as "_format". If _format is null, then "_value" is assumed to be in a
     * form acceptable for parsing and converting _value without _format.
     * 
     * @param _value
     *            String value to convert
     * @param _format
     *            Number Format
     * @return Number
     * @throws SystemException
     *             if "format" is not an acceptable java numeric format
     */
    public static final Number stringToNumber(String _value, String _format)
            throws SystemException {
        DecimalFormat decFormatter = null;
        try {
            if (_value == null) {
                return null;
            }
            if (_value.length() <= 0) {
                return null;
            }

            if (_format == null) {
                decFormatter = new DecimalFormat();
            }
            else {
                decFormatter = new DecimalFormat(_format);
            }
            return decFormatter.parse(_value);
        } catch (IllegalArgumentException e) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_INVALID_NUMBER_FORMAT + ": "
                            + _format);
        } catch (ParseException e) {
            // throw exceptoin if number string could not be successfully
            // converted
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_INVALID_STRING_NUMBER_VALUE
                            + ": " + _value);
        }
    }

    /**
     * Verifies if a String expression is a number.
     * 
     * @param value
     *            The String expression to be evaluated.
     * @return boolean true if <i>value</i> is a number and false when invalid.
     */
    public static final boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses a formatted decimal value to Doulbe wrapper type.
     * 
     * @param strVal
     *            a String Deciaml value which can contain any numreric pattern.
     * @return Double when <i>strVal</i> is valid decimal or null when strVal
     *         parses with an error.
     */
    public Double stringToDouble(String strVal) {
        DecimalFormat format = new DecimalFormat();
        ParsePosition pos = new ParsePosition(0);
        // format.setParseBigDecimal(true);
        Number num = format.parse(strVal, pos);
        // Parsing error
        if (num != null || pos.getErrorIndex() >= 0) {
            return null;
        }

        if (strVal.length() > pos.getIndex()) {
            return null;
        }
        return new Double(num.doubleValue());
    }

    /**
     * Validates if <i>value</i> is invalid.
     * 
     * @param value
     *            a numeric expression
     * @throws InvalidDataException
     *             <ul>
     *             <li><i>value</i> is invalid</li>
     *             <li><i>value</i> is not numeric</li>
     *             <li><i>value</i> does not contain a decimal point</li>
     *             </ul>
     */
    public final static void validateMoney(String value)
            throws InvalidDataException {
        if (value == null) {
            String msg = "Input is invalid";
            throw new InvalidDataException(msg);
        }
        if (!RMT2Money.isNumeric(value)) {
            String msg = "Input is not numeric";
            throw new InvalidDataException(msg);
        }
        if (value.indexOf(".") == -1) {
            String msg = "Please enter an amount in dollars and cents.Be sure to use a decimal point. For example: 1234.56 or 413.00 or 1,234.78 or 345,678.00";
            throw new InvalidDataException(msg);
        }
    }
}
