package com.yushina.Exception;

public class EmployeeValidationException extends RuntimeException {
    public EmployeeValidationException(String message) {
        super(message);
    }
}
