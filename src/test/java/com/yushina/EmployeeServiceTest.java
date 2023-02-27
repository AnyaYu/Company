package com.yushina;

import com.yushina.Exception.EmployeeException;
import com.yushina.entities.Employee;
import com.yushina.repository.EmployeeRepository;
import com.yushina.service.EmployeeService;
import com.yushina.service.KafkaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.yushina.TestUtils.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void createEmployeeTest() {
        Employee employee = TestUtils.getNewEmployee(false, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);
        Employee savedEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        when(employeeRepository.save(employee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(employee);

        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(savedEmployee.getEmail(), result.getEmail());
        assertEquals(savedEmployee.getFullName(), result.getFullName());
        assertEquals(savedEmployee.getBirthday(), result.getBirthday());
        Assertions.assertLinesMatch(savedEmployee.getHobbies(), result.getHobbies());
    }

    @Test
    void createEmployeeTestWithIncorrectEmail() {
        Employee employee = TestUtils.getNewEmployee(false, "incorrectEmail.com", TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void createEmployeeTestNullEmail() {
        Employee employee = TestUtils.getNewEmployee(false, null, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void createEmployeeTestNullFullName() {
        Employee employee = TestUtils.getNewEmployee(false, TestUtils.EMAIL, null,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void createEmployeeTestNullBirthday() {
        Employee employee = TestUtils.getNewEmployee(false, TestUtils.EMAIL, TestUtils.FULL_NAME,
                null, TestUtils.HOBBIES);

        assertThrows(EmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void createEmployeeTestNullHobbies() {
        Employee employee = TestUtils.getNewEmployee(false, TestUtils.EMAIL, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, null);
        Employee savedEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, null);

        when(employeeRepository.save(employee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(employee);

        Assertions.assertNotNull(result);
    }

    @Test
    public void getEmployeeByIdEmployeeDoesNotExist() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(EmployeeException.class,
                () -> employeeService.getEmployeeById(ID));

        assertEquals("Employee with id = " + ID + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.NO_CONTENT, exception.getErrorCode());
    }

    @Test
    public void getEmployeeById() {
        Employee employee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        when(employeeRepository.findById(ID)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(ID);

        Assertions.assertNotNull(result);
        assertEquals(employee, result);
    }

    @Test
    void testUpdateEmployee() {
        final String newEmail = "newEmail@gmail.com";
        final String newFullName = "Updated";

        Employee existingEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);
        Employee employeeToUpdate = TestUtils.getNewEmployee(true, newEmail, newFullName, TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        when(employeeRepository.findById(ID)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Employee updatedEmployee = employeeService.updateEmployee(ID, employeeToUpdate);

        assertEquals(ID, updatedEmployee.getId());
        assertEquals(newFullName, updatedEmployee.getFullName());
        assertEquals(newEmail, updatedEmployee.getEmail());
        assertEquals(TestUtils.BIRTHDAY, updatedEmployee.getBirthday());
        Assertions.assertLinesMatch(TestUtils.HOBBIES, updatedEmployee.getHobbies());
    }

    @Test
    void testUpdateNonExistentEmployee() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(EmployeeException.class, () -> employeeService.updateEmployee(ID,
                TestUtils.getNewEmployee(false, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES)));

        assertEquals("Employee with id = " + ID + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.NO_CONTENT, exception.getErrorCode());
    }

    @Test
    void testUpdateEmployeeWithInvalidEmail() {
        EmployeeException exception = assertThrows(EmployeeException.class, () -> employeeService.updateEmployee(ID,
                TestUtils.getNewEmployee(false, "sdsd@com", TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES)));

        assertEquals("Please specify a valid employee's email", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void deleteNonExistingEmployee() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(EmployeeException.class, () -> employeeService.deleteEmployee(ID));

        assertEquals("Employee with id = " + ID + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.NO_CONTENT, exception.getErrorCode());
    }

}
