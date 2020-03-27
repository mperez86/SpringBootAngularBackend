package com.curso.backend.service;

import com.curso.backend.entity.Task;

import java.util.List;

public interface TaskServiceInterface {

    public List<Task> findAll();

    public void save(Task task);

    public List<Task> getTasksUser(Long id);

    public Task findById(Long id);

    public Task findTask(Task task);

    public Task findByIdSQL(Long id);

    public void deleteTask(Long id);
}
