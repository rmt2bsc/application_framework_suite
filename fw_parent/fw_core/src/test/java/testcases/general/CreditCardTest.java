package testcases.general;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.util.RMT2String;

/**
 * Test credit card number parsing functionality
 * 
 * @author Roy Terrell
 *
 */
public class CreditCardTest {

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
    public void maskCreditCard() {
        String ccNo1 = "4442-3394-5943-3939";
        String amex = "4442-339459-33939";
        String nullNo = null;
        String results = RMT2String.maskCreditCardNumber(ccNo1);
        System.out.println(results);
        results = RMT2String.maskCreditCardNumber(amex);
        System.out.println(results);
        results = RMT2String.maskCreditCardNumber(nullNo);
        System.out.println(results);

    }
}
