package org.dogepool.practicalrx.error;

import org.springframework.http.HttpStatus;

/**
 * An exception that the dogepool API can raise in case of a problem (see {@link ErrorCategory}).
 */
public class DogePoolException extends RuntimeException {

    public final HttpStatus httpStatus;

    public final ErrorCategory errorCategory;

    public final int errorCode;

    public DogePoolException(String message, Error error, HttpStatus httpStatus) {
        this(message, error.code, error.category, httpStatus, null);
    }

    public DogePoolException(String message, Error error, HttpStatus httpStatus, Throwable cause) {
        this(message, error.code, error.category, httpStatus, cause);
    }

    public DogePoolException(String message, int errorCode, ErrorCategory errorCategory, HttpStatus httpStatus,
            Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCategory = errorCategory;
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DogePoolException that = (DogePoolException) o;

        if (errorCode != that.errorCode) {
            return false;
        }
        if (httpStatus != that.httpStatus) {
            return false;
        }
        return errorCategory == that.errorCategory;

    }

    @Override
    public int hashCode() {
        int result = httpStatus.hashCode();
        result = 31 * result + errorCategory.hashCode();
        result = 31 * result + errorCode;
        return result;
    }
}
