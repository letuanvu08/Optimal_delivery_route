package com.optimalDeliveryRoute.WebAPI.Exceptions;

public class APiExpiredJwtException extends RuntimeException{
    public APiExpiredJwtException() {
        super();
    }

    public APiExpiredJwtException(String message) {
        super(message);
    }

    public APiExpiredJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public APiExpiredJwtException(Throwable cause) {
        super(cause);
    }
}
