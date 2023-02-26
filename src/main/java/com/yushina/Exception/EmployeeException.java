package com.yushina.Exception;

import org.springframework.http.HttpStatus;

public class EmployeeException extends RuntimeException {

    private final HttpStatus errorCode;

    public EmployeeException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}