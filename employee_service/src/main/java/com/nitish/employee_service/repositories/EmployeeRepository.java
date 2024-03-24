package com.nitish.employee_service.repositories;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.nitish.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

}
