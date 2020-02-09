package com.api.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.NotFoundException;
import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.constants.GeneralConst;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2File;
import com.api.util.RMT2Utility;

/**
 * A utility for performing commonly used XML operations. Conatins functionality
 * regarding XSLT transformation and converting java objects to XML.
 * <p>
 * The XSLT functionality offering contains XSLT technology to transform XML
 * data into meaningful output. The output can be in the form of XML, HTML,
 * FO-XSLT, and PDF. The Apache FO Processor is used to convert XSLT-FO
 * documents to PDF's which can be directed to disk or the browser.
 * <p>
 * Also contains functionality to convert javabeans to XML documents, which the
 * the XML documents created are String based. The following javabeans are
 * supported:
 * <ul>
 * <li>primitive data type</li>
 * <li>wrapper objects</li>
 * <li>non-native complex javabeans</li>
 * <li>List</li>
 * <li>Map</li>
 * <li>Set</li>
 * </ul>
 * The following java native classes are ignored when encountered as a top-level
 * class or nested:
 * <ul>
 * <li>Class</li>
 * <li>ObjectInput</li>
 * <li>FileOutputStream</li>
 * <li>FileInputStream</li>
 * <li>ObjectOutput</li>
 * </ul>
 * 
 * @author appdev
 * 
 */
public class RMT2XmlUtility extends RMT2Base {
    public static Logger logger = Logger.getLogger("RMT2XmlUtility");

    /**
     * Privately constructs a RMT2XmlUtility object.
     * 
     */
    protected RMT2XmlUtility() {
        return;
    }

    /**
     * Produces an instance of RMT2XmlUtility.
     * 
     * @return {@link RMT2XmlUtility}
     */
    public static RMT2XmlUtility getInstance() {
        return new RMT2XmlUtility();
    }

    /**
     * Checks a XML document for well-formness or validity.
     * 
     * @param doc
     *            A String which is the actual contents of the XML document to
     *            check.
     * @param checkValidity
     *            true instructs to perform a validity check. Otherwise,
     *            well-formness is checked.
     * @throws SystemException
     *             When <i>doc</i> is invalid or not well-formed or an I/O error
     *             occurred.
     */
    public void checkDocument(String doc, boolean checkValidity)
            throws SystemException {
        InputSource src = RMT2XmlUtility.createXmlDocument(doc);

        // Determine if validation should be enabled
        SAXBuilder builder = new SAXBuilder(checkValidity);

        // Use InputSource to validate XML
        try {
            // If there are no well-formedness or validity errors, then no
            // exception is thrown.
            builder.build(src);
        }
        // Indicates a well-formedness or validity error
        catch (JDOMException e) {
            logger.log(Level.ERROR,
                    "XML document was not valid or well-formed.");
            throw new SystemException(e);
        } catch (IOException e) {
            logger.log(Level.ERROR,
                    "I/O error occurred trying to validate XML document");
            throw new SystemException(e);
        }
    }

    /**
     * Transforms a XML document using XSL in which the transformed results are
     * saved as a file.
     * <p>
     * This method implies that the source and target documents are retrieved
     * from and stored to disk.
     * 
     * @param xsltFileName
     *            File name of the XSLT input document.
     * @param xmlFileName
     *            File name of the input XML document.
     * @param outFileName
     *            File name that will store the results of the transformation
     * @throws SystemException
     */
    public void transformXslt(String xsltFileName, String xmlFileName, String outFileName) throws SystemException {
        File xsltFile = new File(xsltFileName);
        File xmlFile = new File(xmlFileName);
        // File outFile = new File(outFileName);
        OutputStream results = this.transformXslt(xsltFile, xmlFile, outFileName);
        RMT2File.outputFile(((ByteArrayOutputStream) results).toByteArray(), outFileName);
        // String filename = outSrc.getSystemId();
        logger.info("XML document was successfully processed and the XSL transformation was saved as: " + outFileName);
    }

