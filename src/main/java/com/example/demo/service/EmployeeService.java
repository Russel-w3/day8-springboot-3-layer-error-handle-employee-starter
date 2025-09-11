package com.example.demo.service;

import com.example.demo.dto.EmployeeResponse;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(IEmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeMapper.toResponse(employeeRepository.findAll());
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findAll(pageable).stream().toList());
            }
        } else {
            if (page == null || size == null) {
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGenderOrderByAgeDesc(gender));
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGenderOrderByAgeDesc(gender, pageable));
            }
        }
    }

    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeMapper.toResponse(employee.get());
    }

    public EmployeeResponse createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("employee age is null!");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("employee age less than 18 or greater than 65!");
        }
        if (employee.getAge() >= 30 && employee.getSalary() <= 20000.0) {
            throw new InvalidAgeAndSalaryEmployeeException("employee age greater than or equal 30 and salary less than 20000!");
        }
        employee.setStatus(true);
        Employee save = employeeRepository.save(employee);
        return employeeMapper.toResponse(save);
    }

    public EmployeeResponse updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        Employee employeeToUpdate = employee.get();
        if (!employeeToUpdate.getStatus()) {
            throw new InvalidStatusEmployeeException("employee has left the company!");
        }
        updatedEmployee.setId(id);
        if (updatedEmployee.getName() != null) {
            employeeToUpdate.setName(updatedEmployee.getName());
        }
        if (updatedEmployee.getGender() != null) {
            employeeToUpdate.setGender(updatedEmployee.getGender());
        }
        if (updatedEmployee.getAge() != null) {
            employeeToUpdate.setAge(updatedEmployee.getAge());
        }
        if (updatedEmployee.getSalary() != null) {
            employeeToUpdate.setSalary(updatedEmployee.getSalary());
        }
        if (updatedEmployee.getStatus() != null) {
            employeeToUpdate.setStatus(updatedEmployee.getStatus());
        }
        return employeeMapper.toResponse(employeeRepository.save(employeeToUpdate));
    }

    public void deleteEmployee(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employee.get().setStatus(false);
        employeeRepository.save(employee.get());
    }
}
