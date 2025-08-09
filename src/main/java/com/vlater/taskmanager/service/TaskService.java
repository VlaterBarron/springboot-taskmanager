package com.vlater.taskmanager.service;

import com.vlater.taskmanager.dto.request.CreateTaskRequest;
import com.vlater.taskmanager.dto.request.UpdateTaskRequest;
import com.vlater.taskmanager.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);
    List<TaskResponse> getTasks();
    TaskResponse getTask(int id);
    TaskResponse updateTask(int id,  UpdateTaskRequest request);
    void deleteTask(int id);

}
