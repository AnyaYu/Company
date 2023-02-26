package com.yushina.service;

import com.yushina.entities.Employee;
import com.yushina.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private KafkaService kafkaService;

    public Employee createEmployee(Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        kafkaService.sendKafkaMessage("employee_created", employee);
        return savedEmployee;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(UUID id, Employee employee1) {
        Employee employee = getEmployeeById(id).orElse(null);
        if (employee == null) return null;
        employee.updateEmployee(employee1);
        Employee savedEmployee = employeeRepository.save(employee);
        kafkaService.sendKafkaMessage("employee_updated", savedEmployee);
        return savedEmployee;
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
        kafkaService.sendKafkaMessage("employee_deleted", employee);
    }
}
