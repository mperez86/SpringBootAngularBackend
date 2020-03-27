package com.curso.backend.service;

import com.curso.backend.entity.User;

public interface UserServiceInterface {

    public void save(User user);

    public User checkUserLogin(User user);

    public User findById(Long id);

    public User findUser(User user);
}
