package com.vlater.taskmanager;

import com.vlater.taskmanager.controller.TaskController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {TmApplication.class})
class TmApplicationTests {

    @Autowired
    private TaskController taskController;

	@Test
	void contextLoads() throws Exception{
        assertThat(taskController).isNotNull();
	}

}
