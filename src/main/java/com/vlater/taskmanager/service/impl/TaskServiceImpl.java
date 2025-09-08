package com.vlater.taskmanager.service.impl;

import com.vlater.taskmanager.dto.request.CreateTaskRequest;
import com.vlater.taskmanager.dto.request.Filters;
import com.vlater.taskmanager.dto.request.UpdateTaskRequest;
import com.vlater.taskmanager.dto.response.PagedResponse;
import com.vlater.taskmanager.dto.response.TaskResponse;
import com.vlater.taskmanager.exceptions.NoSuchTaskExistsException;
import com.vlater.taskmanager.model.Task;
import com.vlater.taskmanager.repository.TaskRepository;
import com.vlater.taskmanager.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public PagedResponse<TaskResponse> getTasks(Pageable pageable, Filters filters) {


        final Page<Task> taskPage = taskRepository.findWithFilters(
                filters.getTitle(),
                filters.isCompleted(),
                filters.getStartDate(),
                filters.getEndDate(),
                pageable
        );


        final List<TaskResponse> content = taskPage.getContent()
                .stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());

        return PagedResponse.createPagedResponse(content, taskPage);
    }

    @Override
    public TaskResponse getTask(int id) {

        try {
            final Optional<Task> task = taskRepository.findById(id);
            if (task.isPresent()) {
                return modelMapper.map(task.get(), TaskResponse.class);
            }
        } catch (NoSuchTaskExistsException e) {
            return null;
        }

        return null;
    }

    @Override
    public TaskResponse updateTask(int id, UpdateTaskRequest request) {
        Task task ;
        try {
            final Optional<Task> optionalTask = taskRepository.findById(id);
            if(optionalTask.isEmpty()) {
                return null;
            }

            task = optionalTask.get();

        } catch (NoSuchTaskExistsException e) {
            return null;
        }

        task.setCompleted(request.isCompleted());
        task.setDescription(request.getDescription());
        task.setTitle(request.getTitle());
        task.setDueDate(request.getDueDate());

        taskRepository.save(task);
        return modelMapper.map(task, TaskResponse.class);
    }

    @Override
    public void deleteTask(int id) {
        try {
            final Optional<Task> optionalTask = taskRepository.findById(id);
            if(optionalTask.isEmpty()) {
                return;
            }

            taskRepository.deleteById(optionalTask.get().getId());
        } catch (Exception e) {
            return;
        }

    }
}
