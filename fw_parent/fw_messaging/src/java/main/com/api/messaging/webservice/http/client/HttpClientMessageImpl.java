package com.api.messaging.webservice.http.client;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Exception;
import com.SystemException;
import com.api.DaoApi;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.AbstractMessagingImpl;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.http.HttpClient;
import com.api.messaging.webservice.http.HttpException;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.api.xml.RMT2XmlUtility;
import com.constants.GeneralConst;
import com.util.RMT2File;

/**
 * @author Roy Terrell
 * 
 */
class HttpClientMessageImpl extends AbstractMessagingImpl implements
        HttpMessageSender {

    private static Logger logger = Logger.getLogger("HttpClientMessageImpl");

    private HttpClient client;

    private Object replyMsg;

    private DaoApi xmlApi;

    private Properties prop;

    /** The user's request object */
    private Request request;

    /** The user's response object */
    private Response response;

    private boolean error;

    private String serviceId;

    /**
     * 
     */
    public HttpClientMessageImpl() {
        this.replyMsg = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#close()
     */
    public void close() throws SystemException {
        if (this.xmlApi != null) {
            this.xmlApi.close();
            this.xmlApi = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.MessageManager#connect(com.api.messaging.ProviderConfig
     * )
     */
    public Object connect(ProviderConfig config)
            throws ProviderConnectionException {
        if (config == null) {
            this.msg = "Failed to establish a HTTP client connection due to an invalid or null ProviderConfig object";
            logger.log(Level.ERROR, this.msg);
            throw new ProviderConnectionException(this.msg);
        }
        try {
            this.client = new HttpClient(config.getHost());
            this.config = config;
        } catch (HttpException e) {
            throw new ProviderConnectionException(e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.webservice.http.client.HttpMessageSender#receiveClientData
     * ()
     */
    public void receiveRequest() throws HttpClientMessageException {
        if (this.request == null) {
            this.msg = "The user's request instance is invalid";
            logger.error(this.msg);
            throw new HttpClientMessageException(this.msg);
        }

        Properties prop = new Properties();
        // Copy the remaining request parameter and attribute key/value pairs.
        prop = RMT2WebUtility.getRequestData(this.request, prop);

        // Validate Service Id existence
        this.serviceId = prop.getProperty(GeneralConst.REQ_CLIENTACTION);
        if (this.serviceId == null) {
            this.msg = "Unable to determine the service id for the HTTP URL Web service call due to invalid value";
            logger.error(this.msg);
            throw new HttpClientMessageException(this.msg);
        }
        prop.setProperty(GeneralConst.REQ_SERVICEID, serviceId);
        this.prop = prop;

        // Added condition to eliminate compiler warning!
        if (this.response == null) {

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.webservice.http.client.HttpMessageSender#processRequest
     * ()
     */
    public void processRequest() throws HttpClientMessageException {
        // Prepare to invoke remote service
        try {
            this.receiveRequest();
            Object results = this.sendMessage(this.prop);
            this.packageResults(results);
        } catch (Exception e) {
            throw new HttpClientMessageException(e);
        }
    }

    /**
     * Processes the results of the HTTP web service call and packages the
     * processed results into a member variable designated to hold the results.
     * By default, the final results of the service call exists as String
     * (generally an XML document). Override this method to provide more of a
     * customized implemntation. If a custom implementation is provided, ensure
     * that a call is made to {@link setServiceResults(Object)
     * setServiceResults} passing the final data results as a generic parameter.
     * <p>
     * If this method id overridden, be sure to call the getServiceResults()
     * method at the descendent level in order to further process the value
     * returned from the remote call.
     * 
     * @param results
     *            InputStream
     * @throws ServiceHandlerException
     */
    public void packageResults(Object results)
            throws HttpClientMessageException {
        InputStream in;
        if (results == null) {
            this.replyMsg = null;
            return;
        }
        if (results instanceof InputStream) {
            in = (InputStream) results;
        }
        else {
            return;
        }

        Object data;
        try {
            data = RMT2File.getStreamObjectData(in);
            if (data instanceof RMT2Exception) {
                this.msg = ((RMT2Exception) data).getMessage();
                throw new SystemException(this.msg,
                        ((RMT2Exception) data).getErrorCode());
            }
            if (data instanceof Exception) {
                this.msg = ((Exception) data).getMessage();
                throw new SystemException(this.msg);
            }
            this.verifyServiceResults(data);
            this.replyMsg = data;
            return;
        } catch (SystemException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new HttpClientMessageException(
                    "Error occurred processing reply from HTTP URL call", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getMessage()
     */
    public Serializable getMessage() throws MessageException {
        Serializable msg = null;
        try {
            msg = (Serializable) this.replyMsg;
            return msg;
        } catch (Exception e) {
            this.msg = "Reply message must of type Serializable: "
                    + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new MessageException(this.msg);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#sendMessage(java.io.Serializable)
     */
    public Object sendMessage(Serializable data) throws MessageException {
        InputStream is;
        try {
            if (data == null) {
                is = this.client.sendPostMessage();
            }
            else {
                // Send as a Properites object or as a Serialized object
                if (data instanceof Properties) {
                    is = this.client.sendPostMessage((Properties) data);
                }
                else {
                    is = this.client.sendPostMessage(data);
                }
            }
            return is;
        } catch (HttpException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Determines the success or failure of the service call based on the return
     * type of the results. The service is considered to have failed if the data
     * type of the result is of type Exception or of type String which the value
     * is a XML document where the token, <i>"<error>"</i>, can be found.
     * 
     * @see util.RMT2Exception#getMessageAsXml() getMessageAsXml() for a full
     *      explanation of the format for an exception message as XML.
     *      <p>
     *      When the data type of <i>data</i> is a String, it is safe to assume,
     *      regardless of the success or failure of the service call, that
     *      <i>data</i> is a XML document and this method creates a XML DaoApi
     *      member instance from the document.
     * 
     * @param data
     *            An arbitrary object representing the results of the service
     *            call. Its runtime type will be either an Exception or a
     *            String.
     */
    private void verifyServiceResults(Object data) {
        this.error = false;
        if (data == null) {
            return;
        }
        // Determine if error occurred.
        if (data instanceof Exception) {
            this.error = true;
        }
        else if (data instanceof String) {
            List<String> status = RMT2XmlUtility.getElementValue(
                    "return_status", "return_status", (String) data);
            // this.xmlApi = XmlApiFactory.createXmlDao(data.toString());
            if (status != null && status.size() == 1
                    && status.get(0).equalsIgnoreCase("ERROR")) {
                this.error = true;
            }
        }
        return;
    }

    /**
     * Returns the XML results of the service call as a DaoApi instance.
     * 
     * @return {@link com.api.DaoApi DaoApi}
     */
    public DaoApi getXmlResults() {
        return this.xmlApi;
    }

    /**
     * Indicates whether an error occurred.
     * 
     * @return Returns true for service call failure and false for success.
     */
    public boolean isError() {
        return this.error;
    }

    /**
     * @param request
     *            the request to set
     */
    void setRequest(Request request) {
        this.request = request;
    }

    /**
     * @param response
     *            the response to set
     */
    void setResponse(Response response) {
        this.response = response;
    }

    /**
     * @return the serviceId
     */
    String getServiceId() {
        return serviceId;
    }

}
