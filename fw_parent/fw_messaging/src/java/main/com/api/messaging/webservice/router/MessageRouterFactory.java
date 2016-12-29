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
        MessagingRouter router = new MessageRouterSoapImpl();
        return router;
    }

    public static final MessagingRouter createJaxbMessageRouter() {
        MessagingRouter router = new MessageRouterJaxbImpl();
        return router;
    }
}
