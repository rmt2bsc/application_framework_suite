package com.api.security.creditcard;

/**
 * @author RTerrell
 * 
 */
public interface CreditCard {
    /**
     * 
     * @param id
     * @throws CreditCardException
     */
    void validateIdentification(Object id) throws CreditCardException;

    /**
     * 
     * @param addr
     * @throws CreditCardException
     */
    void validateAddress(Object addr) throws CreditCardException;

    /**
     * 
     * @return
     * @throws CreditCardException
     */
    Object processTransaction() throws CreditCardException;

    /**
     * 
     * @throws CreditCardException
     */
    void saveInfo() throws CreditCardException;
}
