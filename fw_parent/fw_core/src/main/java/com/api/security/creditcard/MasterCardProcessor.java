package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
class MasterCardProcessor extends AbstractCreditCardProcessor implements
        CreditCard {

    /**
     * 
     */
    public MasterCardProcessor() {
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
                    "MasterCard credit card identification object is invalid");
        }
        if (!this.containsValidCharacters(ccNo)) {
            throw new CreditCardException(
                    "MasterCard Credit card number must be composed of digits only");
        }
        if (this.doLuhnCheck(ccNo)) {
            throw new CreditCardException(
                    "MasterCard Credit card number is not accurate");
        }

        int MC_LENGTH = 16;
        int MC_PREFIX1 = 51;
        int MC_PREFIX2 = 55;
        boolean isMasterCard = false;
        int prefix = Integer.parseInt(ccNo.substring(0, 2));
        isMasterCard = (prefix >= MC_PREFIX1 && prefix <= MC_PREFIX2) ? true
                : false;
        if (isMasterCard) {
            isMasterCard = (ccNo.length() == MC_LENGTH) ? true : false;
        }
        if (!isMasterCard) {
            throw new CreditCardException(
                    "MasterCard credit card number is invalid");
        }
    }

}