    /**
     * Performs the transformation of a XSLT document to a file using the path
     * names of the input sources and an output object that is a descendent of
     * either an OutputStream, File, or Writer object.
     * <p>
     * This method implies that the source documents are retrieved from and
     * stored to disk and "out" has been set up as an appropriate output object.
     * 
     * @param xsltFileName
     *            File name of the XSLT input document.
     * @param xmlFileName
     *            File name of the input XML document.
     * @param outSource
     *            This is the source of the resulting transformation output
     *            which is required to be either a String representing the
     *            filename, File, OutputStream, or a Writer
     * @return {@link OutputStream}
     * @throws SystemException
     */
    public OutputStream transformXslt(String xsltFileName, String xmlFileName, Object outSource) throws SystemException {
        File xsltFile = new File(xsltFileName);
        File xmlFile = new File(xmlFileName);
        return this.transformXslt(xsltFile, xmlFile, outSource);
    }

    /**
     * Performs the transformation of a XSLT document to a file.
     * <p>
     * The input and output documents are in the form of File objects. This
     * method implies that the source and target documents are retrieved from
     * and stored to disk.
     * 
     * @param xsltFile
     *            A non-null File reference pointing to the XSLT file.
     * @param xmlFile
     *            A non-null File reference pointing to the XML file.
     * @param outSource
     *            This is the source of the resulting transformation output
     *            which is required to be either a String representing the
     *            filename, File, OutputStream, or a Writer
     * @return {@link OutputStream}
     * @throws SystemException
     */
    public OutputStream transformXslt(File xsltFile, File xmlFile, Object outSource) throws SystemException {
        String fileName = null;

        // All file references cannot be null
        if (xsltFile != null && xmlFile != null && outSource != null) {
            // THe references are valid
        }
        else {
            this.msg = "The XSLT and/or XML document File references must be valid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }

        // Input files must exist and cannot be direcotires.
        fileName = xsltFile.getAbsolutePath();
        if (!xsltFile.exists()) {
            if (fileName == null || fileName.length() <= 0) {
                fileName = "<File Name Unknown";
            }
            this.msg = "XSLT document named, " + fileName
                    + " does not exist in file system";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        if (xsltFile.isDirectory()) {
            this.msg = "XSLT document named, " + fileName
                    + " cannot be a directory";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        fileName = xmlFile.getAbsolutePath();
        if (!xmlFile.exists()) {
            if (fileName == null || fileName.length() <= 0) {
                fileName = "<File Name Unknown";
            }
            this.msg = "XML document named, " + fileName
                    + " does not exist in file system";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        if (xmlFile.isDirectory()) {
            this.msg = "XML document named, " + fileName
                    + " cannot be a directory";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }

        // Setup Streams
        InputStream xsltStream = null;
        InputStream xmlStream = null;
        try {
            xsltStream = new FileInputStream(xsltFile);
            xmlStream = new FileInputStream(xmlFile);
            return this.transformXslt(xsltStream, xsltFile.getAbsolutePath(), xmlStream, outSource);
        } catch (FileNotFoundException e) {
            this.msg = "Output file is a directory rather than a regular file, does not exist, cannot be created, or cannot be opened for some other reason";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } catch (NullPointerException e) {
            this.msg = "One or more of the File arguments are invalid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } finally {
            try {
                xsltStream.close();
                xmlStream.close();
            } catch (IOException e) {
                this.msg = "A problem occurred closing either the XSLT, XML, or output files";
                logger.log(Level.ERROR, this.msg);
                throw new SystemException(this.msg, e);
            }
        }
    }

    /**
     * Performs the transformation of a XSLT document to its expected output
     * format.
     * <p>
     * The input and output documents are in the form of InputStream and
     * OutputStream objects, respectively. The input streams are converted to
     * StreamSource objects and the ouput stream is converted to a StreamResult
     * object.
     * <p>
     * The transformation results will be saved to a file in which the file name
     * is assigned to the memmber variable, <i><xslResultsFileName/i>
     * 
     * @param xsltStream
     *            A non-null InputStream object representing the XSLT document.
     * @param xsltPath
     *            The path of the XSLT document in the event the stream is
     *            derived from a file. Otherwise, it is legal to be null.
     * @param xmlStream
     *            A non-null InputStream object representing the XML document.
     * @param outSource
     *            This is the source of the resulting transformation output
     *            which is required to be either a String representing the
     *            filename, File, OutputStream, or a Writer
     * @return {@link OutputStream}
     * @throws SystemException
     */
    public OutputStream transformXslt(InputStream xsltStream, String xsltPath, InputStream xmlStream, Object outSource)
            throws SystemException {
        StreamSource xsltSrc = null;
        StreamSource xmlSrc = null;
        StreamResult outSrc = null;

        xsltSrc = new StreamSource(xsltStream, xsltPath);
        xmlSrc = new StreamSource(xmlStream);
        outSrc = this.initResultStream(outSource);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSrc);
            transformer.transform(xmlSrc, outSrc);
            return outSrc.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            this.msg = "XSLT Transformation error: " + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } finally {

        }
    }

    /**
     * Performs an in-memory transformation of a XSLT document to its expected
     * output format. The XSLT document is in the form of an InputStream, the
     * XML data input is in the form of a String, and output is in the form of
     * OutputStream object. The input streams are converted to StreamSource
     * objects and the ouput stream is converted to a StreamResult object.
     * 
     * @param xslFileName
     *            The filename of the XSL reosurce. The file name can either be
     *            absoulte or relative.
     * @param xml
     *            A String representing the XML document that is to be
     *            transformed.
     * @param outStream
     *            A generic object which its runtime type is evaluated as either
     *            File, OutputStream, or Writer instance. This will represent
     *            the results of the transformation.
     * @return the results as an instance of {@link OutputStream}
     * @throws SystemException
     *             For general transformation errors
     */
    public OutputStream transform(String xslFileName, String xml, Object out)
            throws SystemException {
        InputStream in = RMT2File.getFileInputStream(xslFileName);
        if (in == null) {
            this.msg = "An input stream could not be created for file, "
                    + xslFileName;
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        StreamSource xsltSrc = new StreamSource(in, null);
        StreamSource xmlSrc = new StreamSource(bais);
        StreamResult outSrc = this.initResultStream(out);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSrc);
            transformer.transform(xmlSrc, outSrc);
            return outSrc.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            this.msg = "XSLT Transformation error: " + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } finally {
            xsltSrc = null;
            xmlSrc = null;
        }
    }

    /**
     * Transform a XML input source instance to a String value.
     * 
     * @param source
     * @return String XML document
     * @throws SystemException
     */
    public String transform(Source source) throws SystemException {
        String msg = null;
        String xml = null;
        try {
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    "yes");
            transformer.transform(source, result);
            xml = stringWriter.getBuffer().toString();
            return xml;
        } catch (TransformerConfigurationException e) {
            msg = "TransformerConfigurationException while parsing soap body to string: "
                    + e.getMessage();
            logger.error(msg);
            logger.debug("Stack Trace: ", e);
            throw new SystemException(e);
        } catch (TransformerException e) {
            msg = "TransformerException while parsing soap body to string: "
                    + e.getMessage();
            logger.error(msg);
            logger.debug("Stack Trace: ", e);
            throw new SystemException(e);
        }
    }

