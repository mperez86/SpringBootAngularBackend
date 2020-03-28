package com.curso.backend.controller;

import com.curso.backend.entity.Task;
import com.curso.backend.model.JwtUser;
import com.curso.backend.security.JwtValidator;
import com.curso.backend.service.TaskServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE})
@RequestMapping("/api")
public class TaskRestController {

    public static final String  UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/";
    @Autowired
    private TaskServiceInterface taskServiceInterface;

    @Autowired
    private JwtValidator validator;

    @PostMapping("/task")
    public ResponseEntity<?> addTask(@RequestBody Task task, @RequestHeader (name = "Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        JwtUser jwtUser = validator.validate(token);
        task.setUserId(Long.valueOf(jwtUser.getId()));
        task.setStatus("to-do");
        taskServiceInterface.save(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/task")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @RequestHeader (name = "Authorization") String bearerToken) {
        Task taskUpdate = taskServiceInterface.findByIdSQL(task.getId());
        taskUpdate.setStatus(task.getStatus());
        taskServiceInterface.save(taskUpdate);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/task/list")
    public ResponseEntity<?> listTasks(@RequestHeader (name = "Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        JwtUser jwtUser = validator.validate(token);
        List<Task> listTask = taskServiceInterface.getTasksUser(jwtUser.getId());

        if(listTask != null && listTask.size() != 0) {
            return new ResponseEntity<>(listTask, HttpStatus.OK);
        } else  {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(value = "id") Long id) {
        taskServiceInterface.deleteTask(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/task/upload")
    public ResponseEntity<?> createTaskImage(@RequestParam("image") MultipartFile file,
                                             @RequestParam("name") String name, @RequestParam("description") String description,
                                             @RequestHeader(name = "Authorization") String bearerToken) {
        Task task = new Task();
        String token = bearerToken.substring(7);
        JwtUser jwtUser = validator.validate(token);
        task.setUserId(Long.valueOf(jwtUser.getId()));
        task.setStatus("to-do");
        task.setName(name);
        task.setDescription(description);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        task.setImageUrl(fileDownloadUri);
        taskServiceInterface.save(task);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
