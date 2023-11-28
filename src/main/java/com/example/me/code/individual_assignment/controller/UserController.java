package com.example.me.code.individual_assignment.controller;

import com.example.me.code.individual_assignment.exceptions.InvalidTokenException;
import com.example.me.code.individual_assignment.exceptions.UserAlreadyExistException;
import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import com.example.me.code.individual_assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    private record UserDTO(String username, String password){};

    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public UserController(UserService userService, JwtTokenHandler jwtTokenHandler) {
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return result;
    }


    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUser (@PathVariable String username, @RequestHeader("Authorization") String token) throws InvalidTokenException {

        boolean isValid = jwtTokenHandler.validateToken(token);
        if(isValid) {
            int userId = jwtTokenHandler.getTokenId(token);
            Optional<Map<String, Object>> user = userService.getUserInfo(username, userId);
            return ResponseEntity.ok(user.get());
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

}
