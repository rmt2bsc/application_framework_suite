package com.util.assistants;

/**
 * Represents the result of a data verification failure. Application code should
 * generally avoid the construction of these exceptions directly. The
 * {@link Verifier Verifier} class provides methods for performing
 * verifications.
 * 
 * @author Roy Terrell
 */
public class VerifyException extends RuntimeException {

    private static final long serialVersionUID = -5234393338827989457L;

    /**
     * Constructs the verification exception with no message.
     */
    VerifyException() {}

    /**
     * Constructs the verification exception with the specified message.
     */
    VerifyException(String message) {
        super(message);
    }
}
