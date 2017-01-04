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
     * @param serviceId
     *            The message id that will be assigned to the SOAP header.
     * @param targetAction
     *            The target action that will be assinged to the SOAP header.
     *            This parameter is optional and can contain null.
     * @param soapBody
     *            The XML String representing the payload of the SOAP Message.
     * @return An XML String of the entire SOAP message request.
     * @throws SoapRequestException
     */
    String createRequest(String serviceId, String targetAction, String soapBody)
            throws SoapRequestException;

    /**
     * Creates a SOAP Response Message as a XML document.
     * 
     * @param serviceId
     *            the name of the requested service expecting the response
     * @param soapBody
     *            a XML String representing the actual response data. This XML
     *            document will serve as the SOAP body.
     * @return String SOAP message as the response.
     * @throws SoapResponseException
     */
    String createResponse(String serviceId, String soapBody)
            throws SoapResponseException;

    /**
     * 
     * @param msg
     * @param attachments
     * @return
     * @throws MessageException
     */
    SOAPMessage sendMessage(Serializable msg, List<Object> attachments)
            throws MessageException;

}
