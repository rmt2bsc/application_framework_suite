package com.api.messaging.webservice.soap.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimePartDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.DaoApi;
import com.api.Product;
import com.api.ProductBuilderException;
import com.api.persistence.DatabaseException;
import com.api.xml.RMT2XmlUtility;
import com.api.xml.XmlApiFactory;

/**
 * 
 * @author appdev
 * 
 */
class SoapProductBuilderImpl extends RMT2Base implements SoapProductBuilder {

    private static Logger logger = Logger
            .getLogger(SoapProductBuilderImpl.class);

    private String soapStr;

    private SOAPMessage soapMessageObj;

    private Map<String, String> fautlCodes;

    private DaoApi dao;

    private String serviceId;

    private String queueName;

    private String body;

    private String faultCode;

    private String faultMessage;

    private String faultActor;

    private Map faultDetails;

    private Product product;

    private boolean buildCustom;

    private boolean buildFromString;

    private boolean buildFromObj;

    private boolean buildBodyUsingParms;

    private boolean buildFaultUsingParms;

    /**
     * Create a SoapProductBuilderImpl object requiring the user to customize
     * the construction for the SOAP message using its various methods.
     */
    protected SoapProductBuilderImpl() {
        super();
        return;
    }

    /**
     * Construct a SoapProductBuilderImpl object that will be used to
     * disassemble a SOAPMessage instance to a String.
     * 
     * @param message
     *            SOAPMessage instance to disassemble.
     */
    protected SoapProductBuilderImpl(SOAPMessage message) {
        this();
        this.soapMessageObj = message;
        this.buildFromObj = true;
        return;
    }

    /**
     * Construct a SoapProductBuilderImpl object that will be used to assemble a
     * SOAPMessage instance from a String SOAP message.
     * 
     * @param message
     *            String representation of the a SOAP message.
     */
    protected SoapProductBuilderImpl(String message) {
        this();
        this.soapStr = message;
        this.buildFromString = true;
        return;
    }

    /**
     * Construct a SoapProductBuilderImpl object intended for assembling a RMT2
     * related or generic SOAP message with normal body. The product of the
     * assembly will be an instance of SOAPMessage.
     * 
     * @param serviceId
     *            the id of the service that will process the message. This
     *            parameter is required and is typically used when the assembly
     *            of a RMT2 relate message is desired.
     * @param queueName
     *            the name of the JMS queue where the message should be sent.
     *            This parameter is optional and is typically used when the
     *            assembly of a RMT2 relate message is desired.
     * @param body
     *            the data that will represent the SOAP message's envelope body.
     *            This parameter is required.
     */
    protected SoapProductBuilderImpl(String serviceId, String queueName,
            String body) {
        this();
        this.serviceId = serviceId;
        this.queueName = queueName;
        this.body = body;
        this.buildBodyUsingParms = true;
        return;
    }

