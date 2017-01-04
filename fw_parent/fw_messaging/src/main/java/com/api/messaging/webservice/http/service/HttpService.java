package com.api.messaging.webservice.http.service;

/**
 * Interface provides the method contract needed to implement a web service
 * using the HTTP URL Protocol.
 * 
 * @author appdev
 * 
 */
public interface HttpService {
    static final String SERVICE_RESULTS = "serviceresults";

    /**
     * The purpose of this method is to retrieve data from some arbitrary input
     * source and package the data into a form that is useable as input data for
     * the service implementation.
     * 
     * @throws WsHttpActionHandlerException
     */
    void receiveClientData() throws WsHttpActionHandlerException;

    /**
     * The purpose of this method is to send the results to the caller.
     * 
     * @throws WsHttpActionHandlerException
     */
    public void sendClientData() throws WsHttpActionHandlerException;

    /**
     * The purpose of this method is to use the input data, if applicable, to
     * process the request of a given service.
     * 
     * @throws WsHttpActionHandlerException
     */
    void processData() throws WsHttpActionHandlerException;
}
