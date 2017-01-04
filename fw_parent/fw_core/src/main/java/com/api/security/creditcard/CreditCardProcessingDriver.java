package com.api.security.creditcard;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.RMT2BaseBean;
import com.SystemException;

class CreditCardProcessingDriver extends RMT2BaseBean {
    private static final long serialVersionUID = 9077878516252125629L;

    private Logger logger;

    private Object creditCardAddr;

    private Object creditCardId;

    /**
     * 
     * @throws SystemException
     */
    public CreditCardProcessingDriver() throws SystemException {
        super();
        this.logger = Logger.getLogger("CreditCardProcessingDriver");
    }

    public void initBean() {
        this.logger = Logger.getLogger("CreditCardProcessingDriver");
    }

    public Object processCard(String cardType) throws CreditCardException {
        CreditCard cc = this.identifyCard(cardType);
        return this.chargeCard(cc);
    }

    protected CreditCard identifyCard(String cardType)
            throws CreditCardException {
        CreditCard cc = null;
        if (cardType == null) {
            this.msg = "Type of credit card was not made";
            logger.log(Level.ERROR, this.msg);
            throw new CreditCardException(this.msg);
        }
        if (cardType.equals(CreditCardFactory.TYPE_VISA)) {
            cc = CreditCardFactory.getVisaInstance();
        }
        else if (cardType.equals(CreditCardFactory.TYPE_DISCOVER)) {
            cc = CreditCardFactory.getDiscoverInstance();
        }
        else if (cardType.equals(CreditCardFactory.TYPE_MASTERCARD)) {
            cc = CreditCardFactory.getMasterCardInstance();
        }
        else if (cardType.equals(CreditCardFactory.TYPE_AMEX)) {
            cc = CreditCardFactory.getAmexInstance();
        }
        else if (cardType.equals(CreditCardFactory.TYPE_DINERS)) {
            cc = CreditCardFactory.getDinersClubInstance();
        }
        else {
            this.msg = "Invalid credit card selection: " + cardType;
            logger.log(Level.ERROR, this.msg);
            throw new CreditCardException(this.msg);
        }
        return cc;
    }

    protected Object chargeCard(CreditCard cc) throws CreditCardException {
        cc.validateAddress(this.creditCardAddr);
        cc.validateIdentification(this.creditCardId);
        return cc.processTransaction();
    }

    /**
     * @param creditCardAddr
     *            an arbitrary object representing credit card holder's address.
     */
    public void setCreditCardAddr(Object creditCardAddr) {
        this.creditCardAddr = creditCardAddr;
    }

    /**
     * @param creditCardId
     *            an arbitrary object representing credit card Identification.
     */
    public void setCreditCardId(Object creditCardId) {
        this.creditCardId = creditCardId;
    }

}
