package com.Taskmanager.services.impl;

import com.Taskmanager.dto.TaskRequest;
import com.Taskmanager.dto.TaskResponse;
import com.Taskmanager.entities.Task;
import com.Taskmanager.entities.User;
import com.Taskmanager.exceptions.ResourceNotFoundException;
import com.Taskmanager.repository.TaskRepository;
import com.Taskmanager.services.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    //create obejct of taskrepo
    private final TaskRepository taskRepository;

    //construction
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskResponse createTask(TaskRequest taskRequest, User user) {
        //create new object of task
        Task task = new Task();

        task.setTitle(taskRequest.getTittle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(taskRequest.getDueDate());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return convertToResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getallTask(User user) {
        //get all task from the Database by every user
        //we are using jwt that's why use find by user
        List<Task> all = taskRepository.findByUser(user);

        //Here I am converting task entity to taskresponse
        return all.stream()
                .map(this::convertToResponse)
                .toList();
    }

    //find task by id
    @Override
    public TaskResponse getTaskById(Long id, User user) {

        //find task by id and logged in user
        Task task = taskRepository.findByIdAndUser(id, user)
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException("Task not found"));

        //convert entity to response
        return convertToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest, User user) {
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        task.setTitle(taskRequest.getTittle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
        task.setDueDate(taskRequest.getDueDate());

        Task updatedTask = taskRepository.save(task);

        return convertToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id, User user) {
        //first get user task which want to delete
        Task task = taskRepository.findByIdAndUser(id, user).
                orElseThrow(() ->
                        new ResourceNotFoundException(" Task not found"));

        //delete the task entity
        taskRepository.delete(task);
    }

    // Helper method
    private TaskResponse convertToResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getDueDate()
        );
    }
}
