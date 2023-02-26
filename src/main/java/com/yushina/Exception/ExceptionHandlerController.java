package com.yushina.Exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<String> handleException(EmployeeException ex) {
        return ResponseEntity.status(ex.getErrorCode()).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("Email") || ex.getMessage().contains("EMAIL")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please specify a unique email address for the employee");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
