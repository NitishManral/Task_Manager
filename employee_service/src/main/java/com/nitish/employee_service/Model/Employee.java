package com.nitish.employee_service.Model;

import com.nitish.employee_service.Model.structure.Privilege;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    private Long id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Privilege privilege;
    private String password;

    // getters and setters
}
