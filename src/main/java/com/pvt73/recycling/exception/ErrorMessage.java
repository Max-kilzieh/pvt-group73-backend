package com.pvt73.recycling.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Setter
@Getter
public class ErrorMessage {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;

    public static ErrorMessageBuilder builder() {
        return new ErrorMessageBuilder();
    }


    public String toJson() {
        return new StringJoiner(", ", "{", "}")
                .add("\"timestamp\": \"" + timestamp + "\"")
                .add("\"status\": " + status)
                .add("\"error\": \"" + error + "\"")
                .add("\"message\": \"" + message + "\"")
                .add("\"path\": \"" + path + "\"")
                .toString();
    }
}