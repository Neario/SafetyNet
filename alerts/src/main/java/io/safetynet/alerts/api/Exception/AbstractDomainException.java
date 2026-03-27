package io.safetynet.alerts.api.Exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractDomainException extends RuntimeException {

    protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    protected AbstractDomainException(String message) {
        super(message);
    }

    protected AbstractDomainException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
