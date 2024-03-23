package com.nitish.api_gateway.model;

import com.nitish.api_gateway.model.structure.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private long id;
    private String title;
    private String description;
    private Status status;
    private  long assigneeId;
}
