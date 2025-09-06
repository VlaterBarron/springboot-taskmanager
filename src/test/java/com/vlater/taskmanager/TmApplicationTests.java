package com.vlater.taskmanager;

import com.vlater.taskmanager.dto.request.Filters;
import com.vlater.taskmanager.dto.response.PagedResponse;
import com.vlater.taskmanager.dto.response.TaskResponse;
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

}
