package com.api.messaging.webservice.soap.client;

import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Constants;
import com.SystemException;
import com.api.Product;
import com.api.ProductBuilderException;
import com.api.ProductDirector;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.http.HttpClient;
import com.api.messaging.webservice.soap.SoapMessageHelper;
import com.util.RMT2File;

/**
 * An implementation of {@link SoapClient} which functions only to send
 * request-response SOAP messages when no SAAJ/JAMX provider is available.
 * <p>
 * Since no provider is used, the msessages can only be sent synchronously
 * through HTTP using a concrete URL.
 * 
 * @author Roy Terrell
 * 
 */
class SoapTransportImpl extends AbstractSoapClientImpl implements SoapClient {

    private static Logger logger = Logger.getLogger("SoapTransportImpl");

    private SOAPConnection connection;

    private SOAPMessage soapMessage;

    private URL endpoint;

    private ProviderConfig config;

    /**
     * 
     */
    protected SoapTransportImpl() throws SOAPException {
        super();
        return;
    }

    /**
     * 
     * @param config
     * @return
     * @throws ProviderConnectionException
     */
    public SOAPConnection connect(ProviderConfig config)
            throws ProviderConnectionException {
        logger.info("Obtaining SOAP web service engine connection information for host: "
                + config.getHost());
        this.config = config;
        try {
            SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
            this.connection = sfc.createConnection();
            this.endpoint = new URL(config.getHost());
            return this.connection;
        } catch (SOAPException e) {
            throw new ProviderConnectionException(e);
        } catch (MalformedURLException e) {
            this.msg = "Problem establishing a SOAP connection to host.  The following URL is malformed or incorrect: "
                    + (config.getHost() == null ? "[Server Unknown]" : config
                            .getHost());
            SoapTransportImpl.logger.log(Level.ERROR, this.msg);
            throw new ProviderConnectionException(this.msg, e);
        }
    }

    /**
     * 
     * @throws SystemException
     */
    public void close() throws SystemException {
        try {
            this.connection.close();
            this.connection = null;
            this.endpoint = null;
            return;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * 
     * @param data
     * @return
     * @throws MessageException
     */
    public SOAPMessage sendMessage(Serializable data) throws MessageException {
        String xml = null;
        if (data instanceof String) {
            xml = data.toString();
        }
        else {
            this.msg = "SOAP message send opertaion failed.  Incoming SOAP message must be a Serializable String";
            SoapTransportImpl.logger.error(this.msg);
            throw new MessageException(this.msg);
        }

        SoapMessageHelper helper = new SoapMessageHelper();
        SOAPMessage sm = helper.toInstance(xml);
        SOAPMessage response = this.sendMessage(sm);
        return response;

        // try {
        // SoapMessageHelper helper = new SoapMessageHelper();
        // SOAPMessage sm = helper.toInstance(xml);
        // SOAPMessage response = this.sendMessage(sm);
        // return response;
        // }
        // catch (SoapBuilderException e) {
        // throw e;
        // }
    }

    public SOAPMessage sendMessage(Serializable msg, List<Object> attachments)
            throws MessageException {
        try {
            SoapMessageHelper helper = new SoapMessageHelper();
            SOAPMessage sm = helper.getSoapInstance(msg, attachments);
            SOAPMessage response = this.sendMessage(sm);
            return response;
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        }
    }

    /**
     * Expects the consumer to send the response as a valid SOAP String so that
     * "call" can return the SOAP String as a SOAPMessage instance.
     * 
     * @param soapMsg
     * @return
     * @throws MessageException
     */
    private SOAPMessage sendMessage(SOAPMessage soapMsg)
            throws MessageException {
        SoapMessageHelper helper = new SoapMessageHelper();

        String req = helper.getSoap(soapMsg);
        logger.info("Sending SOAP request to " + this.endpoint);
        logger.info("SOAP request: " + req);

        SOAPMessage response = null;
        try {
            response = this.connection.call(soapMsg, this.endpoint);
            logger.info("SOAP request completed");
            this.soapMessage = response;
        } catch (SOAPException e) {
            this.msg = "Error occurred sending SOAP message at location: "
                    + this.endpoint.toString();
            throw new SoapBuilderException(this.msg, e);
        }

        String resp = helper.getSoap(response);
        logger.info("SOAP response: " + resp);
        return response;
    }

    /**
     * Sends a SOAP message via the HttpClient instance. Currently not used.
     * Will put to use in the near future.
     * 
     * @param data
     *            the XML to transport and process.
     * @return SOAPMessage
     * @throws MessageException
     */
    public SOAPMessage sendStringMessage(Serializable data)
            throws MessageException {
        try {
            HttpClient client = new HttpClient(config.getHost());
            InputStream in = client.sendPostMessage(data);
            String responseXml = RMT2File.getStreamStringData(in);

            // Deserialize response
            SoapProductBuilder builder = SoapClientFactory
                    .getSoapBuilderInstance(responseXml);
            Product xmlProd = ProductDirector.construct(builder);
            this.soapMessage = (SOAPMessage) xmlProd.toObject();
            client.close();
            return this.soapMessage;
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        }
    }

    /**
     * Returns the SOAP message instance as a String
     * 
     * @return String
     * @throws MessageException
     */
    public String getMessage() throws MessageException {
        if (this.soapMessage == null) {
            return null;
        }
        try {
            SoapProductBuilder builder = SoapClientFactory
                    .getSoapBuilderInstance(this.soapMessage);
            Product xmlProd = ProductDirector.deConstruct(builder);
            logger.info("SOAP response message:");
            logger.info(xmlProd.toString());
            return xmlProd.toString();
        } catch (ProductBuilderException e) {
            throw new MessageException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getConfig()
     */
    public ProviderConfig getConfig() throws MessageException {
        return this.config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getHost()
     */
    public String getHost() {
        return this.config.getHost();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#isAuthRequired()
     */
    public boolean isAuthRequired() {
        throw new UnsupportedOperationException(
                RMT2Constants.MSG_METHOD_NOT_SUPPORTED);
    }

}
