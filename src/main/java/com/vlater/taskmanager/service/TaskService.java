package com.vlater.taskmanager.service;

import com.vlater.taskmanager.dto.request.CreateTaskRequest;
import com.vlater.taskmanager.dto.request.Filters;
import com.vlater.taskmanager.dto.request.UpdateTaskRequest;
import com.vlater.taskmanager.dto.response.PagedResponse;
import com.vlater.taskmanager.dto.response.TaskResponse;
import com.vlater.taskmanager.exceptions.NoSuchTaskExistsException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);
    PagedResponse<TaskResponse> getTasks(Pageable pageable, Filters filters);
    TaskResponse getTask(int id) throws NoSuchTaskExistsException;
    TaskResponse updateTask(int id,  UpdateTaskRequest request);
    void deleteTask(int id);

}
