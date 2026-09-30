package com.Taskmanager.controller;

import com.Taskmanager.dto.TaskRequest;
import com.Taskmanager.dto.TaskResponse;
import com.Taskmanager.entities.User;
import com.Taskmanager.exceptions.ResourceNotFoundException;
import com.Taskmanager.repository.UserRepository;
import com.Taskmanager.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    // Constructor Injection
    public TaskController(
            TaskService taskService,
            UserRepository userRepository) {

        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    // =========================
    // ADD TASK
    // =========================

    @PostMapping("/addTask")
    public ResponseEntity<TaskResponse> addTask(
            @RequestBody TaskRequest taskRequest,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        TaskResponse savedTask =
                taskService.createTask(taskRequest, user);

        return new ResponseEntity<>(
                savedTask,
                HttpStatus.CREATED
        );
    }

    // =========================
    // VIEW ALL TASKS
    // =========================

    @GetMapping("/viewAllTask")
    public ResponseEntity<List<TaskResponse>> getAllTask(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        List<TaskResponse> taskResponses =
                taskService.getallTask(user);

        return new ResponseEntity<>(
                taskResponses,
                HttpStatus.OK
        );
    }

    // =========================
    // GET TASK BY ID
    // =========================

    @GetMapping("/taskById/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        TaskResponse taskResponse =
                taskService.getTaskById(id, user);

        return new ResponseEntity<>(
                taskResponse,
                HttpStatus.OK
        );
    }

    // =========================
    // UPDATE TASK
    // =========================

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest taskRequest,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        TaskResponse updatedTask =
                taskService.updateTask(
                        id,
                        taskRequest,
                        user
                );

        return new ResponseEntity<>(
                updatedTask,
                HttpStatus.OK
        );
    }

    // =========================
    // DELETE TASK
    // =========================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        taskService.deleteTask(id, user);

        return new ResponseEntity<>(
                "Task deleted successfully",
                HttpStatus.OK
        );
    }
}