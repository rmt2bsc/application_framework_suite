package com.api.messaging.webservice.soap.client;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.RMT2RuntimeException;
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageException;
import com.api.xml.jaxb.JaxbUtil;

/**
 * A common SOAP client that provides functionality to dynamically handle JAXB
 * related requests and responses.
 * 
 * @author RTerrell
 */
public class SoapJaxbClientWrapper extends RMT2Base {
    private static Logger logger = Logger.getLogger(SoapJaxbClientWrapper.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * A common SOAP method that builds and invokes SOAP messages in which the
     * payload content of the SOAP request and response is a JAXB object.
     * 
     * @param jaxbPayload
     *            The SOAP request's payload which identifies the request to be
     *            executed and is required to be a JAXB object.
     * @return T The SOAP response's payload which identifies the response
     *         message and is required to be JAXB object.
     * @throws RMT2RuntimeException
     *             when <i>jaxbPayload</i> is null or a SOAP invocation error of
     *             any kind.
     */
    public static <T, R> T callSoapRequest(R jaxbPayload) throws RMT2RuntimeException {
        if (jaxbPayload == null) {
            throw new RMT2RuntimeException("The request parameter, which is required to be a JAXB type, cannot be null");
        }

        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(jaxbPayload);

        // Prepare to make SOAP request.
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage resp = client.callSoap(payload);
            if (client.isSoapError(resp)) {
                String errMsg = client.getSoapErrorMessage(resp);
                logger.error(errMsg);
                throw new RMT2RuntimeException(errMsg);
            }
            String result = client.getSoapResponsePayloadString();
            logger.debug(result);

            T response = (T) jaxb.unMarshalMessage(result);
            return response;
        } catch (MessageException e) {
            throw new RMT2RuntimeException(SoapJaxbClientWrapper.MSG, e);
        }
    }

}