    /**
     * Creates a StreamResult object out of outSrc.
     * 
     * @param outSrc
     *            An object of type OutputStream, Writer, or File.
     * @return StreamResult.
     * @throws SystemException
     *             When ourSrc is of an invalid type or is null.
     */
    private StreamResult initResultStream(Object outSrc) throws SystemException {
        StreamResult out = null;
        if (outSrc == null) {
            this.msg = "Output source cannot be null";
            throw new SystemException(this.msg);
        }
        if (outSrc instanceof String) {
            // outSrc is a String that will used persist the results of the
            // first transformation as a FO file
            OutputStream os = new ByteArrayOutputStream();
            out = new StreamResult(os);
        }
        else if (outSrc instanceof File) {
            out = new StreamResult((File) outSrc);
        }
        else if (outSrc instanceof OutputStream) {
            out = new StreamResult((OutputStream) outSrc);
        }
        else if (outSrc instanceof Writer) {
            out = new StreamResult((Writer) outSrc);
        }
        else {
            this.msg = "Output source is of a data type that is not applicable for converting to a StreamResult.  Must be of type OutputStream, Writer, or File";
            throw new SystemException(this.msg);
        }
        return out;
    }

    /**
     * Renders a XSLT Formatted Object document as a PDF stream in memeory.
     * 
     * @param srcFileName
     *            The XSLT-FO document to be rendered.
     * @return
     * @throws SystemException
     */
    public ByteArrayOutputStream renderPdf(String srcFileName) throws SystemException {
        ByteArrayOutputStream stream = this.generateXsltPdf(srcFileName);
        return stream;
    }

