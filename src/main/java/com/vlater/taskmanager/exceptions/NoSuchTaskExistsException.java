package com.vlater.taskmanager.exceptions;

public class NoSuchTaskExistsException extends RuntimeException {

    public NoSuchTaskExistsException(int id) {
        super("No such task with id: " + id);
    }

}
