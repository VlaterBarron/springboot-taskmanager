package com.vlater.taskmanager.repository;

import com.vlater.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    Task findTaskById(int id);

}
