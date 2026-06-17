package com.example.crudrapido.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudrapido.model.User;
import com.example.crudrapido.service.UserService;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll(){
        return userService.getUsers();
    }
    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    @PostMapping
    public User saveUpdate(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id ) {
        userService.delete(id);
    }
}
