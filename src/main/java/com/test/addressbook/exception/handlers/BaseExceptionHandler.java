package com.test.addressbook.exception.handlers;

import com.test.addressbook.model.exception.ExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionDetails> handleBaseException(Throwable throwable, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        String exCause = Optional.ofNullable(throwable.getCause()).map(Throwable::toString).
                orElse("Unexpected error occurred");
        exDetails.setCause("Unexpected error occurred");
        exDetails.setCode("TECHNICAL_ERROR");
        exDetails.setMessage("Internal Server Error");
        log.error("Unexpected server-side error occurred with message: {} and cause: {}", throwable.getMessage(), exCause);
        return new ResponseEntity<>(exDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
