package com.test.addressbook.exception.handlers;

import com.test.addressbook.model.exception.ExceptionDetails;
import com.test.addressbook.model.exception.InvalidCriteriaException;
import com.test.addressbook.model.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({InvalidCriteriaException.class})
    public ResponseEntity<ExceptionDetails> handleInvalidCriteriaException(Throwable throwable, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        String exCause = Optional.ofNullable(throwable.getCause()).map(Throwable::toString).
                orElse(throwable.getClass().getCanonicalName());
        exDetails.setCause(exCause);
        exDetails.setCode("INVALID_CRITERIA_SELECTED_EXCEPTION");
        exDetails.setMessage(throwable.getMessage());
        log.error("Business error occurred with message: {} and cause: {}", exCause, throwable.getMessage());

        return new ResponseEntity<>(exDetails,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({PersonNotFoundException.class})
    public ResponseEntity<ExceptionDetails> handlePersonNotFoundException(Throwable throwable, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        String exCause = Optional.ofNullable(throwable.getCause()).map(Throwable::toString).
                orElse(throwable.getClass().getCanonicalName());
        exDetails.setCause(exCause);
        exDetails.setCode("PERSON_NOT_FOUND_EXCEPTION");
        exDetails.setMessage(throwable.getMessage());
        log.error("Business error occurred with message: {} and cause: {}", exCause, throwable.getMessage());

        return new ResponseEntity<>(exDetails,HttpStatus.NOT_FOUND);
    }
}
