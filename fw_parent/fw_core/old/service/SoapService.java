package com.api.messaging.webservice.soap.service;

import javax.xml.soap.SOAPMessage;

import com.api.jsp.action.ICommand;



/**
 * @author appdev
 * @deprecated No longer in use
 *
 */
public interface SoapService extends ICommand {

    /**
     * Disassemble the SOAP message that is passed to it by the servlet and process its contents. 
     * Processing the message involves accessing the parts of a SOAP message. If there are 
     * problems in the processing of the message, the service needs to create a SOAP fault 
     * object and send it back to the client.
     * 
     * @param soap
     *          an instance of SOAPMessage
     * @return SOAPMessage
     *          The response SOAP instance.  The implementation should always return a response 
     *          which will be either a valid SOAPMessage instance or a SOAPFault instance.
     */
    SOAPMessage processMessage(SOAPMessage soap);

}
