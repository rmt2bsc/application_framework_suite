package com;

/**
 * An exception class for handling application intialization errors.
 * 
 * @author rterrell
 *
 */
public class GuiEnvSetupException extends RMT2RuntimeException {

    private static final long serialVersionUID = -7667239669740064955L;

    /**
     * 
     */
    public GuiEnvSetupException() {
        super();
    }

    /**
     * @param message
     */
    public GuiEnvSetupException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public GuiEnvSetupException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GuiEnvSetupException(String message, Throwable cause) {
        super(message, cause);
    }

}
