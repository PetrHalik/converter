package com.petrhalik.converter.exception;

public class ConversionException extends RuntimeException {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Exception ex) {
        super(message, ex);
    }

}
