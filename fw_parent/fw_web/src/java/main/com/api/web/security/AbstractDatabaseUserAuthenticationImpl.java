package com.api.web.security;

import org.apache.log4j.Logger;

//import com.api.persistence.db.DatabaseTransApi;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.AuthorizationException;
import com.api.web.Request;

/**
 * An abstract class that provides common functionality for user authentication,
 * authorization, and logoff where the data source is some form of a physical
 * database repository.
 * <p>
 * The implementor of this class will be required to provide logic to obtain an
 * appropriate instance of DatabaseTransApi which will encapsulate the database
 * connection.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractDatabaseUserAuthenticationImpl extends
        AbstractUserAuthenticationTemplateImpl {

    private static final Logger logger = Logger
            .getLogger("AbstractDatabaseUserAuthenticationImpl");

    private boolean singleSignOn;

    // private DatabaseTransApi tx;

    /**
     * Default constructor
     */
    public AbstractDatabaseUserAuthenticationImpl() {
        super();
        this.singleSignOn = false;
    }

    public AbstractDatabaseUserAuthenticationImpl(Request request) {
        this();
        this.request = request;
    }

    public AbstractDatabaseUserAuthenticationImpl(Request request,
            boolean singleSignOn) {
        this(request);
        this.singleSignOn = singleSignOn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.authentication.web.UserAuthenticator#authenticate()
     */
    public RMT2SecurityToken authenticate() throws AuthenticationException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.UserAuthenticator#authenticate(java
     * .lang .String, java.lang.String, java.lang.String, java.lang.String)
     */
    public RMT2SecurityToken authenticate(String loginId, String password,
            String appCode, String sessionId) throws AuthenticationException,
            AuthorizationException {
        RMT2SecurityToken token = (RMT2SecurityToken) this.process(loginId,
                password, appCode, sessionId);
        return token;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.security.authentication.web.AbstractUserAuthenticationTemplateImpl
     * #cleanUp()
     */
    @Override
    protected void cleanUp() throws AuthenticationException {
        if (this.isError()) {
            // this.tx.rollbackUOW();
            logger.info("Authentication / Authorization database transactions were rolled back");
        }
        else {
            // this.tx.commitUOW();
            logger.info("Authentication / Authorization database transactions were committed");
        }
        // this.tx.close();
        // this.tx = null;
    }

    /**
     * @return the singleSignOn
     */
    public boolean isSingleSignOn() {
        return singleSignOn;
    }
    //
    // /**
    // * @param tx the tx to set
    // */
    // public void setTx(DatabaseTransApi tx) {
    // this.tx = tx;
    // }
    //
    // /**
    // * @return the tx
    // */
    // public DatabaseTransApi getTx() {
    // return tx;
    // }
}
