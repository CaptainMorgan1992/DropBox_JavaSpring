package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.exceptions.ForbiddenPathException;
import com.example.me.code.individual_assignment.exceptions.ImageDoesNotBelongToUserException;
import com.example.me.code.individual_assignment.exceptions.UserAlreadyExistException;
import com.example.me.code.individual_assignment.exceptions.UserNotFoundException;
import com.example.me.code.individual_assignment.model.Folder;
import com.example.me.code.individual_assignment.model.Image;
import com.example.me.code.individual_assignment.model.User;
import com.example.me.code.individual_assignment.repository.UserRepository;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(isCorrectCredentials){
            User user = findUserByUsername(username);
            String token = jwtTokenHandler.generateToken(user);
            return "You have successfully logged in. Here's your token: " + token;
        }
        else throw new IllegalArgumentException("Login failed, incorrect username or password");
    }

    private User findUserByUsername(String username) throws UserNotFoundException {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new UserNotFoundException("User with username: {" + username + "} could not be found");
        }
    }
/*
    @Transactional
    public Optional<Map<String, Object>> getUserInfo(String username, int userId) {
        boolean isUserPermittedToSeeInfo = userRepository.compareUsernameWithId(username, userId);
        Optional<User> userOptional = userRepository.getUserInfo(userId);

    if (userOptional.isPresent() && isUserPermittedToSeeInfo) {
        User user = userOptional.get();

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());

        List<Map<String, Object>> folders = user.getFolders().stream().map(folder -> {
            Map<String, Object> folderMap = new HashMap<>();
            folderMap.put("name", folder.getName());
            folderMap.put("id", folder.getId());

            List<Map<String, Object>> images = folder.getImages().stream().map(image -> {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("name", image.getName());
                imageMap.put("id", image.getId());
                imageMap.put("size", image.getSize());

                return imageMap;
            }).collect(Collectors.toList());

            folderMap.put("images", images);
            return folderMap;
        }).collect(Collectors.toList());

        result.put("folders", folders);

        return Optional.of(result);
    }

    return Optional.empty();
}

 */

    @Transactional
    public Optional<Map<String, Object>> getUserInfo(String username, int userId) throws ForbiddenPathException {
        try {
            boolean isUserPermittedToSeeInfo = isUserPermitted(username, userId);

            if (isUserPermittedToSeeInfo) {
                Optional<User> userOptional = userRepository.getUserInfo(userId);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    return Optional.of(buildUserInfo(user));
                } else {
                    throw new ForbiddenPathException("User with username: " + username + " could not be found");
                }
            } else {
                throw new ForbiddenPathException("You are not allowed to see information about the requested user");
            }
        } catch (Exception e) {
           throw new ForbiddenPathException("User with username: " + username + " could not be found");
        }
    }

    private boolean isUserPermitted(String username, int userId) {
        return userRepository.compareUsernameWithId(username, userId);
    }

    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());

        List<Map<String, Object>> folders = user.getFolders().stream().map(this::buildFolderInfo).collect(Collectors.toList());
        result.put("folders", folders);

        return result;
    }

    private Map<String, Object> buildFolderInfo(Folder folder) {
        Map<String, Object> folderMap = new HashMap<>();
        folderMap.put("name", folder.getName());
        folderMap.put("id", folder.getId());

        List<Map<String, Object>> images = folder.getImages().stream().map(this::buildImageInfo).collect(Collectors.toList());
        folderMap.put("images", images);

        return folderMap;
    }

    private Map<String, Object> buildImageInfo(Image image) {
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("name", image.getName());
        imageMap.put("id", image.getId());
        imageMap.put("size", image.getSize());

        return imageMap;
    }

}
