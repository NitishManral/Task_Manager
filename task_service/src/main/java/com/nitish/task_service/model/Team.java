package com.nitish.task_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Team")
public class Team {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "teamLeaderId")
    private Employee teamLeader;
    @ManyToMany
    @JoinTable(
            name = "TeamMembers",
            joinColumns = @JoinColumn(name = "teamId"),
            inverseJoinColumns = @JoinColumn(name = "employeeId")
    )
    private Set<Employee> teamMembers;

    // getters and setters
}
