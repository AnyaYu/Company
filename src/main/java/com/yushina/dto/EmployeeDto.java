package com.yushina.dto;

import com.yushina.entities.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDto {

    private UUID id;

    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotNull
    private LocalDate birthday;

    private List<String> hobbies;

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setId(this.id);
        employee.setFullName(this.fullName);
        employee.setEmail(this.email);
        employee.setBirthday(this.getBirthday());
        employee.setHobbies(this.hobbies);

        return employee;
    }
}
