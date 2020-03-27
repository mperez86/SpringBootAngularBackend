package com.curso.backend.service;

import com.curso.backend.dao.UserDaoInterface;
import com.curso.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserDaoInterface userDao;

    @Override
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User checkUserLogin(User user) {
        return (User) userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return (User) userDao.findByIdSQL(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(User user) {
        return userDao.findByEmail(user.getEmail());
    }
}
