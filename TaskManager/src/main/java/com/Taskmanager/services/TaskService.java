package com.Taskmanager.services;

import com.Taskmanager.dto.TaskRequest;
import com.Taskmanager.dto.TaskResponse;
import com.Taskmanager.entities.User;
import java.util.List;


public interface TaskService {

    //add task method
    public TaskResponse createTask(TaskRequest taskRequest, User user);

    //get all task method
    public List<TaskResponse> getallTask(User user);

    //get task by id
    public TaskResponse getTaskById(Long id,User user);

    //update task
    public TaskResponse updateTask(
            Long id,
            TaskRequest taskRequest,
            User user
    );


    //delete task
    void deleteTask(Long id,User User);
}
