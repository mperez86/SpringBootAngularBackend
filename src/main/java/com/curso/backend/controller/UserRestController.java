package com.curso.backend.controller;

import com.curso.backend.entity.User;
import com.curso.backend.model.JwtUser;
import com.curso.backend.security.JwtGenerator;
import com.curso.backend.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RequestMapping("/auth")
public class UserRestController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping(value = "/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {

        if(userServiceInterface.findUser(user) == null) {
            userServiceInterface.save(user);
            User userDb = userServiceInterface.checkUserLogin(user);
            JwtUser jwtUser = new JwtUser();
            jwtUser.setId(userDb.getId());
            jwtUser.setUsername(userDb.getEmail());

            return new ResponseEntity<>((Collections.singletonMap("jwtToken", jwtGenerator.generate(jwtUser))),HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> login(@RequestBody User user) {

        User userDb = userServiceInterface.checkUserLogin(user);

        if(userDb != null) {
            JwtUser jwtUser = new JwtUser();
            jwtUser.setId(userDb.getId());
            jwtUser.setUsername(userDb.getEmail());
            return new ResponseEntity<>((Collections.singletonMap("jwtToken", jwtGenerator.generate(jwtUser))),HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }
}
