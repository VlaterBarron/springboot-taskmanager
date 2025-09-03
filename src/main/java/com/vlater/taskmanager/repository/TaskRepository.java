package com.vlater.taskmanager.repository;

import com.vlater.taskmanager.model.Task;
import com.vlater.taskmanager.repository.constants.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    @Query(Constants.QUERY_FIND_WITH_FILTERS)
    Page<Task> findWithFilters(
            @Param("title") String title,
            @Param("completed") Boolean completed,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
