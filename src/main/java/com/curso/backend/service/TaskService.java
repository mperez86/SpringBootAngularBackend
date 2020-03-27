package com.curso.backend.service;

import com.curso.backend.dao.TaskDaoInterface;
import com.curso.backend.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService implements TaskServiceInterface {

    @Autowired
    private TaskDaoInterface taskDaoInterface;

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return (List<Task>) taskDaoInterface.findAll();
    }

    @Override
    @Transactional
    public void save(Task task) {
        taskDaoInterface.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksUser(Long id) {
        return (List<Task>) taskDaoInterface.findByUserId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTask(Task task) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Task findByIdSQL(Long id) {
        return taskDaoInterface.findByIdSQL(id);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskDaoInterface.deleteById(id);
    }
}
