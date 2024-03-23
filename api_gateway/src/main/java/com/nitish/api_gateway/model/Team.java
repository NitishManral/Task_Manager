package com.nitish.api_gateway.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Team {
    private long id;
    private String name;
    private List<Employee> teamMembers;
    private Employee TeamLeader;
    
}
