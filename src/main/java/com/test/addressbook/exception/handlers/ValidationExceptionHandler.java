package com.test.addressbook.exception.handlers;

import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import com.atlassian.oai.validator.springmvc.InvalidResponseException;
import com.test.addressbook.model.exception.ExceptionDetails;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionDetails> handleInValidRequestException(InvalidRequestException ex, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        if (Optional.ofNullable(ex.getCause()).isPresent()) {
            Optional.of(ex.getCause().toString()).ifPresent(exDetails::setCause);
        } else {
            exDetails.setCause("Bad Request");
        }
        exDetails.setCode("VALIDATION_ERROR");
        exDetails.setMessage(ex.getValidationReport().getMessages().get(0).toString());
        return new ResponseEntity<>(exDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidResponseException.class)
    public ResponseEntity<ExceptionDetails> handleInValidResponseException(InvalidResponseException ex, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        if (Optional.ofNullable(ex.getCause()).isPresent()) {
            Optional.of(ex.getCause().toString()).ifPresent(exDetails::setCause);
        } else {
            exDetails.setCause("Bad Request");
        }
        exDetails.setCode("VALIDATION_ERROR");
        exDetails.setMessage(ex.getValidationReport().getMessages().get(0).toString());
        return new ResponseEntity<>(exDetails,HttpStatus.BAD_REQUEST);
    }
}
