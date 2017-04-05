package com.api.messaging.webservice.router;

import com.api.messaging.MessagingRouter;

/**
 * Factory for creating message routers.
 * 
 * @author Roy Terrell
 * 
 */
public class MessageRouterFactory {

    public static final MessagingRouter createSoapMessageRouter() {
        MessagingRouter router = new SoapMessageRouterImpl();
        return router;
    }

    public static final MessagingRouter createBasicMessageRouter() {
        MessagingRouter router = new BasicMessageRouterImpl();
        return router;
    }
}
