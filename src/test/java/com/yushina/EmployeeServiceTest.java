package com.yushina;

import com.yushina.Exception.EmployeeException;
import com.yushina.Exception.EmployeeValidationException;
import com.yushina.dto.EmployeeDto;
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

import static com.yushina.TestUtils.BIRTHDAY;
import static com.yushina.TestUtils.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);
        Employee employee = TestUtils.getNewEmployee(false, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);
        Employee savedEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(employeeDto);

        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(savedEmployee.getEmail(), result.getEmail());
        assertEquals(savedEmployee.getFullName(), result.getFullName());
        assertEquals(savedEmployee.getBirthday(), result.getBirthday());
        Assertions.assertLinesMatch(savedEmployee.getHobbies(), result.getHobbies());
    }

    @Test
    void createEmployeeTestWithIncorrectEmail() {
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, "incorrectEmail.com", TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeValidationException.class, () -> {
            employeeService.createEmployee(employeeDto);
        });
    }

    @Test
    void createEmployeeTestNullEmail() {
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, null, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeValidationException.class, () -> {
            employeeService.createEmployee(employeeDto);
        });
    }

    @Test
    void createEmployeeTestNullFullName() {
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, TestUtils.EMAIL, null,
                TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        assertThrows(EmployeeValidationException.class, () -> {
            employeeService.createEmployee(employeeDto);
        });
    }

    @Test
    void createEmployeeTestNullBirthday() {
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, TestUtils.EMAIL, TestUtils.FULL_NAME,
                null, TestUtils.HOBBIES);

        assertThrows(EmployeeValidationException.class, () -> {
            employeeService.createEmployee(employeeDto);
        });
    }

    @Test
    void createEmployeeTestNullHobbies() {
        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(false, TestUtils.EMAIL, TestUtils.FULL_NAME,
                BIRTHDAY, null);
        Employee savedEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME,
                TestUtils.BIRTHDAY, null);

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(employeeDto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void getEmployeeByIdEmployeeDoesNotExist() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(EmployeeException.class,
                () -> employeeService.getEmployeeById(ID));

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

        EmployeeDto employeeDto = TestUtils.getNewEmployeeDto(true, newEmail, newFullName, TestUtils.BIRTHDAY, TestUtils.HOBBIES);;
        Employee existingEmployee = TestUtils.getNewEmployee(true, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES);

        when(employeeRepository.findById(ID)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeDto.toEmployee());

        Employee updatedEmployee = employeeService.updateEmployee(ID, employeeDto);

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
                TestUtils.getNewEmployeeDto(false, TestUtils.EMAIL, TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES)));

        assertEquals(HttpStatus.NO_CONTENT, exception.getErrorCode());
    }

    @Test
    void testUpdateEmployeeWithInvalidEmail() {
        EmployeeValidationException exception = assertThrows(EmployeeValidationException.class, () -> employeeService.updateEmployee(ID,
                TestUtils.getNewEmployeeDto(false, "sdsd@com", TestUtils.FULL_NAME, TestUtils.BIRTHDAY, TestUtils.HOBBIES)));

        assertEquals("Please specify a valid employee's email", exception.getMessage());
    }

    @Test
    void deleteNonExistingEmployee() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(EmployeeException.class, () -> employeeService.deleteEmployee(ID));

        assertEquals(HttpStatus.NO_CONTENT, exception.getErrorCode());
    }

}
