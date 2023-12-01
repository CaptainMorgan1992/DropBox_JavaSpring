package me.code.individual_assignment.controller;

import me.code.individual_assignment.exceptions.InvalidTokenException;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.security.JwtTokenHandler;
import me.code.individual_assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private JwtTokenHandler jwtTokenHandler;
    public record UserDTO(String username, String password){};

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
