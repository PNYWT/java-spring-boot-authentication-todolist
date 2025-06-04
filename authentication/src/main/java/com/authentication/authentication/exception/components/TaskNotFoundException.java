package com.authentication.authentication.exception.components;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("Task not found with id: " + taskId);
    }

//    public TaskNotFoundException(String message) {
//        super(message);
//    }
}