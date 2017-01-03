package com.util;

import com.RMT2RuntimeException;

public class ZipFileException extends RMT2RuntimeException {
    private static final long serialVersionUID = 725395994669414338L;

    public ZipFileException() {
        super();
    }

    public ZipFileException(final String message) {
        super(message);
    }

    public ZipFileException(final Throwable cause) {
        super(cause);
    }

    public ZipFileException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
