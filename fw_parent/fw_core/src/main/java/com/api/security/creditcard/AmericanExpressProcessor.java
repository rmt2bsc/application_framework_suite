/**
 * 
 */
package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
class AmericanExpressProcessor extends AbstractCreditCardProcessor implements
        CreditCard {

    /**
     * 
     */
    public AmericanExpressProcessor() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.creditcard.CreditCard#processTransaction()
     */
    public Object processTransaction() throws CreditCardException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.creditcard.CreditCard#saveInfo()
     */
    public void saveInfo() throws CreditCardException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.creditcard.CreditCard#validateAddress(com.api.security
     * .creditcard.AddressVerification)
     */
    public void validateAddress(Object addr) throws CreditCardException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.creditcard.CreditCard#validateIdentification(com.api
     * .security.creditcard.CreditCardIdentification)
     */
    public void validateIdentification(Object id) throws CreditCardException {
        String ccNo;
        CreditCardIdentification cc;
        if (id != null && id instanceof CreditCardIdentification) {
            cc = (CreditCardIdentification) id;
            ccNo = cc.getId();
        }
        else {
            throw new CreditCardException(
                    "American Express credit card identification object is invalid");
        }
        if (!this.containsValidCharacters(ccNo)) {
            throw new CreditCardException(
                    "American Express Credit card number must be composed of digits only");
        }
        if (this.doLuhnCheck(ccNo)) {
            throw new CreditCardException(
                    "American Express Credit card number is not accurate");
        }

        int AX_LENGTH = 15;
        String AX_PREFIX1 = "34";
        String AX_PREFIX2 = "37";
        boolean isAmericanExpress = false;
        String prefix = ccNo.substring(0, 2);
        isAmericanExpress = (prefix.equals(AX_PREFIX1) || prefix
                .equals(AX_PREFIX2)) ? true : false;

        if (isAmericanExpress) {
            isAmericanExpress = (ccNo.length() == AX_LENGTH) ? true : false;
        }
        if (!isAmericanExpress) {
            throw new CreditCardException(
                    "American Express credit card number is invalid");
        }
    }

}
