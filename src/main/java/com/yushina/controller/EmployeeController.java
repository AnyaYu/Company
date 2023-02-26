package com.yushina.controller;

import com.yushina.entities.Employee;
import com.yushina.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees == null || employees.size() == 0) {
            new ResponseEntity<>("There are no employees in the data source", HttpStatus.OK);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") UUID id) {
        Employee employee = employeeService.getEmployeeById(id).orElse(null);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable("id") UUID id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee == null) {
            return new ResponseEntity<>(String.format("Employee with id = %s does not exist", id), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
        //TODO validate id
        Employee employee = employeeService.getEmployeeById(UUID.fromString(id)).orElse(null);

        if (employee == null) {
            return new ResponseEntity<>(String.format("Employee with id = %s does not exist", id), HttpStatus.NOT_FOUND);
        }
        employeeService.deleteEmployee(employee);
        return new ResponseEntity<>(String.format("Employee with id = %s is deleted", id), HttpStatus.NO_CONTENT);
    }
}
