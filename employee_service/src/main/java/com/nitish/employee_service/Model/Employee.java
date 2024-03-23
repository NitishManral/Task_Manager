package com.nitish.employee_service.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.nitish.employee_service.Model.structure.Privilege;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Employee {
    private long id;
    private String name;
    private String email;
    private Privilege privilege;
    private String password;
}
