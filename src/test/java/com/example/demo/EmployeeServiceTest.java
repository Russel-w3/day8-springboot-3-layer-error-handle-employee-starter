package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void should_return_employee_when_create_an_employee() {
        Employee employee = new Employee(18, "MALE", null, "Tom", 6000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(), employee.getAge());
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_greater_than_65_years_old_or_less_than_18() {
        Employee employee = new Employee(66, "MALE", null, "Tom", 20000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_create_an_employee_with_over_or_equals_30_with_salary_less_than_20000() {
        Employee employee = new Employee(30, "MALE", null, "Tom", 18000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidAgeAndSalaryEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_set_status_to_true_by_default_when_create_an_employee() {
        Employee employee = new Employee(18, "MALE", null, "Tom", 18000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(true, employeeResult.getStatus());
    }
}
