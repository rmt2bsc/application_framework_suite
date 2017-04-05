package com.api.messaging.webservice.router;

import com.api.messaging.handler.MessageHandlerResults;

/**
 * A messaging contract for routing a message to its destination independent of
 * any transport protocols.
 * <p>
 * If some sort of service registry is used, the implementation of this
 * interface is required to provide the means of loading and maintaining the
 * registry of services. It is up to the implementor as to what and how the
 * structure of the service registry is mandated.
 * 
 * @author Roy Terrell
 * 
 */
public interface MessagingRouter {
    /**
     * Sends a message to its intended destination.
     * <p>
     * This implementation will be supplied the message routing information via
     * the input parameter, <i>srvc</i>.
     * 
     * @param srvc
     *            the routing information pertaining to the SOAP message
     * @param message
     *            an arbitrary object as the message
     * @return an instance of {@link MessageHandlerResults}
     * @throws MessageRoutingException
     *             general message routing errors.
     */
    MessageHandlerResults routeMessage(MessageRoutingInfo srvc, Object message)
            throws MessageRoutingException;
}
