package com.api.messaging.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.InvalidRequestException;
import com.api.messaging.webservice.WebServiceConstants;
import com.api.util.RMT2File;
import com.api.xml.RMT2XmlUtility;
import com.api.xml.jaxb.JaxbUtil;
import com.api.xml.jaxb.JaxbUtilException;

/**
 * Abstract class containing common logic for receiving, unmarshaling,
 * validating message payloads.
 * <p>
 * This class uses JAXB to manage incoming message payloads
 * 
 * @author roy.terrell
 *
 * @param <T1>
 *            The request type to process
 * @param <T2>
 *            The response type to process
 * @param <P>
 *            The Payload type to process for responses and/or request update
 *            data
 */
public abstract class AbstractJaxbMessageHandler<T1, T2, P> extends RMT2Base implements MessageHandlerCommand<T1, T2, P> {

    private static final Logger logger = Logger.getLogger(AbstractJaxbMessageHandler.class);

    public static final String ERROR_MSG_TRANS_NOT_FOUND = "Unable to identify transaction code: ";
    
    public static final String ERROR_MSG_INVALID_TRANS = "An invalid request message was encountered.  Please check payload.";

    protected Serializable payload;

    protected JaxbUtil jaxb;

    protected T1 requestObj;
    
    protected T2 responseObj;
    
    protected String command;

    protected String sessionId;

    /**
     * Creates a AbstractMessageHandler 
     */
    public AbstractJaxbMessageHandler() {
        this.jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        return;
    }

    /**
     * Unmarshals the payload and verifies that the payload is valid.
     * 
     * @param command
     *            The command that best describes <i>payload</i> represents
     * @param payload
     *            The message that is to be processed. Typically this will be a
     *            String of XML or JSON
     * @return MessageHandlerResults
     * @throws MessageHandlerCommandException
     *             <i>payload</i> is deemed invalid.
     */
    @Override
    public MessageHandlerResults processMessage(String command, Serializable payload) throws MessageHandlerCommandException {
        this.command = command;
        this.payload = payload;
        if (this.payload instanceof Serializable) {
            logger.info("Payload is identified as a valid Serializable");
        }
        
        MessageHandlerResults results = null;
        String reqXml = this.getPayloadAsString();
        try {
            // Create user's work area in which the session id or security token
            // is required.
            this.sessionId = RMT2XmlUtility.getElementValue(WebServiceConstants.SESSIONID, reqXml);
            String userWorkArea = System.getProperty("SerialPath");
            RMT2File.createDirectory(userWorkArea + File.separatorChar + this.sessionId);
        } catch (Exception e) {
            logger.warn(WebServiceConstants.ERROR_MSG_SESSIONID_MISSING);
            // throw new
            // MessageHandlerException(WebServiceConstants.ERROR_MSG_SESSIONID_REQUIRED);
        }

        try {
            // Unmarshall XML String
            this.requestObj = (T1) this.jaxb.unMarshalMessage(reqXml);
            if (logger.isDebugEnabled()) {
                try {
                    String printXml = RMT2XmlUtility.prettyPrint(reqXml);
                    logger.debug(printXml);
                } catch (TransformerException e1) {
                    logger.debug(reqXml, e1);
                }
            }
            
            this.validateRequest(this.requestObj);
        } catch (Exception e) {
            logger.error("Core Message Handler Error", e);
            MessageHandlerCommonReplyStatus rs = new MessageHandlerCommonReplyStatus();
            rs.setReturnCode(WebServiceConstants.RETURN_CODE_FAILURE);
            rs.setReturnStatus(String.valueOf(HttpServletResponse.SC_BAD_REQUEST));
            
            if (e instanceof JaxbUtilException) {
                rs.setMessage(ERROR_MSG_INVALID_TRANS);
                rs.setExtMessage(e.getMessage());
            } else {
                rs.setMessage(e.getMessage());
            }
            
            String respXml = this.buildResponse(null,rs);
            results = new MessageHandlerResults();
            results.setPayload(respXml);

            if (logger.isDebugEnabled()) {
                try {
                    String printXml = RMT2XmlUtility.prettyPrint(respXml);
                    logger.debug(printXml);
                } catch (TransformerException e1) {
                    logger.debug(reqXml, e1);
                }
            }
        }
        
        return results;
    }
    
    /**
     * Validates the payload in its unmarshalled form
     * 
     * @param req
     *            instance of {@link T1}
     */
    protected abstract void validateRequest(T1 req) throws InvalidRequestException;
    
    /**
     * Uses a generic payload type to build the payload response and/or the
     * payload representing data that is to be updated in a request.
     * 
     * @param payload
     *            an instance of {@link P}
     * @param replyStatus
     *            an instance of {@value MessageHandlerCommonReplyStatus}
     * @return the response payload
     */
    protected abstract String buildResponse(P payload, MessageHandlerCommonReplyStatus replyStatus);

    /**
     * Return payload as a String.
     * 
     * @return
     */
    public String getPayloadAsString() {
        return this.payload.toString();
    }

    protected OutputStream buildPdfReport() {
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        return pdfStream;
    }

    /**
     * Return the file name of the file layout or XSL transformation script used
     * to generate a report
     * 
     * @return
     */
    public String getReportName() {
        return null;
    }

    /**
     * Creates an error reply as XML String
     * 
     * @param errorCode
     * @param msg
     * @return
     */
    protected MessageHandlerResults createErrorReply(int errorCode, String statusCode, String msg) {
        MessageHandlerResults results = new MessageHandlerResults();
        MessageHandlerCommonReplyStatus rs = new MessageHandlerCommonReplyStatus();
        rs.setReturnCode(errorCode);
        rs.setReturnStatus(statusCode);
        rs.setMessage(msg);
        String xml = this.buildResponse(null, rs);
        results.setPayload(xml);
        results.setReturnCode(errorCode);
        results.setErrorMsg(msg);
        return results;
    }
}
