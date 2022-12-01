package com.petrhalik.converter.exception;

public class NotSupportedException extends RuntimeException{

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Exception ex) {
        super(message, ex);
    }

}
