package com.api.security.creditcard;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Manages details pertaining to credit card identification such as credit card
 * number, credit card verication code, transaction amount, and etc.
 * 
 * @author RTerrell
 * 
 */
class CreditCardIdentification extends RMT2BaseBean {

    private static final long serialVersionUID = -4988831345568648379L;

    private String id;

    private String expiryDate;

    private String verificationCode;

    private String xactAmount;

    private String type;

    public CreditCardIdentification() throws SystemException {
        super();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    public void initBean() throws SystemException {
        // TODO Auto-generated method stub

    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate
     *            the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the verificationCode
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * @param verificationCode
     *            the verificationCode to set
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     * @return the xactAmount
     */
    public String getXactAmount() {
        return xactAmount;
    }

    /**
     * @param xactAmount
     *            the xactAmount to set
     */
    public void setXactAmount(String xactAmount) {
        this.xactAmount = xactAmount;
    }

}
