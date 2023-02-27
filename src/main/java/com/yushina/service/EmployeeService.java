package com.yushina.service;

import com.yushina.Exception.EmployeeException;
import com.yushina.dto.EmployeeDto;
import com.yushina.entities.Employee;
import com.yushina.repository.EmployeeRepository;
import com.yushina.validation.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private KafkaService kafkaService;

    private static final String EMPLOYEE_DOES_NOT_EXIST = "Employee with id %s does not exist";

    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeDto.toEmployee();
        ValidationUtils.validateEmployee(employee);
        Employee savedEmployee = employeeRepository.save(employee);
        kafkaService.sendKafkaMessage("employee_created", savedEmployee);
        log.info("Employee was created: " + savedEmployee);
        return savedEmployee;
    }

    public List<Employee> getAllEmployees() {
        log.info("Getting all employees");
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        log.info("Looking for employee with id: " + id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            String info = String.format(EMPLOYEE_DOES_NOT_EXIST, id);
            log.info(info);
            throw new EmployeeException(info, HttpStatus.NO_CONTENT);
        }
        log.info("Employee found: " + employee);
        return employee.get();
    }

    public Employee updateEmployee(UUID id, EmployeeDto employeeDto) {
        Employee employeeWithNewData = employeeDto.toEmployee();
        employeeWithNewData.setId(id);
        ValidationUtils.validateEmployee(employeeWithNewData);
        Employee employee = getEmployeeById(id);
        if (!employee.equals(employeeWithNewData)) {
            employee.updateEmployee(employeeWithNewData);
            employeeRepository.save(employee);
            kafkaService.sendKafkaMessage("employee_updated", employee);
            log.info("Employee was updated: " + employee);
        }
        return employee;
    }

    public void deleteEmployee(UUID id) {
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            employeeRepository.delete(employee);
            kafkaService.sendKafkaMessage("employee_deleted", employee);
            log.info("Employee was deleted: " + employee);
        }
    }
}
