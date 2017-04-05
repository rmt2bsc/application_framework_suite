package com.api.messaging.webservice.soap.client;

import java.util.List;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.MessageRoutingException;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.xml.jaxb.JaxbUtil;

/**
 * The web service client that acts as a wrapper for invoking various kinds of
 * web services designed in a RMT2 environment.<br>
 * <br>
 * 
 * <b><u>How to Use the SoapClientWrapper to call SOAP based web
 * services</u></b>
 * 
 * <pre>
 *    ObjectFactory f = new ObjectFactory();<br>
 *    // Create Payload instance from JAXB generated java class<br>
 *    RQContentSearch ws = f.createRQContentSearch();<br><br>
 *    // Setup the header section of the SOAP body payload<br>
 *    HeaderType header = JaxbPayloadFactory.createHeader("RQ_content_search", "SYNC", "REQUEST", "rterrell");<br>
 *    ws.setHeader(header);<br><br>
 *    
 *    // Setup individual data items of the SOAP body payload<br>
 *    ws.setContentId(BigInteger.valueOf(283));<br><br>
 *    // Marshal the payload to a XML String<br>
 *    String msg = MessagingResourceFactory.getJaxbMessageBinder().marshalMessage(ws);<br><br>
 *    // Create the SoapClientWrapper instance<br>
 *    SoapClientWrapper client = new SoapClientWrapper();<br>
 *    try {<br>
 *        // Call the intended web service<br>
 *        SOAPMessage resp = client.callSoap(msg);<br><br>
 *        // Check if response contains a SOAPFault<br>
 *        if (client.isSoapError(resp)) {<br>
 *           String errMsg = client.getSoapErrorMessage(resp);<br>
 *           throw new Exception(errMsg);<br>
 *        }<br><br>
 *        // Obtain the payload from the response as a JAXB instance.<br>
 *        RSContentSearch content = (RSContentSearch) client.getSoapResponsePayload();<br>
 *        // From this point, manage the JAXB instance as needed.<br>
 *    }<br>
 *    catch (Exception e) {<br>
 *         e.printStackTrace();<br>
 *    }<br>
 * 
 * @author RTerrell
 */
public class SoapClientWrapper extends RMT2Base {
    private static Logger logger = Logger.getLogger("SoapClientWrapper");

    private ProviderConfig config;

    private SOAPMessage soapResponse;

    private SoapMessageHelper helper;

    /**
     * Creates an empty SoapClientWrapper instance.
     */
    public SoapClientWrapper() {
        this.config = SoapClientFactory.getSoapConfigInstance();
        this.helper = new SoapMessageHelper();
        logger.info("Web service invocation client logger is created");
    }

    /**
     * Releases all resources allocated to used the underlying messaging
     * provider.
     * 
     * @throws SystemException
     */
    public void close() throws SystemException {
        return;
    }

    /**
     * Calls a SOAP web service using XML that represents the SOAP message's
     * payload.
     * <p>
     * This accepts only the XML that is to serve as the SOAP body of the
     * message. The framework builds a standard SOAP envelope and inserts
     * <i>payload</i> as the SOAP body.
     * 
     * @param payload
     *            the raw XML in String that will serve as the SOAP body.
     * @return the response of the web service call or null if the call was
     *         performed asynchronously.
     * @throws MessageException
     */
    public SOAPMessage callSoap(String payload) throws MessageException {
        SoapClient api = SoapClientFactory.getClient();
        String soapXml = api.createRequest(payload);
        try {
            api.connect(this.config);
        } catch (ProviderConnectionException e) {
            throw new MessageException(e);
        }

        this.soapResponse = (SOAPMessage) api.sendMessage(soapXml);
        this.logSoapResponse();
        return this.soapResponse;
    }

    /**
     * Calls a SOAP web service using XML that represents the SOAP message's
     * payload and associates attachments with the SOAP message.
     * <p>
     * This accepts only the XML that is to serve as the SOAP body of the
     * message. The framework builds a standard SOAP envelope and inserts
     * <i>payload</i> as the SOAP body.
     * 
     * @param payload
     *            the raw XML in String. The format of the XML document should
     *            follow the definition of the schema,
     *            RMT2_Message_Payload_Request.xsd.
     * @param attachments
     *            List of generic objects that will be used as SOAP attachments.
     *            When null, the SOAP message invocation is done without trying
     *            to included the attachments object.
     * @return the response of the web service call or null if the call was
     *         performed asynchronously.
     * @throws MessageException
     */
    public SOAPMessage callSoap(String payload, List<Object> attachments)
            throws MessageException {
        SoapClient api = SoapClientFactory.getClient();
        String soapXml = api.createRequest(payload);
        try {
            api.connect(this.config);
            if (attachments != null && attachments.size() > 0) {
                this.soapResponse = (SOAPMessage) api.sendMessage(soapXml,
                        attachments);
            }
            else {
                this.soapResponse = (SOAPMessage) api.sendMessage(soapXml);
            }
            this.logSoapResponse();
            return soapResponse;
        } catch (ProviderConnectionException e) {
            throw new MessageException(e);
        }
    }

    /**
     * Extracts the SOAP body from the SOAP message and applies JAXB binding to
     * unmarshal it to a Java instance.
     * 
     * @return Object a JAXB instance. Client is repsonsible for applying the
     *         proper cast.
     * @throws MessageException
     */
    public Object getSoapResponsePayload() throws MessageException {
        String payload = this.getSoapResponsePayloadString();
        Object jaxbPayload = null;
        try {
            JaxbUtil jaxbUtil = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
            jaxbPayload = jaxbUtil.unMarshalMessage(payload);
        } catch (Exception e) {
            this.msg = "Error occurred trying to unmarshall the payload of the response SOAP envelope as a JAXB object";
            throw new MessageRoutingException(this.msg, e);
        }
        return jaxbPayload;
    }

    /**
     * Extracts the SOAP body from the SOAP message and returns the body to the
     * caller as a String.
     * 
     * @return
     * @throws MessageException
     */
    public String getSoapResponsePayloadString() throws MessageException {
        if (this.soapResponse == null) {
            return null;
        }
        SoapMessageHelper helper = new SoapMessageHelper();
        String payload = helper.getBody(this.soapResponse);
        return payload;
    }

    /**
     * Obtains the String representation of the SOAP message instance and sends
     * the output to the log file.
     * 
     * @return String Returns SOAP message in String form. Returns null if the
     *         internal SOAP instance is invalid or null.
     * @throws MessageException
     */
    protected String logSoapResponse() throws MessageException {
        if (this.soapResponse == null) {
            return null;
        }
        SoapMessageHelper helper = new SoapMessageHelper();
        String xml = helper.getSoap(this.soapResponse);
        logger.info(xml);
        return xml;
    }


    /**
     * Tests whether input SOAP message instance's body contains a SOAPFault.
     * 
     * @param soap
     * @return boolean true when SOAP body contains a fault or false <i>soap</i>
     *         is null or the SOAP body does not contain a fault.
     */
    public boolean isSoapError(SOAPMessage soap) {
        if (soap == null) {
            return false;
        }
        return this.helper.isError(soap);

    }

    /**
     * Returns the SOAPFault message contained in <i>soap</i>.
     * 
     * @param soap
     * @return String the SOAPFault message or null when <i>soap</i> is invalid
     *         or SOAPFault does not exist.
     */
    public String getSoapErrorMessage(SOAPMessage soap) {
        if (soap == null) {
            return null;
        }
        if (this.helper.isError(soap)) {
            return this.helper.getErrorMessage(soap);
        }
        return null;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(ProviderConfig config) {
        this.config = config;
    }

}
