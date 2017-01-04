package com.api.messaging;

import com.InvalidDataException;
import com.NotFoundException;

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

    // /**
    // * Setup the service registry.
    // *
    // * @throws MessageRoutingException
    // */
    // void intitalizeRegistry() throws MessageRoutingException;

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
     * @return an arbitrary object as the results of the message being
     *         processed.
     * @throws MessageRoutingException
     *             general message routing errors.
     */
    Object routeMessage(MessageRoutingInfo srvc, Object message)
            throws MessageRoutingException;

    /**
     * Sends a message to its intended destination.
     * <p>
     * This implementation must obtain the message routing inforamtion using the
     * message id input parameter.
     * 
     * @param messageId
     *            The id of the message to send.
     * @param message
     *            An arbitrary object representing the actual message payload
     * @return an arbitrary object as the results of the message being
     *         processed.
     * @throws MessageRoutingException
     */
    Object routeMessage(String messageId, Object message)
            throws MessageRoutingException;

    /**
     * Obtain routing information for a specified message.
     * <p>
     * The routing information contains the networking details about how to
     * contact the appropriate web service handler designated to process the
     * message.
     * 
     * @param messageId
     *            the message id to use to obtain message routing information.
     * @return an instance of {@link com.api.messaging.MessageRoutingInfo
     *         MessageRoutingInfo}
     * @throws MessageRoutingException
     *             Routing information is unobtainable due to the occurrence of
     *             data access errors or etc.
     * @throws InvalidDataException
     *             Routing information is unobtainable due to invalid data.
     * @throws NotFoundException
     *             Routing information is not found by the supplied key,
     *             <i>messageId</i>
     */
    MessageRoutingInfo getRoutingInfo(String messageId)
            throws InvalidDataException, NotFoundException,
            MessageRoutingException;

    // /**
    // * Create the input data for a specific message handler using the incoming
    // generic message.
    // *
    // * @param inMessage
    // * an arbitrary object representing the incoming message data.
    // * @return
    // * an instance of {@link MessageHandlerInput}
    // * @throws MessageRoutingException
    // * Routing information is unobtainable due to the occurrence of data
    // access
    // * errors or etc.
    // */
    // MessageHandlerInput createReceptorInputData(Object inMessage) throws
    // MessageRoutingException;
    //
    // /**
    // * Builds the response message from the service handler's results.
    // *
    // * @param results
    // * An instance of {@link MessageHandlerResults} containing the results of
    // the service
    // * handler processing.
    // * @return
    // * An arbitrary object befitting for the messaging router's
    // implementation.
    // * @throws InvalidDataException
    // * @throws MessageRoutingException
    // */
    // Object getReceptorResults(MessageHandlerResults results) throws
    // InvalidDataException, MessageRoutingException;

    // /**
    // * Retrieves the total number of services contained in the service
    // registry.
    // */
    // Integer getServiceCount();

    // /**
    // * Sets the user's request object.
    // *
    // * @param request
    // * an instance of {@link HttpServletRequest}
    // */
    // void setUserRequest(HttpServletRequest request);
}
