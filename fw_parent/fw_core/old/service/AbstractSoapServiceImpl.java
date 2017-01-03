package com.api.messaging.webservice.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.NotFoundException;
import com.SystemException;

import com.api.Product;
import com.api.ProductDirector;

import com.api.jsp.action.AbstractActionHandler;
import com.api.jsp.action.ActionHandlerException;
import com.api.messaging.MessageBinder;
import com.api.messaging.MessageHandler;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.api.messaging.webservice.soap.SoapResponseException;
import com.api.messaging.webservice.soap.SoapServiceException;
import com.api.messaging.webservice.soap.client.SoapProductBuilder;
import com.api.messaging.webservice.soap.client.SoapClientFactory;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.DatabaseException;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.security.authentication.web.RMT2SessionBean;


import com.constants.RMT2ServletConst;
import com.controller.Request;
import com.controller.Response;

import com.util.RMT2BeanUtility;
import com.util.RMT2XmlUtility;

/**
 * This class provides a template implementation of the SoapProcessor interface to minimize the work 
 * required to implement this interface for any particular SOAP based web service.  Minimally, the 
 * programmer only needs to extend this class and provide an implementation for the 
 * doExecuteRequest(Properties) method.
 *  
 * @author appdev
 * @deprecated No longer in use
 *
 */
public abstract class AbstractSoapServiceImpl extends AbstractActionHandler implements SoapService {

    private static final Logger logger = Logger.getLogger(AbstractSoapServiceImpl.class);

    protected String requestMsgId;

    protected String responseMsgId;

    protected Map<String, String> reqHeaders;

    protected SoapMessageHelper helper;

    protected DatabaseConnectionBean con;

    private List<DataHandler> inAttachments;

    protected List<Object> outAttachments;
    
    protected String currentUser;
    
    protected SOAPMessage inMsg;

    protected SOAPMessage outMsg;
    
    private Properties parms;

    /**
     * Default constructor which creates an empty AbstractSoapServiceProcessor object.
     */
    public AbstractSoapServiceImpl() {
        super();
        AbstractSoapServiceImpl.logger.log(Level.INFO, "An empty instance of AbstractSoapServiceProcessor has been created");
        this.helper = new SoapMessageHelper();
    }


    /**
     * Closes the XML DAO instance.
     * 
     * @throws ActionHandlerException
     */
    public void close() throws ActionHandlerException {
        return;
    }

    /**
     * This method is not supported for SOAP oreiented web services which 
     * no implementation is provided.
     *
     * @param request   
     *          N/A
     * @param response  
     *          N/A
     * @param command  
     *          N/A
     * @throws UnsupportedOperationException 
     *          when method is invoked
     */
    public void processRequest(Request request, Response response, String command) throws ActionHandlerException {
	super.processRequest(request, response, command);
        RMT2SessionBean userSession = (RMT2SessionBean) this.request.getSession().getAttribute(RMT2ServletConst.SESSION_BEAN);
        this.currentUser = (userSession == null ? "" : userSession.getLoginId());
        
        
        this.receiveClientData();
        this.outMsg = this.processMessage(this.inMsg);
        logger.info("Response Message: " + this.outMsg);
        this.sendClientData();
    }

    /**
     * Retreives the SOAP message from the user's request instance.
     * 
     * @throws ActionHandlerException
     *           when the SOAP message is unable to be obtained from the user's request instance 
     *           or the SOAP message is of an invalid structure.
     */
    protected void receiveClientData() throws ActionHandlerException {
        try {
            this.inMsg = this.helper.getSoapInstance(this.request);
            // Test if SOAP message is of valid structure
            this.inMsg.getSOAPBody();
        }
        catch (Exception e) {
            throw new ActionHandlerException(e);
        }
    }
    
