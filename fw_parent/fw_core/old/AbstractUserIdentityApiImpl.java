package com.api.foundation;

import org.apache.log4j.Logger;



/**
 * An abstract class designed to provide some way to identify the user of an API implementation.
 * <p>
 * The idea is to provide API developers the ability to track the user who is utilizing the 
 * his/her API.
 * 
 * @author rterrell
 *
 */
public abstract class AbstractUserIdentityApiImpl extends AbstractApiImpl {
    
    private static Logger logger;

//    protected String loginId;
    
    
    /**
     * Create a AbstractUserIdentityApiImpl object which initializes the logger.
     */
    protected AbstractUserIdentityApiImpl() {
	super();
	AbstractUserIdentityApiImpl.logger = Logger.getLogger(AbstractUserIdentityApiImpl.class);
	return;
    }
    
//    /**
//     * Create an {@link AbstractUserIdentityApiImpl} object identifying the user who is accessing the API.
//     * 
//     * @param loginId
//     *          the login id of the user.
//     */
//    public AbstractUserIdentityApiImpl(String loginId) {
//	this();
//	this.loginId = loginId;
//	logger.info(AbstractUserIdentityApiImpl.class.getName() + " was initialized.  Api is accessed by: " + (loginId == null ? "User Unknown" : loginId));
//    }

}
