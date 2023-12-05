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

/**
 * Controller class for handling user-related operations.
 */
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenHandler jwtTokenHandler;

    // Record representing user data transfer object (DTO)
    public record UserDTO(String username, String password) {
    }

    ;

    /**
     * Constructor for UserController.
     *
     * @param userService     The service handling user operations.
     * @param jwtTokenHandler The handler for JWT tokens.
     */
    @Autowired
    public UserController(UserService userService, JwtTokenHandler jwtTokenHandler) {
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    /**
     * Registers a new user.
     *
     * @param userDTO The data transfer object containing user information.
     * @return ResponseEntity containing the registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    /**
     * Logs in a user.
     *
     * @param user The data transfer object containing user credentials.
     * @return A JWT token representing the user's session.
     */
    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {
        return userService.login(user.username(), user.password());
    }

    /**
     * Retrieves information about a user.
     *
     * @param username The username of the user to retrieve information about.
     * @param token    The JWT token for authentication.
     * @return ResponseEntity containing user information.
     */
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String username, @RequestHeader("Authorization") String token) {
        // Validate the JWT token
        boolean isValid = jwtTokenHandler.validateToken(token);
        if (isValid) {
            // Get user ID from the token and retrieve user information
            int userId = jwtTokenHandler.getTokenId(token);
            Optional<Map<String, Object>> user = userService.getUserInfo(username, userId);
            return ResponseEntity.ok(user.get());
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}
