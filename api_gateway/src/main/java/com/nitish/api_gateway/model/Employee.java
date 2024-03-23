package com.nitish.api_gateway.model;

import com.nitish.api_gateway.model.structure.Privilege;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
