package com.example.demo;

import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    void should_return_employee_when_create_an_employee() {
        Employee employee = new Employee(18, "MALE", null, "Tom", 6000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeResponse employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(), employee.getAge());
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_greater_than_65_years_old_or_less_than_18() {
        Employee employee = new Employee(66, "MALE", null, "Tom", 20000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_create_an_employee_with_over_or_equals_30_with_salary_less_than_20000() {
        Employee employee = new Employee(30, "MALE", null, "Tom", 18000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidAgeAndSalaryEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_set_status_to_true_by_default_when_create_an_employee() {
        Employee employee = new Employee(18, "MALE", null, "Tom", 18000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeResponse employeeResult = employeeService.createEmployee(employee);
        assertEquals(true, employeeResult.getStatus());
    }

    @Test
    void should_throw_exception_when_update_an_no_active_employee() {
        Employee deletedEmployee = new Employee(18, "MALE", 1, "Tom", 18000.0);
        deletedEmployee.setStatus(false);
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(deletedEmployee));
        assertThrows(InvalidStatusEmployeeException.class, () -> employeeService.updateEmployee(1, deletedEmployee));
    }

    @Test
    void should_return_active_false_when_delete_employee() {
        Employee employee = new Employee(18, "MALE", 1, "Tom", 18000.0);
        assertTrue(employee.getStatus());
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);

        verify(employeeRepository)
                .save(argThat(e -> !e.getStatus()));
    }

}
