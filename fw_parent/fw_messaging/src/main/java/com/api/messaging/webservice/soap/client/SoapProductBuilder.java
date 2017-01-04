package com.api.messaging.webservice.soap.client;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import com.api.ProductBuilder;
import com.api.persistence.DatabaseException;

/**
 * Provides the functionality needed to handle a document-oriented XML messages,
 * mainly SOAP type messages. Primarily, this class is used to package XML SOAP
 * content into SOAP message instances and vice versa.
 * 
 * @author appdev
 * 
 */
public interface SoapProductBuilder extends ProductBuilder {
    /**
     * 
     */
    public static final String SOAP_ENVELOPE_NS = "SOAP-ENV";

    /**
     * 
     */
    public static final String HEADER_NS = "rmt2-hns";

    /**
     * 
     */
    public static final String FAULT_NS = "rmt2-fns";

    /**
     * 
     */
    public static final String FAULT_DETAIL_NAME = "faultdetails";

    /**
     * @deprecated no longer in use
     */
    public static final String SERVICEID_NAME = "serviceId";

    /**
     * 
     */
    public static final String QUEUE_NAME = "queueName";

    /**
     * 
     */
    public static final String PAYLOAD = "payload";

    /**
     * 
     */
    public static final String PAYLOADINSTANCE = "payloadinstance";

    /**
     * 
     * @param elementName
     * @param value
     * @return
     * @throws SoapBuilderException
     */
    SOAPHeaderElement addHeaderElement(String elementName, String value)
            throws SoapBuilderException;

    /**
     * 
     * @param elementName
     * @param value
     * @param actor
     * @param required
     * @param encoding
     * @return
     * @throws SoapBuilderException
     */
    SOAPElement addHeaderElement(String elementName, String value,
            String actor, boolean required, String encoding)
            throws SoapBuilderException;

    /**
     * 
     * @param bodyData
     * @throws SoapBuilderException
     */
    void addBody(String bodyData) throws SoapBuilderException;

    /**
     * 
     * @param faultCode
     * @param faultMsg
     * @param faultActor
     * @param faultDetails
     * @throws SoapBuilderException
     */
    void addFault(String faultCode, String faultMsg, String faultActor,
            Map faultDetails) throws SoapBuilderException;

    /**
     * Adds one or more attachments to the SOAP message instance.
     * 
     * @param attachments
     *            a list of data representing one or more attachments to add.
     * @return int the total number of attachments successfully added.
     * @throws SoapBuilderException
     */
    int createAttachments(List<Object> attachments) throws SoapBuilderException;

    /**
     * 
     * @param urlPath
     *            the URL where the attachment content is located.
     * @return
     * @throws SoapBuilderException
     */
    AttachmentPart createAttachment(String urlPath) throws SoapBuilderException;

    /**
     * 
     * @param file
     * @return
     * @throws SoapBuilderException
     */
    AttachmentPart createAttachment(File file) throws SoapBuilderException;

    /**
     * 
     * @param data
     * @return
     * @throws SoapBuilderException
     */
    AttachmentPart createAttachment(InputStream data)
            throws SoapBuilderException;

    /**
     * 
     * @param data
     * @return
     * @throws SoapBuilderException
     */
    AttachmentPart createAttachment(DataHandler data)
            throws SoapBuilderException;

    /**
     * 
     * @param out
     * @return
     * @throws SoapBuilderException
     */
    Object outputMessage(OutputStream out) throws SoapBuilderException;

    /**
     * 
     * @param out
     * @param soapMessageObj
     * @return
     * @throws SoapBuilderException
     */
    Object outputMessage(OutputStream out, SOAPMessage soapMessageObj)
            throws SoapBuilderException;

    /**
     * 
     * @param propName
     * @return
     * @throws SoapBuilderException
     */
    List<String> getElementValue(String propName) throws SoapBuilderException;

    /**
     * 
     * @param xPath
     * @return
     * @throws DatabaseException
     */
    int locateXmlNode(String xPath) throws DatabaseException;
}
