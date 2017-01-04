package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
class VisaProcessor extends AbstractCreditCardProcessor implements CreditCard {

    /**
     * 
     */
    public VisaProcessor() {
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
                    "Visa credit card identification object is invalid");
        }
        if (!this.containsValidCharacters(ccNo)) {
            throw new CreditCardException(
                    "Visa Credit card number must be composed of digits only");
        }
        if (this.doLuhnCheck(ccNo)) {
            throw new CreditCardException(
                    "Visa Credit card number is not accurate");
        }

        int VS_LENGTH1 = 13;
        int VS_LENGTH2 = 16;
        String VS_PREFIX = "4";
        boolean isVisa = false;
        String prefix = ccNo.substring(0, 1);
        isVisa = (prefix.equals(VS_PREFIX)) ? true : false;

        if (isVisa) {
            isVisa = (ccNo.length() == VS_LENGTH1)
                    || (ccNo.length() == VS_LENGTH2) ? true : false;
        }
        if (!isVisa) {
            throw new CreditCardException("Visa credit card number is invalid");
        }
    }

}
