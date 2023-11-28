package com.example.me.code.individual_assignment.controller;

import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    private record UserDTO(String username, String password){};

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO){
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return result;
    }

    /*
    @GetMapping("/user/{username}")
    public ResponseEntity<Map<String, Object>> getUser (@PathVariable String username) {
        List<User> user = userService.getUser(username);
        return ResponseEntity.ok((Map<String, Object>) user);
    }

     */
}