    /**
     * Sends the response of the target web service back to the calling servlet via the 
     * {@link com.controller.Response Response} instance.
     */
    protected void sendClientData() throws ActionHandlerException {
        try {
            this.helper.sendSoapInstance(this.response, this.outMsg);
        }
        catch (SoapResponseException e) {
            throw new ActionHandlerException(e);
        }
    }
    /**
     * Interprets the body content of the SOAP envelope and performs the actual execution 
     * of the client request.  Operates on the body contents of the SOAP envelope by 
     * creating a Properties mapping for each element of the SOAP body, invokes the main 
     * process that is repsonsible for carrying out the intent of the request, and packages 
     * the response into a new SOAP envelope.  If a problem occurs during the processing of 
     * the message, the service will create a SOAP fault and send it back to the client.
     * <p> 
     * This method utilizes the template pattern to execute client's request invoking the 
     * following instance methods in the order stated: 
     * <ol>
     *   <li>{@link com.api.messaging.webservice.soap.service.AbstractSoapServiceProcessor.soap.AbstractSoapServiceImpl#doGetRequestData(SOAPMessage) doGetRequestData(SOAPMessage)}</li>
     *   <li>{@link com.api.messaging.webservice.soap.service.AbstractSoapServiceProcessor.soap.AbstractSoapServiceImpl#doExecuteRequest(Properties) doExecuteRequest(Properties)}</li>
     *   <li>{@link com.api.messaging.webservice.soap.service.AbstractSoapServiceProcessor.soap.AbstractSoapServiceImpl#doPackageResponse(SOAPMessage, String) doPackageResponse(SOAPMessage, String)}</li>
     * </ol>
     * These methods contain base logic with the exception of <i>doExecuteRequest</i>, 
     * which is abstract, and can be extended at the descendent.
     * 
     * @param soap
     *          an instance of SOAPMessage
     * @return String
     *          the response XML
     */
    public SOAPMessage processMessage(SOAPMessage soap) {
	SOAPMessage soapResp = null;
        try {
            Properties parms = this.doGetRequestData(soap);
            Object xml = this.invokeSoapHandler(parms);
            soapResp = this.doPackageResponse(soap, xml.toString());
        }
        catch (Exception e) {
            this.msg = "SOAP Web Service call failed for service id, " + (this.requestMsgId == null ? "[Unknown SOAP Web Service Id]" : this.requestMsgId);
            this.msg += "  Error Message: " + e.getMessage();
            logger.error(this.msg);
            soapResp = helper.createSoapFault("Server", e.getMessage(), null, null);
        }
        return soapResp;
    }

    /**
     * Extracts the XML details from the SOAP envelope's body and creates an instance 
     * of {@link com.api.xml.XmlDao XmlDao}.  The Dao is put in place as a helper so 
     * that the implementer can use it to obtain the required data items from the Body 
     * part of the SOAP message in order to process the client's request.
     * 
     * @param soap
     * @return
     * @throws SoapServiceException
     */
    protected Properties doGetRequestData(SOAPMessage soap) throws SoapServiceException {
        Properties parms = new Properties();
        SoapMessageHelper helper = new SoapMessageHelper();

        // Get outAttachments, if applicable
        if (soap.countAttachments() > 0) {
            this.loadAttachments(soap);
        }
        // get payload, serviceId and queueName and populate into properties collection.
        String xmlPayload = helper.getBody(soap);
        MessageBinder jaxbCtx = MessagingResourceFactory.getJaxbMessageBinder();
        Object req = jaxbCtx.unMarshalMessage(xmlPayload);
        
        // New Code to create properties instance
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(req);
        parms = beanUtil.toProperties();
        
        parms.put(SoapProductBuilder.PAYLOADINSTANCE, req);

        // Capture actual user id.   This logic is needed to comply with the fact that some 
        // areas of the framework requires a user id, which is indicative of the user 
        // possessing a session.  One case is constructing the user timestamp from RMT2Date 
        // utility class.
        //
        //  New Business Requirement:  User Id is not required at this point of authentication 
        //  since we are now authenticating using the session id as an alternative for when the 
        //  user id is not available.
        try {
            String userId = RMT2XmlUtility.getElementValue("user_id", "//header", xmlPayload);
            this.request.setAttribute(AuthenticationConst.AUTH_PROP_USERID, userId);
            parms.setProperty(MessageHandler.MSG_USERID_TAG, userId);
        }
        catch (Exception e) {
            // DO nothing...we will try to authenticate by user's session id since the user id is not available.
        }

        String serviceId = null;
        try {
            serviceId = helper.getHeaderValue(SoapProductBuilder.HEADER_NS + ":" + SoapProductBuilder.SERVICEID_NAME, soap);
            parms.setProperty(MessageHandler.MSG_REQUESTID_TAG, serviceId);
        }
        catch (SOAPException e) {
            this.msg = "A problem occurred obtaining Service Id header element";
            AbstractSoapServiceImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapServiceException(this.msg, -3003);
        }
        catch (NotFoundException e) {
            this.msg = "The value of Service Id is required and was not found as part of the SOAP header element";
            AbstractSoapServiceImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapServiceException(this.msg, -3004);
        }
        String queueName = null;
        try {
            queueName = helper.getHeaderValue(SoapProductBuilder.HEADER_NS + ":" + SoapProductBuilder.QUEUE_NAME, soap);
            parms.setProperty(SoapProductBuilder.QUEUE_NAME, queueName);
        }
        catch (SOAPException e) {
            this.msg = "A problem occurred obtaining Queue Name header element";
            AbstractSoapServiceImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapServiceException(this.msg, -3003);
        }
        catch (NotFoundException e) {
            // Queue Name is not required.
        }
        this.requestMsgId = serviceId;
        this.parms = parms;
        return parms;
    }

