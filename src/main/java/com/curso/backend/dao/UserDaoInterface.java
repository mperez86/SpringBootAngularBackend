package com.curso.backend.dao;

import com.curso.backend.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDaoInterface extends CrudRepository<User, Long> {

    public User findByEmail(String email);

    public User findByEmailAndPassword(String email, String password);

    @Query("select u from User u where u.id=?1")
    public User findByIdSQL(Long id);
}
