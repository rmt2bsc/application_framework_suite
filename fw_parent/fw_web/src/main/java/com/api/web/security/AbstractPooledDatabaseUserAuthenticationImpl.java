package com.api.web.security;

import org.apache.log4j.Logger;





//import com.api.persistence.db.DatabaseTransApi;
//import com.api.persistence.db.DatabaseTransFactory;
import com.api.security.authentication.web.AuthenticationException;
import com.api.web.Request;
import com.api.web.security.UserAuthenticator;

/**
 * An abstract implementation of interface,
 * {@link com.api.security.authentication.web.UserAuthenticator UserAuthenticator}
 * which uses database connections coming from an object pool to perform user
 * authentication, authorization, and logoff.
 * <p>
 * The implementor of this class is responsible for providing specific database
 * logic that fulfills the remainder of the UseAuthenticator interface and
 * AbstractUserAuthenticationTemplateImpl contracts.
 * 
 * @author rterrell
 * 
 */
public abstract class AbstractPooledDatabaseUserAuthenticationImpl extends
        AbstractDatabaseUserAuthenticationImpl implements UserAuthenticator {

    private static final Logger logger = Logger
            .getLogger("AbstractPooledDatabaseUserAuthenticationImpl");

    /**
     * Default constructor
     */
    public AbstractPooledDatabaseUserAuthenticationImpl() {
        super();
        logger.info("Logger for AbstractPooledDatabaseUserAuthenticationImpl created");
    }

    public AbstractPooledDatabaseUserAuthenticationImpl(Request request) {
        super(request);
    }

    public AbstractPooledDatabaseUserAuthenticationImpl(Request request,
            boolean singleSignOn) {
        super(request, singleSignOn);
    }

    /**
     * Initializes an instance of DatabaseTransApi by getting a database
     * connection from the object pool.
     * 
     * @throws AuthenticationException
     */
    @Override
    protected void initialize() throws AuthenticationException {
        // DatabaseTransApi t = DatabaseTransFactory.create();
        // this.setTx(t);
    }

}
