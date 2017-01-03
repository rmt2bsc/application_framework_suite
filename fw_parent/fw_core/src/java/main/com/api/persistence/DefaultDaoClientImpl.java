/**
 * 
 */
package com.api.persistence;

/**
 * @author Roy Terrell
 * 
 */
public class DefaultDaoClientImpl extends AbstractDaoClientImpl {

    /**
     * 
     */
    public DefaultDaoClientImpl() {
        super();
    }

    /**
     * @param contextName
     */
    public DefaultDaoClientImpl(String contextName) {
        super(contextName);
    }

    /**
     * @param client
     */
    public DefaultDaoClientImpl(PersistenceClient client) {
        super(client);
    }

}
