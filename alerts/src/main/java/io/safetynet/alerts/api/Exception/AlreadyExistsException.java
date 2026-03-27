package io.safetynet.alerts.api.Exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends AbstractDomainException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
