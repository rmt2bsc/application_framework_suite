package com.api.security.creditcard;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.RMT2Base;

/**
 * @author RTerrell
 * 
 */
public abstract class AbstractCreditCardProcessor extends RMT2Base {
    private Logger logger;

    /**
     * 
     */
    public AbstractCreditCardProcessor() {
        super();
        this.logger = Logger.getLogger("AbstractCreditCardProcessor");
    }

    protected boolean containsValidCharacters(String cardNumber) {
        StringBuffer buffer = new StringBuffer(cardNumber);
        boolean valid = true;
        char invalidCharacter = '\0';
        int i = 0;

        while (i < buffer.length()) {
            char ch = buffer.charAt(i);
            if (Character.isDigit(ch))
                i++;
            else if (Character.isWhitespace(ch))
                buffer.deleteCharAt(i);
            else {
                valid = false;
                invalidCharacter = ch;
                break;
            }
        }
        if (!valid) {
            this.msg = "Credit Card [ " + cardNumber + "]"
                    + " Bad Credit Card Character ["
                    + String.valueOf(invalidCharacter) + "]";
            logger.log(Level.ERROR, this.msg);
        }
        return valid;
    }

    protected boolean doLuhnCheck(String cardNumber) {
        int sum = 0;
        for (int i = cardNumber.length() - 1; i >= 0; i = -2) {
            sum += Integer.parseInt(cardNumber.substring(i - 1, i));
            if (i > 0) {
                int d = 2 * Integer.parseInt(cardNumber.substring(i - 1, i));
                if (d > 9) {
                    d -= 9;
                }
                sum += d;
            }
        }
        return sum % 10 == 0;
    }

}
