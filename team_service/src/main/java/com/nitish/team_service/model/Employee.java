package com.nitish.team_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.nitish.team_service.model.structure.Privilege;

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
