package com.api.messaging.webservice.soap.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.messaging.AbstractMessagingImpl;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapRequestException;
import com.api.messaging.webservice.soap.SoapResponseException;

/**
 * Abastract class for creating SOAP requests and responses.
 * 
 * @author appdev
 * 
 */
public abstract class AbstractSoapClientImpl extends AbstractMessagingImpl {

    private static Logger logger = Logger
            .getLogger(AbstractSoapClientImpl.class);

    protected SoapMessageHelper soapHelper;

    /**
     * Creates a default AbstractSoapClientImpl
     */
    public AbstractSoapClientImpl() {
        super();
        this.soapHelper = new SoapMessageHelper();
        logger.log(Level.DEBUG, "AbstractSoapClientImpl initialized");
    }

    /**
     * Creates a SOAP message for the client request.
     * 
     * @param soapBody
     *            The XML String representing the payload of the SOAP Message.
     * @return An XML String of the entire SOAP message request.
     * @throws SoapRequestException
     */
    public String createRequest(String soapBody) throws SoapRequestException {
        return this.soapHelper.createRequest(soapBody);
    }

    /**
     * Creates a SOAP message for the client response.
     * 
     * @param serviceId
     *            The message id that was processed.
     * @param soapBody
     *            The XML String representing the payload of the response SOAP
     *            message
     * @return An XML String of the entire SOAP message response.
     * @throws SoapResponseException
     */
    public String createResponse(String soapBody) throws SoapResponseException {
        return this.soapHelper.createResponse(soapBody);
    }
}
