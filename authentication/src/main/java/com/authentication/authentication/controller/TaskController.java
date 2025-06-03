package com.authentication.authentication.controller;

import com.authentication.authentication.dto.BaseResponseDto;
import com.authentication.authentication.dto.TaskModelDto;
import com.authentication.authentication.models.TaskModel;
import com.authentication.authentication.services.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /// getAllUserTask
    @GetMapping("/")
    // /api/v1/task/
    // /api/v1/task?name={name}
    public ResponseEntity<?> getAllUserTask(Authentication authentication,
                                        @RequestParam(value = "task_name", defaultValue = "") String task_name) {
        String usernameOrEmail = authentication.getName();
        List<TaskModelDto> myAllTask = new ArrayList<>();
        if (task_name.isEmpty()) {
            myAllTask = taskService.getAllTaskUser(usernameOrEmail);
        } else {
            myAllTask = taskService.getTaskByTaskName(task_name, usernameOrEmail);
        }
        BaseResponseDto<List<TaskModelDto>> baseResponseDto = new BaseResponseDto<List<TaskModelDto>>()
                .setBaseReposeData("101", "Success", myAllTask);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }

    ///  getTaskDetail
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskDetail(Authentication authentication,
                                           @PathVariable("id") Long task_id) {
        String usernameOrEmail = authentication.getName();
        log.debug("Path: = {}", task_id);
        log.debug("usernameOrEmail: = {}", usernameOrEmail);
        Optional<TaskModelDto> taskDetail = taskService.findTaskById(task_id, usernameOrEmail);
        BaseResponseDto<Optional<TaskModelDto>> baseResponseDto = new BaseResponseDto<Optional<TaskModelDto>>()
                .setBaseReposeData("101", "Success", taskDetail);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }

    /// getAllTaskByStatus
    @GetMapping("/completed")
    public ResponseEntity<?> getAllTaskByStatus(Authentication authentication,
                                                @RequestParam(value = "completed", required = false) Boolean state) {
        String usernameOrEmail = authentication.getName();
        List<TaskModelDto> myAllTaskStatus = new ArrayList<>();
        if (state == null) {
            myAllTaskStatus = taskService.getAllTaskUser(usernameOrEmail);
        } else {
            myAllTaskStatus = taskService.findTaskByCompleted(state, usernameOrEmail);
        }
        BaseResponseDto<List<TaskModelDto>> baseResponseDto = new BaseResponseDto<List<TaskModelDto>>()
                .setBaseReposeData("101", "Success", myAllTaskStatus);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }

    /// createTask
    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(Authentication authentication,
                                        @Valid @RequestBody TaskModel taskmodel) {
        String usernameOrEmail = authentication.getName();
        TaskModelDto taskModelDto = taskService.createTask(
                taskmodel,
                usernameOrEmail // return authentication.getName() by CustomUserDetailsService
        );
        BaseResponseDto<TaskModelDto> baseResponseDto = new BaseResponseDto<TaskModelDto>()
                .setBaseReposeData("101", "Success", taskModelDto);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }
}
