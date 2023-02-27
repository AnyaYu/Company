package com.yushina;

import com.yushina.entities.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static final String EMAIL = "test@example.com";
    public static final String FULL_NAME = "Test User";
    public static final LocalDate BIRTHDAY = LocalDate.parse("2000-01-01");
    public static final List<String> HOBBIES = List.of("Soccer", "Music");
    public static final UUID ID = UUID.fromString("d6be343f-12a1-461a-8dfb-828dcb7189e1");

    public static Employee getNewEmployee(boolean withId, String email, String fullName, LocalDate birthday, List<String> hobbies) {
        Employee employee = new Employee();
        if (withId) employee.setId(ID);
        employee.setEmail(email);
        employee.setFullName(fullName);
        employee.setBirthday(birthday);
        employee.setHobbies(hobbies);
        return employee;
    }
}
