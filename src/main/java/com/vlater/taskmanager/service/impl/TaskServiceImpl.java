package com.vlater.taskmanager.service.impl;

import com.vlater.taskmanager.dto.request.CreateTaskRequest;
import com.vlater.taskmanager.dto.request.UpdateTaskRequest;
import com.vlater.taskmanager.dto.response.TaskResponse;
import com.vlater.taskmanager.exceptions.NoSuchTaskExistsException;
import com.vlater.taskmanager.model.Task;
import com.vlater.taskmanager.repository.TaskRepository;
import com.vlater.taskmanager.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = modelMapper.map(request, Task.class);
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public List<TaskResponse> getTasks() {
        final List<TaskResponse> taskResponseList = new ArrayList<>();
        final List<Task> tasks = taskRepository.findAll();
        for(Task task : tasks) {
            final TaskResponse taskResponse = modelMapper.map(task, TaskResponse.class);
            taskResponseList.add(taskResponse);
        }
        return taskResponseList;
    }

    @Override
    public TaskResponse getTask(int id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskExistsException(id));

        return modelMapper.map(task, TaskResponse.class);
    }

    @Override
    public TaskResponse updateTask(int id, UpdateTaskRequest request) {

        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskExistsException(id));

        task.setCompleted(request.isCompleted());
        task.setDescription(request.getDescription());
        task.setTitle(request.getTitle());
        task.setDueDate(request.getDueDate());

        taskRepository.save(task);
        return modelMapper.map(task, TaskResponse.class);
    }

    @Override
    public void deleteTask(int id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskExistsException(id));

        taskRepository.deleteById(task.getId());
    }
}