    /**
     * Generates a PDF document from a XSLT Formatted Object document,
     * <i>srcFileName</i>, using Apache Formatted Object Processeor.
     * 
     * @param srcFileName
     *            The XSLT-FO document to be rendered.
     * @return ByteArrayOutputStream representing the PDF results.
     * @throws SystemException
     */
    protected ByteArrayOutputStream generateXsltPdf(String srcFileName) throws SystemException {
        // Setup a buffer to obtain the content length
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        this.generateXsltPdf(srcFileName, pdfStream);
        return pdfStream;
    }

    /**
     * Generates a PDF document from a XSLT Formatted Object document using
     * Apache Formatted Object Processeor to a generic output stream
     * 
     * @param srcFileName
     *            the filename of the XSL-FO document acting as the source of
     *            the generated PDF.
     * @param pdfStream
     *            a generic output stream for direciting the generated results.
     * @throws SystemException
     */
    protected void generateXsltPdf(String srcFileName, OutputStream outStream) throws SystemException {

        try {
            // Set up input and output streams.
            // Note: Using BufferedOutputStream for performance reasons (helpful
            // with FileOutputStreams).
            File inFile = new File(srcFileName);

            // Setup input and output for XSLT transformation
            // Setup input stream
            Source source = new StreamSource(inFile);

            // Resulting SAX events (the generated FO) must be piped through to
            // FOP Construct fop with desired output format (reuse if you plan
            // to render multiple documents!)

            FopFactory fopFactory = FopFactory.newInstance();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, outStream);
            Result result = new SAXResult(fop.getDefaultHandler());

            // Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            // Start XSLT transformation and FOP processing
            transformer.transform(source, result);
            return;
        } catch (TransformerConfigurationException e) {
            this.msg = "TransformerConfigurationException: " + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } catch (TransformerException e) {
            this.msg = "TransformerException: " + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } catch (FOPException e) {
            this.msg = "FOPException: " + e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                this.msg = "IOException: " + e.getMessage();
                logger.log(Level.ERROR, this.msg);
                throw new SystemException(this.msg);
            }
        }
    }



    public void serialize(Object obj, String filePath) throws SystemException {
        XMLEncoder out = null;
        try {
            out = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
                    filePath)));
            out.writeObject(obj);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new SystemException(e);
        } finally {
            out.close();
        }

    }

    public Object deserialize(String filePath) throws SystemException {
        XMLDecoder in = null;
        try {
            in = new XMLDecoder(new BufferedInputStream(new FileInputStream(
                    filePath)));
            Object obj = in.readObject();
            return obj;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new SystemException(e);
        } finally {
            in.close();
        }
    }

    /**
     * Creates a XML instance in the form of an InputSource from a XML document
     * of type String.
     * 
     * @param xmlData
     *            XML document in String format.
     * @return InputSOurce representation of <i>xmlData</i>
     * @throws SystemException
     */
    public static InputSource createXmlDocument(String xmlData)
            throws SystemException {
        if (xmlData.length() <= 0) {
            throw new SystemException(
                    "The XML input source does not contain any content");
        }

        // Create an InputStream
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlData.getBytes());
        InputSource inputSource = new InputSource(bais);
        return inputSource;
    }

    /**
     * Creates a XML instance in the form of a org.w3c.dom.Document from a XML
     * document of type String.
     * 
     * @param xmlSource
     * @return
     * @throws SystemException
     */
    public static org.w3c.dom.Document toDocument(String xmlSource) throws SystemException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        String errMsg = null;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlSource)));
        } catch (ParserConfigurationException e) {
            errMsg = "Unable to convert XML String to Document instance due to a parser configuration problem for the XML DocumentBuilder interface : "
                    + e.getMessage();
            logger.error(errMsg);
            throw new SystemException(errMsg);
        } catch (SAXException e) {
            errMsg = "Unable to convert XML String to Document instance due to the existence of an error related to the actual parsing of the XML document:  "
                    + e.getMessage();
            logger.error(errMsg);
            throw new SystemException(errMsg);
        } catch (IOException e) {
            errMsg = "Unable to convert XML String to Document instance due to general IO error(s):  "
                    + e.getMessage();
            logger.error(errMsg);
            throw new SystemException(errMsg);
        }
    }

    /**
     * 
     * @param xml
     * @return
     * @throws TransformerException
     */
    public static String prettyPrint(String xml) throws TransformerException {
        org.w3c.dom.Document doc = toDocument(xml);
        return prettyPrint(doc, true, true);
    }

    /**
     * Converts a Document instance to a XML String.
     * 
     * @param node
     * @return
     */
    public static String documentToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    "yes");
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtains the name of the root element of the XML document. This will work
     * for qualified and unqualified elements.
     * 
     * @param xml
     *            XML document in String form.
     * @return the name of the element which could contain an assoicated
     *         namespace prefix.
     * @throws SystemException
     *             document parsing errors.
     */
    public static String getDocumentName(String xml) throws SystemException {
        org.w3c.dom.Document doc = RMT2XmlUtility.toDocument(xml);
        Element e = doc.getDocumentElement();
        String docName = e.getTagName();
        return docName;
    }

    /**
     * Applies the XPath expression from the query property to the InputSource
     * object that represents the XML data. The return type is of type
     * XPathConstants.NODESET which, by default, is a NodeList.
     * 
     * @param xpathExp
     *            The XPath query
     * @return NodeList The results of the XPath expression as one or more
     *         nodes.
     * @throws SystemException
     *             If xpathExp is null or invalid or if the expression could not
     *             be applied.
     */
    public static NodeList executeXpathQuery(String xpathExp, InputSource doc)
            throws SystemException {
        return (NodeList) RMT2XmlUtility.executeXpathQuery(xpathExp, doc,
                XPathConstants.NODESET);
    }

    /**
     * Applies the XPath expression from the query property to the InputSource
     * object that represents the XML data.
     * 
     * @param xpathExp
     *            The XPath query
     * @param doc
     *            XML document as an InputSource object.
     * @param qName
     * @return Object The results of the XPath expression which could be either
     *         a String or an Object.
     * @throws SystemException
     *             If xpathExp is null or invalid or if the expression could not
     *             be applied.
     */
    public static Object executeXpathQuery(String xpathExp, InputSource doc,
            QName qName) throws SystemException {
        if (xpathExp == null) {
            throw new SystemException("The XPath Expression is null or invalid");
        }
        Object result = null;
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        // xpath.setNamespaceContext(new NamespacePrefixResolver());

        try {
            result = xpath.evaluate(xpathExp, doc, qName);
            return result;
        } catch (XPathExpressionException e) {
            throw new SystemException("Error evaluating XPaht, " + xpathExp, e);
        }
    }

    /**
     * Converts a DOM Node to a XML String
     * 
     * @param node
     *            The node to convert
     * @return The String representing the Node
     */
    public static String transformNodeToString(Node node) {
        StringBuffer nodeString = new StringBuffer();

        if (node.getNodeName() != null) {
            if (!node.getNodeName().equals("#text")) {
                nodeString.append("<" + node.getNodeName() + ">");
            }
            // get your node value
            if (node.getNodeValue() != null) {
                nodeString.append(node.getTextContent());
            }

            // do i have a child node to handle?
            if (node.hasChildNodes()) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    nodeString.append(RMT2XmlUtility.transformNodeToString(node
                            .getChildNodes().item(i)));
                }
            }
            if (!node.getNodeName().equals("#text")) {
                nodeString.append("</" + node.getNodeName() + ">");
            }
        }
        return nodeString.toString();
    }

    /**
     * Retrieves the XML name space URI that is mapped to a specified name space
     * prefix.
     * 
     * @param uriPrefix
     *            the URI namespace prefix to search.
     * @return String the matching URI of <i>uriPrefix</i>
     * @throws NotFoundException
     *             the URI prefix was not found in designated XML namespace
     *             configuration file set as
     *             {@link com.api.xml.XmlApiFactory.NAMESPACES_RESOURCES
     *             NAMESPACES_RESOURCES}
     * @throws SystemException
     *             when <i>uriPrefix</i> is null or class cast error occurs due
     *             to the value retrieved from ResourceBundle that is mapped to
     *             <i>uriPrefix</i>.
     * @throws NotFoundException
     *             when <i>uriPrefix</i> is not included in the namespace
     *             resource bunlde or it is included but not mapped to any
     *             value.
     */
    public static String getXmlNamespaceUri(String uriPrefix)
            throws NotFoundException, SystemException {
        String errMsg = null;
        ResourceBundle rb = RMT2Utility.getXmlNamespaces();
        try {
            String uri = rb.getString(uriPrefix);
            if (uri == null) {
                throw new NotFoundException(
                        "XML namespace prefix, "
                                + uriPrefix
                                + ", was not found or not configured in the XML namespace configuration file, "
                                + GeneralConst.NAMESPACES_RESOURCES);
            }
            return uri;
        } catch (NullPointerException e) {
            errMsg = "Unable to determine XML element's namespace URI due to the input URI prefix is null";
            logger.error(errMsg);
            throw new SystemException(errMsg);
        } catch (MissingResourceException e) {
            errMsg = "Unable to determine XML element's namespace URI due to input URI prefix key ["
                    + uriPrefix
                    + "] is not included in resource bundle, "
                    + GeneralConst.NAMESPACES_RESOURCES + ".properties";
            logger.error(errMsg);
            throw new NotFoundException(errMsg);
        } catch (ClassCastException e) {
            errMsg = "Unable to determine XML element's namespace URI due to class cast exception:  "
                    + e.getMessage();
            logger.error(errMsg);
            throw new SystemException(errMsg);
        }
    }

    /**
     * 
     * @param source
     * @return
     * @throws SystemException
     */
    public static final Node getRootNode(Source source) throws SystemException {
        try {
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
                org.w3c.dom.Document doc = db.parse(is);
                root = (Node) doc.getDocumentElement();
            }
            else if (source instanceof SAXSource) {
                InputSource inSource = ((SAXSource) source).getInputSource();
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                org.w3c.dom.Document doc = db.parse(inSource);
                root = (Node) doc.getDocumentElement();
            }
            return root;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtain the value of a XML node belonging to a XML document.
     * <p>
     * The XML element is located using XPath as the method of query. The result
     * of the XPath expression should point to the parent XML node of
     * <i>elementName</i>. This method is purely for convenience and is not the
     * most efficient in terms of performing XML queries. It is not recommended
     * to invoke this method iteratively. It is best suited for one time type
     * queries.
     * 
     * @param elementName
     *            the name of the element to retreive value.
     * @param xmlQuery
     *            XPath expression which should target the parent node of
     *            <i>elementName</i>.
     * @param xml
     *            XML document to query.
     * @return A List of values for <i>elementName</i> or null when
     *         <i>elementName</i> is not found.
     * @throws DatabaseException
     * @throws SystemException
     * @deprecated use getElementValue(String elementName, String xml)
     */
    public static List<String> getElementValue(String elementName,
            String xmlQuery, String xml) {
        XmlDao dao = XmlApiFactory.createXmlDao(xml);
        try {
            dao.retrieve(xmlQuery);
            List<String> values = new ArrayList<String>();
            while (dao.nextRow()) {
                try {
                    String result = dao.getColumnValue(elementName);
                    values.add(result);
                } catch (NotFoundException e) {
                    logger.warn(
                            "XML element, " + elementName
                                    + ", was not found using XPath query,  "
                                    + xmlQuery, e);
                }
            }
            return values.size() == 0 ? null : values;
        } catch (DatabaseException e) {
            throw new DatabaseException(
                    "Error occurred performing XPath statement, " + xmlQuery, e);
        } finally {
            dao.close();
        }
    }

    /**
     * Obtain the value of a XML node belonging to a XML document.
     * 
     * @param elementName
     *            the name of the element to retreive value.
     * @param xml
     *            XML document to query.
     * @return The element's value as a String.
     * @throws SystemException
     */
    public static String getElementValue(String elementName, String xml) {
        org.w3c.dom.Document doc = null;
        doc = RMT2XmlUtility.toDocument(xml);

        // Get the target element element by tag name directly
        Node targetNode = doc.getElementsByTagName(elementName).item(0);
        String value = targetNode.getTextContent();
        return value;
    }

    /**
     * 
     * @param elementName
     * @param value
     * @param xml
     * @return
     */
    public static String setElementValue(String elementName, String value, String xml) {
        org.w3c.dom.Document doc = null;
        doc = RMT2XmlUtility.toDocument(xml);
        // Get the root element
        Node root = doc.getFirstChild();

        // Get the target element element by tag name directly
        Node targetNode = doc.getElementsByTagName(elementName).item(0);
        targetNode.setTextContent(value);

        // write the content to String
        RMT2XmlUtility util = RMT2XmlUtility.getInstance();
        String modifiedContent = util.transform(new DOMSource(doc));
        return modifiedContent;
    }

    /**
     * Prints a W3C XML document instance using JDOM api.
     * 
     * @param document
     * @return
     * @throws TransformerException
     */
    public static String prettyPrint(org.w3c.dom.Document document, boolean pretty, boolean omitDeclaration)
            throws SystemException {
        DOMBuilder builder = new DOMBuilder();
        Document jdomDoc = builder.build(document);

        // Output document
        // Document doc = new Document(root);
        Format format = null;
        if (pretty) {
            format = Format.getPrettyFormat();
            format.setIndent("   ");
        }
        else {
            format = Format.getCompactFormat();
        }
        format.setOmitDeclaration(omitDeclaration);
        XMLOutputter outputter = new XMLOutputter(format);
        String xml = outputter.outputString(jdomDoc);
        return xml;
    }

    /**
     * Creates a SAX InputSource instance using an instance of
     * org.w3c.dom.Document.
     * 
     * @param doc
     * @return
     * @throws SystemException
     */
    public static InputSource getSaxInputSource(org.w3c.dom.Document doc)
            throws SystemException {
        // THE JDOM way to obtain XML string from Document instance...
        String xml = RMT2XmlUtility.prettyPrint(doc, true, true);
        StringReader is = new StringReader(xml);
        InputSource insrc = new InputSource(is);
        return insrc;
    }

    /**
     * Will try to obtain SAX driver class name from several preconfigured
     * resource paths and using the first one it finds.
     * <p>
     * The resource path taken when seaching for the driver is:
     * <ol>
     * <li>The system property, <i>org.xml.sax.driver</i>, has a value.</li>
     * <li>The system property, <i>SAXDriver</i>.</li>
     * <li>The application's property pool contains the driver as
     * <i>SAXDriver</i>.</li>
     * </ol>
     * 
     * @return The name of the driver or null if the driver is not configured in
     *         the environment.
     * @throws SystemException
     */
    public static String getSaxDriver() throws SystemException {
        String driver = null;
        // Try loading from the System class as "org.xml.sax.driver" property
        // name which is the default driver supplied by the JDK.
        driver = System.getProperty("org.xml.sax.driver");

        // Try loading from the Application property pool
        if (driver == null) {
            driver = AppPropertyPool
                    .getProperty(ConfigConstants.PROPNAME_SAX_DRIVER);
        }

        // Try loading from the System class as "SAXDriver" property name which
        // the supplied driver is a SAX third party distrubution
        if (driver == null) {
            driver = System.getProperty(ConfigConstants.PROPNAME_SAX_DRIVER);
        }

        return driver;
    }

} // end class

