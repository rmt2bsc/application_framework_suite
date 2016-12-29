package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
class DiscoverProcessor extends AbstractCreditCardProcessor implements
        CreditCard {

    /**
     * 
     */
    public DiscoverProcessor() {
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
                    "Discover credit card identification object is invalid");
        }
        if (!this.containsValidCharacters(ccNo)) {
            throw new CreditCardException(
                    "Disover Credit card number must be composed of digits only");
        }
        if (this.doLuhnCheck(ccNo)) {
            throw new CreditCardException(
                    "Disover Credit card number is not accurate");
        }

        int DS_LENGTH = 16;
        String DS_PREFIX = "6011";
        boolean isDiscover = false;
        String prefix = ccNo.substring(0, 4);
        isDiscover = (prefix.equals(DS_PREFIX)) ? true : false;
        if (isDiscover) {
            isDiscover = (ccNo.length() == DS_LENGTH) ? true : false;
        }
        if (!isDiscover) {
            throw new CreditCardException(
                    "Disover credit card number is invalid");
        }
    }

}
