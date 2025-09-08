package com.vlater.taskmanager;

import com.vlater.taskmanager.dto.request.CreateTaskRequest;
import com.vlater.taskmanager.dto.request.Filters;
import com.vlater.taskmanager.dto.request.UpdateTaskRequest;
import com.vlater.taskmanager.dto.response.PagedResponse;
import com.vlater.taskmanager.dto.response.TaskResponse;
import com.vlater.taskmanager.exceptions.NoSuchTaskExistsException;
import com.vlater.taskmanager.model.Task;
import com.vlater.taskmanager.repository.TaskRepository;
import com.vlater.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TmApplicationTests {

    private final static String ORDER_BY = "id";
    private final static String DIRECTION = "asc";
    private final static int PAGE = 0;
    private final static int SIZE = 10;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

   @BeforeEach
   void setUp() {}

    private Page<Task> generateTasksPage() {
        final Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");
        task1.setDescription("Task Description 1");
        task1.setDueDate(LocalDate.now());
        task1.setCompleted(false);
        final Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task 2");
        task2.setDescription("Task Description 2");
        task2.setDueDate(LocalDate.of(2025, 9, 12));
        task2.setCompleted(false);
        final List<Task> tasks = List.of(task1, task2);
        return new PageImpl<>(tasks);
    }

    @Test
    void getTasksWithNoFiltersNoContent() {
        Page<Task> emptyPage = new PageImpl<>(List.of());
        Mockito.when(taskRepository.findWithFilters(
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)
        )).thenReturn(emptyPage);

        final Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(ORDER_BY));
        final Filters filters = new Filters(null, null, null, null);

        final PagedResponse<TaskResponse> res = taskService.getTasks(pageable,filters );

        Assertions.assertNotNull(res);
        Assertions.assertEquals(0, res.getList().size());
        Assertions.assertEquals(Collections.emptyList(), res.getList());
    }

    @Test
    void getTasksWithNoFiltersWContent(){
        final Page<Task> mockedPage = generateTasksPage();
        final List<Task> mockedTasks = mockedPage.getContent();

        Mockito.when(taskRepository.findWithFilters(
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.any(Pageable.class)
        )).thenReturn(mockedPage);

        final TaskResponse response1 = new TaskResponse();
        response1.setId(1);
        response1.setTitle("Task 1");

        final TaskResponse response2 = new TaskResponse();
        response2.setId(2);
        response2.setTitle("Task 2");

        Mockito.when(modelMapper.map(mockedTasks.get(0), TaskResponse.class)).thenReturn(response1);
        Mockito.when(modelMapper.map(mockedTasks.get(1), TaskResponse.class)).thenReturn(response2);

        final Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.DESC, ORDER_BY));
        final Filters filters = new Filters(null, null, null, null);
        final PagedResponse<TaskResponse> res = taskService.getTasks(pageable,filters);

        Assertions.assertNotNull(res);
        Assertions.assertNotNull(res.getList());
        Assertions.assertEquals(2, res.getList().size());
        Mockito.verify(taskRepository).findWithFilters(null, null, null, null, pageable);
    }

    @Test
    void createTask(){
       final CreateTaskRequest  request = new CreateTaskRequest();
       request.setTitle("Task 1");
       request.setDescription("Task Description 1");
       request.setDueDate(LocalDate.now());

       final Task task = new Task();
       task.setTitle("Task 1");
       task.setDescription("Task Description 1");
       task.setDueDate(LocalDate.now());
       task.setCompleted(false);

       final TaskResponse taskResponse = new TaskResponse();
       taskResponse.setId(1);
       taskResponse.setTitle("Task 1");
       taskResponse.setDescription("Task Description 1");
       taskResponse.setDueDate(LocalDate.of(2025, 9, 12));

       Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
       Mockito.when(modelMapper.map(request, Task.class)).thenReturn(task);
        Mockito.when(modelMapper.map(task, TaskResponse.class)).thenReturn(taskResponse);

       final TaskResponse response = taskService.createTask(request);
       Assertions.assertNotNull(response);
       Assertions.assertEquals(request.getTitle(), response.getTitle());
       Assertions.assertEquals(request.getDescription(), response.getDescription());
    }

    @Test
    void getTaskById(){

        final Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");

        final TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(1);
        taskResponse.setTitle("Task 1");

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(task));
        Mockito.when(modelMapper.map(task, TaskResponse.class)).thenReturn(taskResponse);

        final TaskResponse response = taskService.getTask(1);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(taskResponse.getId(), response.getId());
        Assertions.assertEquals(taskResponse.getTitle(), response.getTitle());

    }

    @Test
    void getTaskByIdWrongId(){

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenThrow(new NoSuchTaskExistsException(2));

        final TaskResponse response = taskService.getTask(2);

        Assertions.assertNull(response);

    }

    @Test
    void getTaskByIdOptionalNotFound(){

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        final TaskResponse response = taskService.getTask(2);

        Assertions.assertNull(response);

    }

    @Test
    void updateTask(){
       final UpdateTaskRequest request = new UpdateTaskRequest();
       request.setTitle("Task 2");
       request.setDescription("Task Description 2");
       request.setDueDate(LocalDate.now());
       request.setCompleted(true);

       final Task task = new Task();
       task.setTitle("Task 1");
       task.setDescription("Task Description 1");
       task.setDueDate(LocalDate.now());
       task.setCompleted(false);

        final TaskResponse taskRes = new TaskResponse();
        taskRes.setTitle("Task 2");
        taskRes.setDescription("Task Description 2");
        taskRes.setDueDate(LocalDate.now());
        taskRes.setCompleted(true);

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(task));
        Mockito.when(modelMapper.map(task, TaskResponse.class)).thenReturn(taskRes);

        final TaskResponse response = taskService.updateTask(1, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(task.getId(), response.getId());
        Assertions.assertEquals(request.getTitle(), response.getTitle());

    }

    @Test
    void updateTaskIsEmpty(){
        final UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Task 2");
        request.setDescription("Task Description 2");
        request.setDueDate(LocalDate.now());
        request.setCompleted(true);

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        final TaskResponse response = taskService.updateTask(1, request);

        Assertions.assertNull(response);

    }

    @Test
    void updateTaskException(){
        final UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Task 2");
        request.setDescription("Task Description 2");
        request.setDueDate(LocalDate.now());
        request.setCompleted(true);

        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenThrow(new NoSuchTaskExistsException(1));

        final TaskResponse response = taskService.updateTask(1, request);

        Assertions.assertNull(response);

    }

    @Test
    void deleteTask(){

        final Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Task Description 1");
        task.setDueDate(LocalDate.now());
        task.setCompleted(false);


        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(task));

        taskService.deleteTask(1);

    }

    @Test
    void deleteTaskNotFound(){

        final Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Task Description 1");
        task.setDueDate(LocalDate.now());
        task.setCompleted(false);


        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        taskService.deleteTask(1);

    }

    @Test
    void deleteTaskException(){

        final Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Task Description 1");
        task.setDueDate(LocalDate.now());
        task.setCompleted(false);


        Mockito.when(taskRepository.findById(Mockito.anyInt())).thenThrow(new NoSuchTaskExistsException(task.getId()));

        taskService.deleteTask(1);

    }

}
