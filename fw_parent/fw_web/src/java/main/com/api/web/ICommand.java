package com.api.web;


/**
 * This interface describes what a command can do. It only defines one method,
 * processRequest, that performs the bulk of the job.
 */
public interface ICommand {
    /**
     * Processes the requested command, each implementor decides what to do
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Comand issued by the client.
     * @Throws SystemException when an error needs to be reported.
     */
    void processRequest(Request request, Response response, String command)
            throws ActionCommandException;

    /**
     * Disposes resources associated with this action handler
     * 
     * @throws ActionHandlerException
     */
    void close() throws ActionCommandException;
}
