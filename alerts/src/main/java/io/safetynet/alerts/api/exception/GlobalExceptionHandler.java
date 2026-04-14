package io.safetynet.alerts.api.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(AlreadyExistsException.class)
//    public ResponseEntity<String> handleAlreadyExist(AlreadyExistsException alreadyExistsException) {
//        log.error(alreadyExistsException.getMessage());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsException.getMessage());
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<String> handleNotFound(NotFoundException notFoundException) {
//        log.error(notFoundException.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
//    }

    @ExceptionHandler(AbstractDomainException.class)
    public ResponseEntity<?> handleAbstractDomainException(final AbstractDomainException domainException) {
        log.error(domainException.getMessage(), domainException);
        return ResponseEntity.status(domainException.getHttpStatus()).body(domainException.getMessage());
    }
}
