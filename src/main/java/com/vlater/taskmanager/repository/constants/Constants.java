package com.vlater.taskmanager.repository.constants;

import java.io.Serial;
import java.io.Serializable;

public class Constants implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final static String QUERY_FIND_WITH_FILTERS =
            """
            SELECT t FROM Task t WHERE
            (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND
            (:completed IS NULL OR t.completed = :completed) AND
            (:startDate IS NULL OR t.dueDate >= :startDate) AND
            (:endDate IS NULL OR t.dueDate <= :endDate)
            """;
}