    /**
     * Construct a SoapProductBuilderImpl object intended for assembling a RMT2
     * related or generic SOAP Fault message. The product of the assembly will
     * be an instance of SOAPMessage.
     * 
     * @param serviceId
     *            the id of the service that will process the message. This
     *            parameter is optional and is typically used when the assembly
     *            of a RMT2 relate message is desired.
     * @param queueName
     *            the name of the JMS queue where the message should be sent.
     *            This parameter is optional and is typically used when the
     *            assembly of a RMT2 relate message is desired.
     * @param faultCode
     *            the code indicating the cause of the SOAP fault and will
     *            appear as a subelement of the Fault element. Valid fault code
     *            values are <i>VerisionMismatch</i>, <i>MustUnderstand</i>,
     *            <i>Client</i>, and <i>Server</i>. This paramter is required.
     * @param faultMessage
     *            a human readable description of the fault element. This
     *            parameter is required due to it must appear as a subelement of
     *            the Fault element.
     * @param faultActor
     *            this is an indication of the system tht was responsible for
     *            the fault. This parameter is optional.
     * @param faultDetails
     *            a String representing a XML document providing additional
     *            information related to the Fault element. This parameter is
     *            optional.
     */
    protected SoapProductBuilderImpl(String serviceId, String queueName,
            String faultCode, String faultMessage, String faultActor,
            Map faultDetails) {
        this();
        this.serviceId = serviceId;
        this.queueName = queueName;
        this.faultCode = faultCode;
        this.faultMessage = faultMessage;
        this.faultActor = faultActor;
        this.faultDetails = faultDetails;
        this.buildFaultUsingParms = true;
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bean.RMT2Base#init()
     */
    @Override
    public void init() {
        super.init();
        this.product = Product.getInstance();
        this.soapStr = null;
        this.serviceId = null;
        this.queueName = null;
        this.body = null;
        this.faultCode = null;
        this.faultMessage = null;
        this.faultActor = null;
        this.faultDetails = null;
        this.buildCustom = false;
        this.buildFromString = false;
        this.buildBodyUsingParms = false;
        this.buildFaultUsingParms = false;

        // Setup Fault Codes map
        this.fautlCodes = new HashMap<String, String>();
        this.fautlCodes.put("VersionMisMatch", "SOAP-ENV:VersionMisMatch");
        this.fautlCodes.put("MustUnderstand", "SOAP-ENV:MustUnderstand");
        this.fautlCodes.put("Client", "SOAP-ENV:Client");
        this.fautlCodes.put("Server", "SOAP-ENV:Server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.ProductBuilder#assemble()
     */
    public void assemble() throws ProductBuilderException {
        if (this.buildFromString) {
            this.soapMessageObj = this.assembleFromString();
            // Capture String representation
            this.product.setStringVal(this.soapStr);
        }
        else if (this.buildBodyUsingParms) {
            this.soapMessageObj = this.assembleBodyUsingParms();
            // Capture String representation
            this.disAssemble();
        }
        else if (this.buildFaultUsingParms) {
            this.soapMessageObj = this.assembleFaultUsingParms();
            // Capture String representation
            this.disAssemble();
        }
        else {
            this.buildCustom = true;
        }

        // Set product with the message object just created.
        this.product.setObjectVal(this.soapMessageObj);

    }

    private SOAPMessage assembleFromString() throws ProductBuilderException {
        if (this.soapStr == null) {
            this.msg = "SOAP body assembly failed due to SOAP Message String is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(
                this.soapStr.getBytes());
        StreamSource xmlSrc = new StreamSource(bais);
        try {
            SOAPMessage spMsg = MessageFactory.newInstance().createMessage();
            SOAPPart sp = spMsg.getSOAPPart();
            sp.setContent(xmlSrc);
            spMsg.saveChanges();
            return spMsg;
        } catch (SOAPException e) {
            throw new ProductBuilderException(e);
        }
    }

    private SOAPMessage assembleBodyUsingParms() throws ProductBuilderException {
        if (this.serviceId == null) {
            this.msg = "SOAP body assembly failed due to the absence of the Service Id";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        if (this.body == null) {
            this.msg = "SOAP Builder assembly failed due to absence of Body data";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        try {
            if (this.soapMessageObj == null) {
                this.soapMessageObj = MessageFactory.newInstance()
                        .createMessage();
            }
            this.addHeaderElement(SoapProductBuilder.SERVICEID_NAME,
                    this.serviceId, null, true, null);
            if (this.queueName != null) {
                this.addHeaderElement(SoapProductBuilder.QUEUE_NAME,
                        this.queueName, null, true, null);
            }
            this.addBody(this.body);
            return this.soapMessageObj;
        } catch (Exception e) {
            throw new ProductBuilderException(e);
        }
    }

    private SOAPMessage assembleFaultUsingParms()
            throws ProductBuilderException {
        if (this.faultCode == null) {
            this.msg = "SOAP fault assembly failed due to the absence of fault code";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        if (this.faultMessage == null) {
            this.msg = "SOAP fault assembly failed due to absence of the fault message text";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        try {
            if (this.soapMessageObj == null) {
                this.soapMessageObj = MessageFactory.newInstance()
                        .createMessage();
            }
            if (this.serviceId != null) {
                this.addHeaderElement(SoapProductBuilder.SERVICEID_NAME,
                        this.serviceId, null, true, null);
            }
            if (this.queueName != null) {
                this.addHeaderElement(SoapProductBuilder.QUEUE_NAME,
                        this.queueName, null, true, null);
            }
            this.addFault(this.faultCode, this.faultMessage, this.faultActor,
                    this.faultDetails);
            return this.soapMessageObj;
        } catch (Exception e) {
            throw new ProductBuilderException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.ProductBuilder#disAssemble()
     */
    public void disAssemble() throws ProductBuilderException {
        if (this.soapMessageObj == null) {
            this.msg = "SOAP Builder assembly failed due to SOAP Message Object is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new ProductBuilderException(this.msg);
        }
        String strMsg;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.outputMessage(baos);
            strMsg = baos.toString();
            this.product.setStringVal(strMsg);
            this.setSoapStr(strMsg);
        } catch (Exception e) {
            throw new ProductBuilderException(e);
        }
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.ProductBuilder#getProduct()
     */
    public Product getProduct() {
        return this.product;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#addBody(java.lang.String
     * )
     */
    public void addBody(String bodyData) throws SoapBuilderException {
        if (bodyData == null) {
            this.msg = "Problem adding body to SOAP envelope due to body data is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        try {
            if (this.soapMessageObj == null) {
                this.soapMessageObj = MessageFactory.newInstance()
                        .createMessage();
            }
            SOAPPart soapPart = this.soapMessageObj.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();

            // Create Document instance from data
            Document doc = RMT2XmlUtility.stringToDocument(bodyData);
            soapBody.addDocument(doc);
        } catch (SOAPException e) {
            this.msg = "A SOAP error was detected as a result of trying to add a XML Doucument instance to the SOAP Body";
            logger.error(this.msg);
            throw new SoapBuilderException(this.msg, e);
        } catch (SystemException e) {
            this.msg = "A system error was detected as a result of trying to add a XML Doucument instance to the SOAP Body";
            logger.error(this.msg);
            throw new SoapBuilderException(this.msg, e);
        } catch (Exception e) {
            this.msg = "A general error was detected as a result of trying to add a XML Doucument instance to the SOAP Body";
            logger.error(this.msg);
            throw new SoapBuilderException(this.msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#addFault(java.lang.
     * String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addFault(String faultCode, String faultMsg, String faultActor,
            Map faultDetails) throws SoapBuilderException {
        if (faultCode == null) {
            this.msg = "Problem adding Fault to SOAP envelope due to fault code is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        if (faultMsg == null) {
            this.msg = "Problem adding Fault to SOAP envelope due to fault message text is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        try {
            if (this.soapMessageObj == null) {
                this.soapMessageObj = MessageFactory.newInstance()
                        .createMessage();
            }
            if (this.soapMessageObj == null) {
                this.msg = "Add Fault operation failed due to internal SOAP message is invalid or null";
                SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
                throw new SoapBuilderException(this.msg);
            }
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        }

        try {
            SOAPPart soapPart = this.soapMessageObj.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

            // try {
            // String uri = null;
            // uri =
            // RMT2XmlUtility.getXmlNamespaceUri(SoapProductBuilder.FAULT_NS);
            // soapEnvelope.addNamespaceDeclaration(SoapProductBuilder.FAULT_NS,
            // uri);
            // }
            // catch (Exception e) {
            // throw new SoapBuilderException(e);
            // }

            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPFault fault;
            if (soapBody.hasFault()) {
                fault = soapBody.getFault();
            }
            else {
                try {
                    fault = soapBody.addFault();
                } catch (Throwable t) {
                    throw new SOAPException(t.getMessage());
                }
            }

            if (this.fautlCodes.get(faultCode) == null) {
                throw new SoapBuilderException("Fault code is invalid");
            }
            String fc = this.fautlCodes.get(faultCode);
            fault.setFaultCode(fc);
            fault.setFaultString(faultMsg);

            if (faultDetails != null && !faultDetails.isEmpty()) {
                Detail details = fault.getDetail();
                if (details == null) {
                    details = fault.addDetail();
                }
                this.addFaultDetails(details, faultDetails);
            }
        } catch (SOAPException e) {
            throw new SoapBuilderException(e);
        }
    }

    private void addFaultDetails(Detail detail, Map faultDetail)
            throws SOAPException {
        if (detail == null) {
            return;
        }
        if (faultDetail == null || faultDetail.isEmpty()) {
            return;
        }
        Set keys = faultDetail.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object val = faultDetail.get(key);
            SOAPFactory factory = SOAPFactory.newInstance();
            Name n = factory.createName(key);
            DetailEntry entry = detail.addDetailEntry(n);
            entry.addTextNode(val.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#addHeaderElement(java
     * .lang.String, java.lang.String, java.lang.String, boolean,
     * java.lang.String)
     */
    public SOAPElement addHeaderElement(String elementName, String value,
            String actor, boolean required, String encoding)
            throws SoapBuilderException {
        SOAPHeaderElement she = this.addHeaderElement(elementName, value);
        she.setActor(actor);
        she.setMustUnderstand(required);
        try {
            if (encoding != null) {
                she.setEncodingStyle(encoding);
            }
        } catch (SOAPException e) {
            throw new SoapBuilderException(e);
        }
        return she;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#addHeaderElement(java
     * .lang.String, java.lang.String)
     */
    public SOAPHeaderElement addHeaderElement(String elementName, String value)
            throws SoapBuilderException {
        try {
            if (this.soapMessageObj == null) {
                this.soapMessageObj = MessageFactory.newInstance()
                        .createMessage();
            }
            SOAPPart soapPart = this.soapMessageObj.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();

            // All immediate child elements of the Header element must be
            // namespace-qualified
            String uri = null;
            if (soapHeader.getNamespaceURI(SoapProductBuilder.HEADER_NS) == null) {
                try {
                    uri = RMT2XmlUtility
                            .getXmlNamespaceUri(SoapProductBuilder.HEADER_NS);
                } catch (Exception e) {
                    this.msg = "All immediate child elements of the Header element must be namespace-qualified by "
                            + SoapProductBuilder.HEADER_NS;
                    throw new SoapBuilderException(this.msg, e);
                }
            }

            Name n = null;
            try {
                if (uri == null) {
                    // do not include namespace
                    // Base on changes made to
                    // com.api.xml.RMT2NamespaceContext.properties, uri should
                    // always be null.
                    n = SOAPFactory.newInstance().createName(elementName);
                }
                else {
                    // include namespace
                    n = SOAPFactory.newInstance().createName(elementName,
                            SoapProductBuilder.HEADER_NS, uri);
                }
                SOAPHeaderElement headerElement = soapHeader
                        .addHeaderElement(n); // soapHeader.addChildElement(elementName,
                                              // SoapProductBuilder.HEADER_NS);
                headerElement.addTextNode(value);
                return headerElement;
            } catch (Throwable e) {
                throw new SoapBuilderException(e.getMessage());
            }
        } catch (SOAPException e1) {
            throw new SoapBuilderException(e1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#disAssemble(javax.xml
     * .soap.SOAPMessage)
     */
    private Node toDom(SOAPMessage msg) throws SoapBuilderException {
        this.soapMessageObj = msg;
        return this.toDom();
    }

    /**
     * 
     */
    private Node toDom() throws SoapBuilderException {
        if (this.soapMessageObj == null) {
            this.msg = "SOAP Builder assembly failed due to SOAP Message Object is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        try {
            Source source = this.soapMessageObj.getSOAPPart().getContent();
            Node root = null;
            if (source instanceof DOMSource) {
                root = ((DOMSource) source).getNode();
            }
            else if (source instanceof StreamSource) {
                InputStream is = ((StreamSource) source).getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                Document doc = db.parse(is);
                root = (Node) doc.getDocumentElement();
            }
            else if (source instanceof SAXSource) {
                InputSource inSource = ((SAXSource) source).getInputSource();
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                Document doc = db.parse(inSource);
                root = (Node) doc.getDocumentElement();
            }
            return root;
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapProductBuilder#locateXmlNode(java.lang
     * .String)
     */
    public int locateXmlNode(String xPath) throws DatabaseException {
        this.dao = XmlApiFactory.createXmlDao(this.soapStr);
        // Perform query
        Object result[] = this.dao.retrieve(xPath);
        int rows = ((Integer) result[0]).intValue();
        if (rows <= 0) {
            this.msg = "XPath query returned an empty result set";
            logger.log(Level.WARN, this.msg);
        }
        return rows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#getElementValue(java
     * .lang.String, java.lang.String, com.api.DaoApi)
     */
    public List<String> getElementValue(String propName)
            throws SoapBuilderException {
        if (this.dao == null) {
            this.msg = "Method, getElementValue, failed due to internal SOAP DAO object is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        List<String> results = new ArrayList<String>();
        try {
            // Begin to retrieve the results
            while (this.dao.nextRow()) {
                try {
                    String val = this.dao.getColumnValue(propName);
                    results.add(val);
                } catch (NotFoundException e) {
                    msg = "Property, "
                            + propName
                            + ", did not exist in XML Api result set.  Additional information: "
                            + e.getMessage();
                    logger.log(Level.WARN, this.msg);
                }
            } // Go to next element
            return results;
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        } finally {
            logger.log(Level.INFO, "Total number of recors retrieved: "
                    + results.size());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#outputMessage(java.
     * io.OutputStream, javax.xml.soap.SOAPMessage)
     */
    public Object outputMessage(OutputStream out, SOAPMessage soapMessageObj)
            throws SoapBuilderException {
        this.soapMessageObj = soapMessageObj;
        return this.outputMessage(out);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapMessageHelper_Old#outputMessage(java.
     * io.OutputStream)
     */
    public Object outputMessage(OutputStream out) throws SoapBuilderException {
        try {
            Node root = this.toDom();

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Use default output from System class
            transformer.transform(new DOMSource(root), new StreamResult(
                    System.out));
            // Use input parameter as output stream
            transformer.transform(new DOMSource(root), new StreamResult(out));

            return null;
        } catch (Exception e) {
            throw new SoapBuilderException(e);
        }
    }

    /**
     * Adds attachments to the SOAP message.
     * 
     * @param attachments
     * @return
     * @throws SoapBuilderException
     */
    public int createAttachments(List<Object> attachments)
            throws SoapBuilderException {
        int count = 0;
        if (attachments == null) {
            return -1;
        }
        for (Object attachment : attachments) {
            if (attachment instanceof File) {
                this.createAttachment((File) attachment);
                count++;
            }
            if (attachment instanceof String) {
                this.createAttachment(attachment.toString());
                count++;
            }
            if (attachment instanceof InputStream) {
                this.createAttachment((InputStream) attachment);
                count++;
            }
            if (attachment instanceof DataHandler) {
                this.createAttachment((DataHandler) attachment);
                count++;
            }
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.http.soap.SoapProductBuilder#addAttachment(java.lang
     * .String)
     */
    public AttachmentPart createAttachment(String urlPath)
            throws SoapBuilderException {
        if (this.soapMessageObj == null) {
            this.msg = "SOAP URL addAttachment failed due to SOAP Message Object is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        URL url = null;
        try {
            url = new URL(urlPath);
        } catch (MalformedURLException e) {
            this.msg = "The URL , "
                    + urlPath
                    + ", for SOAP addAttachment method is syntactically incorrect.  Other Message: "
                    + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        DataSource ds = new URLDataSource(url);
        DataHandler dataHandler = new DataHandler(ds);
        return this.createAttachment(dataHandler);
    }

    public AttachmentPart createAttachment(File file)
            throws SoapBuilderException {
        if (this.soapMessageObj == null) {
            this.msg = "SOAP File addAttachment failed due to SOAP Message Object is invalid or null";
            SoapProductBuilderImpl.logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        if (file == null) {
            this.msg = "Creation of File SOAP Attachment failed due to an null File instance reference";
            logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        if (file.isDirectory()) {
            this.msg = "Creation of File SOAP Attachment failed due to File instance must point to a file resource instead of a directory";
            logger.log(Level.ERROR, this.msg);
            throw new SoapBuilderException(this.msg);
        }
        DataSource ds = new FileDataSource(file);
        DataHandler dataHandler = new DataHandler(ds);
        return this.createAttachment(dataHandler);
    }

    public AttachmentPart createAttachment(InputStream data)
            throws SoapBuilderException {
        MimeBodyPart mbp = null;
        try {
            mbp = new MimeBodyPart(data);
        } catch (MessagingException e) {
            throw new SoapBuilderException(e);
        }
        MimePartDataSource mpds = new MimePartDataSource(mbp);
        DataHandler dataHandler = new DataHandler(mpds);
        return this.createAttachment(dataHandler);
    }

    public AttachmentPart createAttachment(DataHandler data)
            throws SoapBuilderException {
        AttachmentPart ap = this.soapMessageObj.createAttachmentPart(data);
        ap.setContentId("attachment" + Math.random());
        this.soapMessageObj.addAttachmentPart(ap);
        return ap;
    }

    /**
     * @param soapStr
     *            the soapStr to set
     */
    protected void setSoapStr(String soapStr) {
        this.soapStr = soapStr;
    }

    /**
     * @param soapMessageObj
     *            the soapMessageObj to set
     */
    protected void setSoapMessageObj(SOAPMessage soapObj) {
        this.soapMessageObj = soapObj;
    }

}
