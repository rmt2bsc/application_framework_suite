package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
class DinersClubProcessor extends AbstractCreditCardProcessor implements
        CreditCard {

    /**
     * 
     */
    public DinersClubProcessor() {
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
                    "Diner\'s Club Carte Blanche credit card identification object is invalid");
        }
        if (!this.containsValidCharacters(ccNo)) {
            throw new CreditCardException(
                    "Diner\'s Club Carte Blanche Credit card number must be composed of digits only");
        }
        if (this.doLuhnCheck(ccNo)) {
            throw new CreditCardException(
                    "Diner\'s Club Carte Blanche Credit card number is not accurate");
        }

        int DC_LENGTH = 14;
        int DC_PREFIX1 = 300;
        int DC_PREFIX2 = 305;
        String DC_PREFIX3 = "36";
        String DC_PREFIX4 = "38";
        boolean isDiners = false;
        String prefix = ccNo.substring(0, 2);
        isDiners = (prefix.equals(DC_PREFIX3) || prefix.equals(DC_PREFIX4)) ? true
                : false;

        if (!isDiners) {
            int intPrefix = Integer.parseInt(ccNo.substring(0, 3));
            isDiners = (intPrefix >= DC_PREFIX1 && intPrefix <= DC_PREFIX2) ? true
                    : false;
        }
        if (isDiners) {
            isDiners = (ccNo.length() == DC_LENGTH) ? true : false;
        }
        if (!isDiners) {
            throw new CreditCardException(
                    "Diner\'s Club/Carte Blanche credit card number is invalid");
        }
    }

}
