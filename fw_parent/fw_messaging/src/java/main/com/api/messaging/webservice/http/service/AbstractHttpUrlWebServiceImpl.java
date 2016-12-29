package com.api.messaging.webservice.http.service;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
//import com.api.jsp.action.AbstractActionHandler;
//import com.api.jsp.action.ActionHandlerException;
//import com.api.jsp.action.ICommand;

/**
 * This abstract class serves as a producer of remote services and provides the
 * template needed to process a client's service request over the Http protocol
 * where the data is package as query string name/value pairs.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractHttpUrlWebServiceImpl extends
        AbstractWsHttpActionHandler implements WsHttpCommand {
    private static Logger logger = Logger
            .getLogger("AbstractHttpUrlWebServiceImpl");

    /**
     * Input variable representing the URL query string parameters
     */
    protected Properties inProp;

    /**
     * Input variable representing an arbitrary data object sent by the client
     */
    protected Object inObjData;

    /**
     * An arbitrary object used as the results of the web service
     */
    protected Object outData;

    /** This is the action name triggered by the client */
    protected String command;

    /**
     * This is the content type used to identify data transmitted between client
     * and server
     */
    protected String content;

    /** A reference to the client */
    protected Object gui;

    /** Stream used to send data back to the client as a response. */
    protected ObjectOutputStream os;

    /** Strean used to obtain data from the client. */
    protected ObjectInputStream is;

    /**
     * Default Constructor
     * 
     * @throws SystemException
     */
    public AbstractHttpUrlWebServiceImpl() throws SystemException {
        return;
    }

    /**
     * Contructs a AbstractHttpUrlWebServiceImpl object by initializing the
     * servlet context and the user's request.
     * 
     * @param _context
     *            The servlet context to be associated with this action handler
     * @param _request
     *            The request object sent by the client to be associated with
     *            this action handler
     * @throws SystemException
     */
    public AbstractHttpUrlWebServiceImpl(Context _context, Request _request)
            throws SystemException, DatabaseException {
        super(_context, _request);
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is inistantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request)
            throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's request using request, response, and command.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Comand issued by the client.
     * @throws SystemException
     *             when an error needs to be reported.
     */
    public void processRequest(Request request, Response response,
            String command) throws WsHttpActionHandlerException {
        try {
            this.init(null, request);
            this.init();
            this.command = command;
            this.response = response;
        } catch (SystemException e) {
            throw new WsHttpActionHandlerException(e);
        }

        // Perform template
        this.receiveClientData();
        this.processData();
        this.sendClientData();
    }

    /**
     * Drives the process of servicing the client's request.
     * 
     * @throws ActionHandlerException
     */
    public abstract void processData() throws WsHttpActionHandlerException;

    /**
     * Accepts a data object coming from the client via the Http protocol, if
     * available. If the object is not available or is null, then the
     * implementation may require the input data to be obtained from the request
     * using repetitive calls to getParameter(). Override to customize input
     * funtionality when needed.
     * 
     * @throws AbstractActionHandler
     */
    public void receiveClientData() throws WsHttpActionHandlerException {
        // Try to obtain URL encoded query string data
        this.inProp = RMT2WebUtility.getRequestData(this.request, this.inProp);

        // Attempt to retrieve object data, if available.
        try {
            this.is = new ObjectInputStream(
                    ((ServletRequest) this.request.getNativeInstance())
                            .getInputStream());
            this.inObjData = this.is.readObject();
            return;
        } catch (EOFException e) {
            // There is a possibility that we are dealing with
            // post data and not an object output stream.
            return;
        } catch (IOException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new WsHttpActionHandlerException(this.msg);
        } catch (ClassNotFoundException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new WsHttpActionHandlerException(this.msg);
        }
    }

    /**
     * Send data back to the Http client as an Object.
     * 
     * @param data
     *            The data object.
     * @throws ActionHandlerException
     */
    public void sendClientData() throws WsHttpActionHandlerException {
        // Set MIME type for objects encoded with an ObjectOutputStream.
        this.content = "application/x-java-serialized-object";
        this.response.setContentType(this.content);
        try {
            this.os = new ObjectOutputStream(
                    ((ServletResponse) this.response.getNativeInstance())
                            .getOutputStream());
            this.os.writeObject(this.outData);
            this.os.flush();
            this.os.close();
            return;
        } catch (IOException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new WsHttpActionHandlerException(this.msg);
        }
    }

    // /**
    // * No Action
    // */
    // public void edit() throws ActionHandlerException {
    // return;
    // }
    //
    // /**
    // * No Action
    // */
    // public void add() throws ActionHandlerException {
    // return;
    // }
    //
    // /**
    // * No Action
    // */
    // public void save() throws ActionHandlerException {
    // return;
    // }
    //
    // /**
    // * No Action
    // */
    // public void delete() throws ActionHandlerException {
    // return;
    // }

}