package io.safetynet.alerts.api.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends AbstractDomainException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
