package com.test.addressbook.exception.handlers;

import com.test.addressbook.model.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionDetails> handleBaseException(Throwable throwable, HttpServletRequest httpServletRequest) {

        ExceptionDetails exDetails = new ExceptionDetails();
        if(Optional.ofNullable(throwable.getCause()).isPresent()){
            exDetails.setCause(throwable.getCause().toString());
        }else{
            exDetails.setCause(throwable.getClass().getCanonicalName());
        }
        exDetails.setCode("TECHNICAL_ERROR");
        exDetails.setMessage(throwable.getMessage());
        return new ResponseEntity<>(exDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
