package com.api.security.creditcard;

import com.RMT2Exception;

/**
 * @author RTerrell
 * 
 */
public class CreditCardException extends RMT2Exception {
    private static final long serialVersionUID = 749903225419625851L;

    /**
     * 
     */
    public CreditCardException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public CreditCardException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param code
     */
    public CreditCardException(int code) {
        super(code);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param _msg
     * @param _code
     */
    public CreditCardException(String _msg, int _code) {
        super(_msg, _code);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public CreditCardException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

}
