package com.api.messaging;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.persistence.db.DatabaseConnectionBean;
import com.controller.Request;

/**
 * Common message handler class for the purpose of accepting the database connection 
 * bean and the user's request instance, and validating incoming data parameters such 
 * as the <i>user id</i> and <i>request id</i>.
 * <p>
 * The descendent implementation of this class will be responsible for providing 
 * specific functionality for processing the request.
 * 
 * @author rterrell
 *
 */
public class CommonMessageHandlerImpl extends RMT2Base implements MessageHandler {
    
    private static final Logger logger = Logger.getLogger("CommonMessageHandlerImpl");
    
    private String userId;
    
    private String reqMsgId;
    
    private Properties parms;
    
    private String respMsgId;
    
    protected DatabaseConnectionBean con;
    
    protected Request request;

    /**
     * Default Constructor 
     */
    public CommonMessageHandlerImpl() {
	return;
    }

    public CommonMessageHandlerImpl(DatabaseConnectionBean con, Request request) {
	this();
	this.con = con;
	this.request = request;
	return;
    }
    
    
    /**
     * Validates the <i>parms</i> Properties instance and verifies that it contains 
     * key/value pairs for "userId" and "requestMsgId".
     * 
     * @param parms
     *           Properties instance containing the request parameters
     * @return
     *           Always return null upon successful completion.
     * @throws MessageException
     *           When <i>parms</i> is either null or does not contain key-value pairs for "userId" 
     *           and "requestMsgId".
     */
    public Object execute(Properties parms) throws MessageHandlerException {
	if (parms == null) {
	    this.msg = "Message handler requires Properties instance containing request parameters";
	    logger.error(this.msg);
	    throw new MessageHandlerException(this.msg);
	}
	this.userId = parms.getProperty(MessageHandler.MSG_USERID_TAG);
	this.reqMsgId = parms.getProperty(MessageHandler.MSG_REQUESTID_TAG);
	
	if (this.userId == null) {
	    this.msg = "Message handler requires input parameter, " + MessageHandler.MSG_USERID_TAG + ", which represents the user id of the requester";
	    logger.error(this.msg);
	    throw new MessageHandlerException(this.msg);
	}
	if (this.reqMsgId == null) {
	    this.msg = "Message handler requires input parameter, " + MessageHandler.MSG_REQUESTID_TAG + ", which represents the message id or service id of the request that is to be processed";
	    logger.error(this.msg);
	    throw new MessageHandlerException(this.msg);
	}
	this.parms = parms;
	return null;
    }
    
    /* (non-Javadoc)
     * @see com.api.messaging.MessageHandler#getResponseServiceId()
     */
    public String getResponseServiceId() {
	return this.respMsgId;
    }
    
    /* (non-Javadoc)
     * @see com.api.messaging.MessageHandler#setResponseServiceId(java.lang.String)
     */
    public void setResponseServiceId(String id) {
	this.respMsgId = id;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the reqMsgId
     */
    public String getReqMsgId() {
        return reqMsgId;
    }

    /**
     * @return the respMsgId
     */
    public String getRespMsgId() {
        return respMsgId;
    }

    /**
     * @return the parms
     */
    public Properties getParms() {
        return parms;
    }

   

    

   

}
