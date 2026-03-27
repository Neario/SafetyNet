package io.safetynet.alerts.api.Exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractDomainException {


    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
