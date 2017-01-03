package com.api.web;

import java.io.IOException;

/**
 * Defines an object to assist the server side of an application in sending a
 * response to the client.
 * 
 * @author RTerrell
 * 
 */
public interface Response {

    /**
     * Forces any content in the buffer to be written to the client. A call to
     * this method automatically commits the response, meaning the status code
     * and headers will be written.
     * 
     * @throws java.io.IOException
     */
    void flushBuffer() throws java.io.IOException;

    /**
     * Returns the actual buffer size used for the response. If no buffering is
     * used, this method returns 0.
     * 
     * @return the actual buffer size used
     */
    int getBufferSize();

    /**
     * Returns the content type used for the body sent in this response.
     * 
     * @return a String specifying the content type
     */
    String getContentType();

    /**
     * Returns a boolean indicating if the response has been committed. A
     * committed response has already had its status code and headers written.
     * 
     * @return a boolean indicating if the response has been committed.
     */
    boolean isCommitted();

    /**
     * Clears the content of the underlying buffer in the response without
     * clearing status code. If the response has been committed, this method
     * throws an IllegalStateException.
     * 
     * @throws IllegalStateException
     *             if this method is called after content has been written
     */
    void resetBuffer() throws IllegalStateException;

    /**
     * Sets the preferred buffer size for the body of the response. The
     * application will use a buffer at least as large as the size requested.
     * The actual buffer size used can be found using getBufferSize.
     * <p>
     * A larger buffer allows more content to be written before anything is
     * actually sent, thus providing the application with more time to set
     * appropriate status codes. A smaller buffer decreases server memory load
     * and allows the client to start receiving data more quickly.
     * <p>
     * This method must be called before any response body content is written;
     * if content has been written or the response object has been committed,
     * this method throws an IllegalStateException.
     * 
     * @param size
     *            the preferred buffer size
     * @throws IllegalStateException
     *             if this method is called after content has been written
     */
    void setBufferSize(int size) throws IllegalStateException;

    /**
     * Sets the length of the content body in the response.
     * 
     * @param len
     *            an integer specifying the length of the content being returned
     *            to the client; sets the Content-Length header
     */
    void setContentLength(int len);

    /**
     * Sets the content type of the response being sent to the client, if the
     * response has not been committed yet. The given content type may include a
     * character encoding specification, for example, text/html;charset=UTF-8.
     * 
     * @param type
     *            a String specifying the MIME type of the content
     */
    void setContentType(String type);

    /**
     * Returns the actual object in which the implementation is built around.
     * 
     * @return An arbirary object.
     */
    Object getNativeInstance();

    /**
     * Returns an output steam suitable for the specific implementation.
     * 
     * @return An arbitrary object.
     * @throws IOException
     */
    Object getOutputStream() throws IOException;

    /**
     * Returns a Writer object that can send character text to the client.
     * 
     * @return An arbitrary object.
     * @throws IOException
     */
    Object getWriter() throws IOException;

    /**
     * Sets a response header with the given name and value. If the header had
     * already been set, the new value overwrites the previous one.
     * 
     * @param name
     *            the name of the header
     * @param value
     *            the header value If it contains octet string, it should be
     *            encoded according to RFC 2047
     *            (http://www.ietf.org/rfc/rfc2047.txt)
     */
    void setHeader(String name, String value);

    /**
     * Adds a response header with the given name and value. This method allows
     * response headers to have multiple values.
     * 
     * @param name
     *            the name of the header
     * @param value
     *            the additional header value If it contains octet string, it
     *            should be encoded according to RFC 2047
     *            (http://www.ietf.org/rfc/rfc2047.txt)
     */
    void addHeader(String name, String value);

    /**
     * Sets a response header with the given name and integer value. If the
     * header had already been set, the new value overwrites the previous one.
     * The containsHeader method can be used to test for the presence of a
     * header before setting its value.
     * 
     * @param name
     *            the name of the header
     * @param value
     *            the assigned integer value
     */
    void setIntHeader(String name, int value);

    /**
     * Returns a boolean indicating whether the named response header has
     * already been set.
     * 
     * @param name
     *            the header name
     * @return true if the named response header has already been set; false
     *         otherwise
     */
    boolean containsHeader(String name);

    /**
     * Sends an error response to the client using the specified status code and
     * clearing the buffer.
     * <p>
     * If the response has already been committed, this method throws an
     * IllegalStateException. After using this method, the response should be
     * considered to be committed and should not be written to.
     * 
     * @param sc
     *            the error status code
     * @throws IOException
     *             If an input or output exception occurs
     */
    void sendError(int sc) throws IOException;
}