    /**
     * Creates a List of DataHandlers using the outAttachments found in <i>soap</i>.
     * 
     * @param soap
     *         an iinstance of SOAPMessage.
     * @return int
     *     the total number of inAttachments processed.
     */
    private int loadAttachments(SOAPMessage soap) {
        this.inAttachments = new ArrayList<DataHandler>();
        int count = 0;
        Iterator<AttachmentPart> iter = soap.getAttachments();
        while (iter.hasNext()) {
            AttachmentPart data = iter.next();
            try {
                DataHandler dataHandler = data.getDataHandler();
                count++;
                this.inAttachments.add(dataHandler);
            }
            catch (SOAPException e) {
                throw new SystemException(e);
            }
        }
        return count;
    }

    /**
     * Implementation should include logic that will fulfil the client's request based 
     * on the parmaters gathered in <i>reqParms</i>.
     * 
     * @param reqParms
     * @return
     * @throws SoapServiceException
     */
    abstract protected Object invokeSoapHandler(Properties reqParms) throws SoapServiceException;

    /**
     * Creates a new SOAP message including <i>soapResponse</i> as the data for the SOAP Body part. 
     * 
     * @param origMsg
     *          the SOAP message that is assoicated with the inital request.
     * @param payload
     *          the results of executing the service request.
     * @return SOAPMessage
     *          The response SOAP message
     * @throws SoapServiceException
     */
    protected SOAPMessage doPackageResponse(SOAPMessage origMsg, String payload) throws SoapServiceException {
	// If response service id does not exist in the Propeties instance, then set as "N/A" so that
	// the SOAP response requires a service id.
	if (this.responseMsgId == null) {
	    this.responseMsgId = "N/A";
	}
        // Create new SOAP message by adding the SOAP response as part of the SOAP body.
        SOAPMessage sm = null;
        try {
            SoapProductBuilder builder = SoapClientFactory.getSoapBuilderInstance(this.responseMsgId, null, payload);
            Product xmlProd = ProductDirector.construct(builder);
            builder.createAttachments(this.outAttachments);
            sm = (SOAPMessage) xmlProd.toObject();
            return sm;
        }
        catch (Exception e) {
            throw new SoapServiceException(e);
        }
    }
    
    /* (non-Javadoc)
     * @see com.action.ICommonAction#add()
     */
    public void add() throws ActionHandlerException {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see com.action.ICommonAction#edit()
     */
    public void edit() throws ActionHandlerException {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see com.action.ICommonAction#save()
     */
    public void save() throws ActionHandlerException, DatabaseException {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see com.action.ICommonAction#delete()
     */
    public void delete() throws ActionHandlerException, DatabaseException {
	// TODO Auto-generated method stub
	
    }
}
