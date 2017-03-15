package com.api.messaging.webservice.soap.client;

import java.io.Serializable;
import java.util.List;

import javax.xml.soap.SOAPMessage;

import com.api.messaging.MessageException;
import com.api.messaging.MessageManager;
import com.api.messaging.webservice.soap.SoapRequestException;
import com.api.messaging.webservice.soap.SoapResponseException;

/**
 * A client interface for sending and receiving SOAP messages.
 * 
 * @author Roy Terrell
 * 
 */
public interface SoapClient extends MessageManager {

    /**
     * Creates a SOAP message for the client request.
     * 
     * @param payload
     *            The XML String representing the payload of the SOAP Message.
     * @return An XML String of the entire SOAP message request.
     * @throws SoapRequestException
     */
    String createRequest(String payload) throws SoapRequestException;

    /**
     * Creates a SOAP Response Message as a XML document.
     * 
     * @param payload
     *            a XML String representing the actual response data. This XML
     *            document will serve as the SOAP body.
     * @return String SOAP message as the response.
     * @throws SoapResponseException
     */
    String createResponse(String payload)
            throws SoapResponseException;

    /**
     * Sends SOAP messages to designated endpoint.
     * 
     * @param soapXml
     *            XML as String
     * @param attachments
     *            a List of Objects serving as attachments
     * @return {@link SOAPMessage}
     * @throws MessageException
     */
    SOAPMessage sendMessage(Serializable soapXml, List<Object> attachments)
            throws MessageException;
}
