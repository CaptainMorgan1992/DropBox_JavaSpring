package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        User newUser = new User(username, password);
        try {
            userRepository.save(newUser);
            return newUser;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
        return newUser;
    }

    public String login(String username, String password) {
        // Här ska användaren logga in, här vill vi ta emot uppgifterna
        // (username & password) och se ifall de matchar mot databasen
    boolean isCorrectCredentials = userRepository.isValidUser(username, password);
        // Vi vill också skapa en session för användaren där ett token genereras och visas i body'n

        if(isCorrectCredentials){
            return "You have successfully logged in";
        } else {
            System.out.println("Login failed, incorrect username or password");
        }
        return null;
    }
}
