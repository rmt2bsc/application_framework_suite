package com.api.messaging.webservice.router;


/**
 * Factory for creating message routers.
 * 
 * @author Roy Terrell
 * 
 */
public class MessageRouterFactory {

    /**
     * Create a message router specific for SOAP messages.
     * 
     * @return the SOAP implementation of {@link MessagingRouter}
     */
    public static final MessagingRouter createSoapMessageRouter() {
        MessagingRouter router = new SoapMessageRouterImpl();
        return router;
    }

    /**
     * Create a common message router.
     * 
     * @return the common implementation of {@link MessagingRouter}
     */
    public static final MessagingRouter createBasicMessageRouter() {
        MessagingRouter router = new BasicMessageRouterImpl();
        return router;
    }
}
