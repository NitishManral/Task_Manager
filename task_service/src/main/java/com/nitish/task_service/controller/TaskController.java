package com.nitish.task_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nitish.task_service.model.Task;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TaskController {
    List<Task> taskList = new ArrayList<>();

    @GetMapping("/task/live")
    public ResponseEntity<CustomResponseEntity> live() {

        return new ResponseEntity<>(new CustomResponseEntity(200, "Server is live", null), HttpStatus.OK);
    }

    @GetMapping("/task")
    public ResponseEntity<CustomResponseEntity> getTasks() {
        // TODO: return list of tasks based on user privilege
        return new ResponseEntity<>(new CustomResponseEntity(200, "Tasks", taskList), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<CustomResponseEntity> getTaskById(@PathVariable long id) {
        Task task = taskList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
        return task != null ? new ResponseEntity<>(new CustomResponseEntity(200,"",task),HttpStatus.OK) : new ResponseEntity<>(new CustomResponseEntity(404,"Task not found",null),HttpStatus.NOT_FOUND);
    }
    //TODO add custome response to everything
    @PostMapping("/task")
    public ResponseEntity<CustomResponseEntity> getTask(@RequestBody Task task){
        // TODO: create task and return task id
        task.setId(taskList.size() + 1);
        taskList.add(task);
        System.out.println(task.toString());
        return new ResponseEntity<>(new CustomResponseEntity(200,"Task created",task),HttpStatus.OK);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<CustomResponseEntity> updateTask(@PathVariable long id,@RequestBody Task updatedTask) {
        Task task = taskList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (task != null) {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setAssigneeId(updatedTask.getAssigneeId());
            return new ResponseEntity<>(new CustomResponseEntity(200,"Task updated",taskList),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponseEntity(404,"Task not found",null),HttpStatus.NOT_FOUND);
        }
    }

}
