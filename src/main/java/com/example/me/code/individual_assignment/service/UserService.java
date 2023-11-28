package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.exceptions.UserAlreadyExistException;
import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.repository.UserRepository;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenHandler jwtTokenHandler) {
        this.userRepository = userRepository;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    public User register(String username, String password) throws UserAlreadyExistException {
        User newUser = new User(username, password);
        try {
            userRepository.save(newUser);
            return newUser;
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistException("Registration failed. Try another username");
        }
    }

    public String login (String username, String password) {

    boolean isCorrectCredentials = userRepository.isValidUser(username, password);
        System.out.println(isCorrectCredentials);
        if(isCorrectCredentials){
            User user = findUserByUsername(username);
            String token = jwtTokenHandler.generateToken(user);
            return "You have successfully logged in. Here's your token: " + token;
        } else throw new IllegalArgumentException("Login failed, incorrect username or password");
    }

    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    /*
    @Transactional
    public List<User> getUser(String username) {
        User user = findUserByUsername(username);
        if (user != null) {
            int userId = user.getId();
            List<User> users = findUserByUserId(userId);
            return users;
        }
        return null;
    }

     */
/*
    private List<User> findUserByUserId(int userId) {
        return userRepository.getUserInfo(userId);
    }

 */

}
