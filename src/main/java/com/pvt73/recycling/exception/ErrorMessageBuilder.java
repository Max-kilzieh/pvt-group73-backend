package com.pvt73.recycling.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class ErrorMessageBuilder {

    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorMessageBuilder status(int status) {
        this.status = status;
        return this;
    }

    public ErrorMessageBuilder status(HttpStatus status) {
        this.status = status.value();

        if (status.isError()) {
            this.error = status.getReasonPhrase();
        }

        return this;
    }

    public ErrorMessageBuilder error(String error) {
        this.error = error;
        return this;
    }

    public ErrorMessageBuilder exception(ResponseStatusException exception) {
        HttpStatus status = exception.getStatus();
        this.status = status.value();

        if (!Objects.requireNonNull(exception.getReason()).isBlank()) {
            this.message = exception.getReason();
        }


        if (status.isError()) {
            this.error = status.getReasonPhrase();
        }

        return this;
    }

    public ErrorMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ErrorMessageBuilder message(Map<String, String> message) {
        StringJoiner joiner = new StringJoiner(", ");

        message.forEach((field, msg) ->
                joiner.add(field + " " + msg));

        this.message = joiner.toString();
        return this;
    }

    public ErrorMessageBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ErrorMessage build() {
        ErrorMessage response = new ErrorMessage();
        response.setStatus(status);
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);
        return response;
    }

    public ResponseEntity<ErrorMessage> entity() {
        return ResponseEntity.status(status).headers(HttpHeaders.EMPTY).body(build());
    }

    public String json() {
        return build().toJson();
    }
}