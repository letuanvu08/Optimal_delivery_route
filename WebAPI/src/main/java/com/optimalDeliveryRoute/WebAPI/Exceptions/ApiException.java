package com.optimalDeliveryRoute.WebAPI.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
     private final String message;
     private final Throwable thrownable;
     private final HttpStatus httpStatus;
     private final ZonedDateTime timestamp;

    public String getMessage() {
        return message;
    }

    public Throwable getThrownable() {
        return thrownable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ApiException(String message, Throwable thrownable, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.thrownable = thrownable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
