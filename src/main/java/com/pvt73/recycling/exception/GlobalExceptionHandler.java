package com.pvt73.recycling.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorMessage> handelConstraintValidationException(
            ConstraintViolationException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorMessage.builder()
                .status(BAD_REQUEST)
                .message(errors)
                .path(getPath(request))
                .entity();
    }


    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> handleStatusException(ResponseStatusException ex, WebRequest request) {
        logger.error(ex.getReason(), ex);
        return handleResponseStatusException(ex, request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage(), ex);
        return handleEveryException(ex, request);
    }

    @SuppressWarnings("unchecked")
    protected @Override
    ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                   HttpStatus status, WebRequest request) {

        ResponseEntity<?> responseEntity;

        if (!status.isError()) {
            responseEntity = handleStatusException(ex, status, request);
        } else if (INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
            responseEntity = handleEveryException(ex, request);
        } else {
            responseEntity = handleEveryException(ex, request);
        }

        return (ResponseEntity<Object>) responseEntity;
    }

    private ResponseEntity<ErrorMessage> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return ErrorMessage.builder()
                .exception(ex)
                .path(getPath(request))
                .entity();
    }

    private ResponseEntity<ErrorMessage> handleStatusException(Exception ex, HttpStatus status, WebRequest request) {
        return ErrorMessage.builder()
                .status(status)
                .message("Execution halted")
                .path(getPath(request))
                .entity();
    }

    private ResponseEntity<ErrorMessage> handleEveryException(Exception ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(INTERNAL_SERVER_ERROR)
                .message("Server encountered an error")
                .path(getPath(request))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ResponseEntity<?> responseEntity = methodArgumentNotValidException(ex, request);

        return (ResponseEntity<Object>) responseEntity;
    }


    private ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ErrorMessage.builder()
                .status(BAD_REQUEST)
                .message(errors)
                .path(getPath(request))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        ResponseEntity<?> responseEntity = missingServletRequestPartException(ex, request);

        return (ResponseEntity<Object>) responseEntity;
    }

    private ResponseEntity<ErrorMessage> missingServletRequestPartException(MissingServletRequestPartException ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(BAD_REQUEST)
                .message(ex.getMessage())
                .path(getPath(request))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        ResponseEntity<?> responseEntity = httpMediaTypeNotSupportedException(ex, request);

        return (ResponseEntity<Object>) responseEntity;
    }

    private ResponseEntity<ErrorMessage> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(UNSUPPORTED_MEDIA_TYPE)
                .message(ex.getMessage())
                .path(getPath(request))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ResponseEntity<?> responseEntity = httpMessageNotReadableException(ex, status, request);

        return (ResponseEntity<Object>) responseEntity;
    }

    private ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpStatus status, WebRequest request) {
        String[] linnes = ex.getMostSpecificCause().getMessage().split("\n");
        if (ex.contains(JsonParseException.class)) {
            return ErrorMessage.builder()
                    .status(BAD_REQUEST)
                    .message("Invalid json message received")
                    .path(getPath(request))
                    .entity();
        }
        return ErrorMessage.builder()
                .status(status)
                .message(linnes[0])
                .path(getPath(request))
                .entity();

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorMessage> handleConstraintViolationException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Wrong parameter type")
                .path(request.getDescription(false).substring(4))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<?> responseEntity = missingServletRequestParameterException(ex, request);

        return (ResponseEntity<Object>) responseEntity;
    }

    ResponseEntity<ErrorMessage> missingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .path(request.getDescription(false).substring(4))
                .entity();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<?> responseEntity = missingPathVariableException(ex, request);

        return (ResponseEntity<Object>) responseEntity;
    }

    ResponseEntity<ErrorMessage> missingPathVariableException(MissingPathVariableException ex, WebRequest request) {
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .path(request.getDescription(false).substring(4))
                .entity();
    }


    private String getPath(WebRequest request) {
        return request.getDescription(false).substring(4);
    }


}
