package com.nitish.employee_service.services;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.nitish.employee_service.model.Employee;
import com.nitish.employee_service.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;

public class EmployeeServices {
    private final EmployeeRepository employeeRepository;

    public EmployeeServices(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    @Transactional
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    public Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public Employee getEmployeePassword(String email) {
        return employeeRepository.findByEmail(email);
    }

}
