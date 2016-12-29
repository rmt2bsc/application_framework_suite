package com.api.web.controller.scope;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.RMT2Base;
import com.api.web.Response;

/**
 * Provides a convenient wrapper implementation of the Response interface, hence
 * the ServletResponse.
 * 
 * @author RTerrell
 * 
 */
class HttpResponse extends RMT2Base implements Response {
    private ServletResponse response;

    private HttpServletResponse httpResponse;

    /**
     * Default constructor
     */
    private HttpResponse() {
        return;
    }

    /**
     * Constrcts a HttpResponse object by initializing it with a ServletResponse
     * instance.
     * 
     * @param response
     *            ServletResponse
     */
    public HttpResponse(ServletResponse response) {
        if (response == null) {
            return;
        }
        this.response = response;
        // Capture the HttpServletRequest instance
        if (this.response instanceof HttpServletResponse) {
            this.httpResponse = (HttpServletResponse) this.response;
        }
    }

    /**
     * Forces any content in the buffer to be written to the client. A call to
     * this method automatically commits the response, meaning the status code
     * and headers will be written.
     * 
     * @throws java.io.IOException
     */
    public void flushBuffer() throws IOException {
        if (this.response == null) {
            return;
        }
        this.response.flushBuffer();
    }

    /**
     * Returns the actual buffer size used for the ServletResponse. If no
     * buffering is used, this method returns 0.
     * 
     * @return the actual buffer size used or -1 if the response object is
     *         invalid
     */
    public int getBufferSize() {
        if (this.response == null) {
            return -1;
        }
        return this.response.getBufferSize();
    }

    /**
     * Returns the content type used for the body sent in this ServletResponse.
     * 
     * @return a String specifying the content type null if the response object
     *         is invalid
     */
    public String getContentType() {
        if (this.response == null) {
            return null;
        }
        return this.response.getContentType();
    }

    /**
     * Returns the actual ServletResponse object in which the implementation is
     * associated.
     * 
     * @return ServletResponse object.
     */
    public Object getNativeInstance() {
        return this.response;
    }

    /**
     * Returns a boolean indicating if the ServletResponse has been committed. A
     * committed ServletResponse has already had its status code and headers
     * written.
     * 
     * @return true when response is committed and false when otherwise.
     */
    public boolean isCommitted() {
        if (this.response == null) {
            return false;
        }
        return this.response.isCommitted();
    }

    /**
     * Clears the content of the underlying buffer in the ServletResponse
     * without clearing status code. If the response has been committed, this
     * method throws an IllegalStateException.
     * 
     * @throws IllegalStateException
     *             if this method is called after content has been written
     */
    public void resetBuffer() throws IllegalStateException {
        if (this.response == null) {
            return;
        }
        this.response.resetBuffer();
    }

    /**
     * Sets the preferred buffer size for the body of the ServletResponse. The
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
    public void setBufferSize(int size) throws IllegalStateException {
        if (this.response == null) {
            return;
        }
        this.response.setBufferSize(size);
    }

    /**
     * Sets the length of the content body in the ServletResponse.
     * 
     * @param len
     *            an integer specifying the length of the content being returned
     *            to the client; sets the Content-Length header
     */
    public void setContentLength(int len) {
        if (this.response == null) {
            return;
        }
        this.response.setContentLength(len);
    }

    /**
     * Sets the content type of the ServletResponse being sent to the client, if
     * the response has not been committed yet. The given content type may
     * include a character encoding specification, for example,
     * text/html;charset=UTF-8.
     * 
     * @param type
     *            a String specifying the MIME type of the content
     */
    public void setContentType(String type) {
        if (this.response == null) {
            return;
        }
        this.response.setContentType(type);
    }

    /**
     * Returns a ServletOutputStream suitable for writing binary data in the
     * response. The servlet container does not encode the binary data.
     * 
     * @return a ServletOutputStream for writing binary data
     * @throws IOException
     */
    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    /**
     * Returns a PrintWriter object that can send character text to the client.
     * 
     * @return a ServletOutputStream for writing binary data
     * @throws IOException
     */
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Response#addHeader(java.lang.String,
     * java.lang.String)
     */
    public void addHeader(String name, String value) {
        this.httpResponse.addHeader(name, value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Response#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name) {
        return this.httpResponse.containsHeader(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Response#sendError(int)
     */
    public void sendError(int sc) throws IOException {
        this.httpResponse.sendError(sc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Response#setHeader(java.lang.String,
     * java.lang.String)
     */
    public void setHeader(String name, String value) {
        this.httpResponse.setHeader(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Response#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value) {
        this.httpResponse.setIntHeader(name, value);
    }

}
