package org.example.config;

import org.example.constant.ResponseConstant;
import org.example.dto.CommonException;
import org.example.dto.RestApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;
    // Handle specific exception
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleResourceNotFoundException(CommonException ex, WebRequest request) {
        log.error("Exception occurred {} for refId {}", ex.getMessage(), ex.getRefId());
            return new ResponseEntity<>(RestApiResponse.builder().responseCode(ex.getCode())
                .responseMessage(ex.getException()).refId(ex.getRefId()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(Exception ex, WebRequest request) {
        log.error("NotReadableException occurred {}", ex.getMessage());
        return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE)
                .responseMessage(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null)).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(Exception ex, WebRequest request) {
        log.error("NoResourceFoundException occurred {}", ex.getMessage());
        return new ResponseEntity<>(RestApiResponse.builder().responseCode(ResponseConstant.RESOURCE_NOT_FOUND_EXCEPTION_CODE)
                .responseMessage(messageSource.getMessage(ResponseConstant.RESOURCE_NOT_FOUND_EXCEPTION_CODE, null, null)).build(),
                HttpStatus.NOT_FOUND);
    }


    // Handle global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Error occurred while processing the request {}", ex.getMessage());
        return new ResponseEntity<>(RestApiResponse.builder().responseCode("500")
                .responseMessage(messageSource.getMessage(ResponseConstant.SERVER_GENERIC_EXCEPTION_CODE, null, null)).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
