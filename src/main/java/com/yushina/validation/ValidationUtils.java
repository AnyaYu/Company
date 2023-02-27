package com.yushina.validation;

import com.yushina.Exception.EmployeeException;
import com.yushina.Exception.EmployeeValidationException;
import com.yushina.entities.Employee;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static void validateEmployee(Employee employee) {
        if (employee.getBirthday() == null) {
            throw new EmployeeValidationException("Please specify employee's birthday in the following format: yyyy-mm-dd");
        }
        if (employee.getEmail() == null || !isValidEmail(employee.getEmail())) {
            throw new EmployeeValidationException("Please specify a valid employee's email");
        }
        if (employee.getFullName() == null) {
            throw new EmployeeValidationException("Please specify employee's full name");
        }
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
}
