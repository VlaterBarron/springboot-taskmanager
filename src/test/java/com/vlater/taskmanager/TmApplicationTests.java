package com.vlater.taskmanager;

import com.vlater.taskmanager.controller.TaskController;
import com.vlater.taskmanager.dto.response.PagedResponse;
import com.vlater.taskmanager.dto.response.TaskResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {TmApplication.class})
class TmApplicationTests {

    private final static String ORDER_BY = "id";
    private final static String DIRECTION = "asc";
    private final static int PAGE = 0;
    private final static int SIZE = 10;

    @Autowired
    private TaskController taskController;

    @Test
    void getTasksWithNoFilters(){
        final ResponseEntity<PagedResponse<TaskResponse>> res = taskController.getAllTasks(PAGE, SIZE, ORDER_BY, DIRECTION, null, null, null, null);
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(Collections.emptyList(), res.getBody().getList());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }

}
