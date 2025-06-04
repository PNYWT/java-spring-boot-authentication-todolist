package com.authentication.authentication.controller;

import com.authentication.authentication.dto.BaseResponseDto;
import com.authentication.authentication.dto.TaskModelDto;
import com.authentication.authentication.exception.validation.RequestValidator;
import com.authentication.authentication.models.TaskModel;
import com.authentication.authentication.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /// getAllUserTask
    @GetMapping("/")
    // /api/v1/task/
    // /api/v1/task?task_name={task_name}
    public ResponseEntity<?> getAllUserTask(Authentication authentication,
                                            @RequestParam(value = "task_name", defaultValue = "", required = false) String task_name,
                                            HttpServletRequest request) {
        RequestValidator.validateAllowedParams(request, Set.of("task_name"));

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
    //  http://localhost:8081/api/v1/task/1
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskDetail(Authentication authentication,
                                           @PathVariable("id") Long task_id) {
        String usernameOrEmail = authentication.getName();
        log.debug("Path: = {}", task_id);
        log.debug("usernameOrEmail: = {}", usernameOrEmail);
        TaskModelDto taskDetail = taskService.findTaskModelDtoById(task_id, usernameOrEmail);
        BaseResponseDto<TaskModelDto> baseResponseDto = new BaseResponseDto<TaskModelDto>()
                .setBaseReposeData("101", "Success", taskDetail);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }

    /// getAllTaskByStatus
    // http://localhost:8081/api/v1/task/completed?completed=true
    @GetMapping("/completed")
    public ResponseEntity<?> getAllTaskByStatus(Authentication authentication,
                                                @RequestParam(value = "completed", required = false) Boolean state,
                                                HttpServletRequest request) {

        RequestValidator.validateAllowedParams(request, Set.of("completed"));

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
    // http://localhost:8081/api/v1/task/createTask + Body
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

    /// Update Task
    // http://localhost:8081/api/v1/task/editTask + Body
    @PostMapping("/editTask")
    public ResponseEntity<?> updateTask(Authentication authentication,
                                        @RequestBody TaskModelDto taskModelDto) {
        String usernameOrEmail = authentication.getName();
        TaskModelDto taskUpdate = taskService.updateTask(taskModelDto, usernameOrEmail);
        BaseResponseDto<TaskModelDto> baseResponseDto = new BaseResponseDto<TaskModelDto>()
                .setBaseReposeData("101", "Success", taskUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }

    /// Delete Task
    // http://localhost:8081/api/v1/task/deleteTask?task_id=24
    @PostMapping("/deleteTask")
    public ResponseEntity<?> updateTask(Authentication authentication,
                                        @RequestParam(value = "task_id", required = true) Long task_id) {
        String usernameOrEmail = authentication.getName();
        taskService.deleteTask(task_id, usernameOrEmail);
        BaseResponseDto baseResponseDto = new BaseResponseDto<TaskModelDto>()
                .setBaseResponse("101", String.format("Delete Task %d Success", task_id));
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDto);
    }
}
