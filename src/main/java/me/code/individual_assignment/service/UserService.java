package me.code.individual_assignment.service;

import me.code.individual_assignment.exceptions.ForbiddenPathException;
import me.code.individual_assignment.exceptions.UserAlreadyExistException;
import me.code.individual_assignment.exceptions.UserNotFoundException;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.repository.UserRepository;
import me.code.individual_assignment.security.JwtTokenHandler;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The UserService class provides services related to user operations, including registration and login.
 */
@Service
@NoArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private JwtTokenHandler jwtTokenHandler;

    /**
     * Constructor for the UserService class.
     *
     * @param userRepository  The repository for user data.
     * @param jwtTokenHandler The handler for JWT token generation.
     */
    @Autowired
    public UserService(UserRepository userRepository, JwtTokenHandler jwtTokenHandler) {
        this.userRepository = userRepository;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return The registered user.
     * @throws UserAlreadyExistException If a user with the same username already exists.
     */
    public User register(String username, String password) {
        User newUser = new User(username, password);
        try {
            // Save the new user to the repository.
            userRepository.save(newUser);
            return newUser;
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistException("Registration failed. Try another username");
        }
    }

    /**
     * Logs in a user with the provided username and password, generating a JWT token upon successful login.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return A message indicating successful login along with the generated JWT token.
     * @throws IllegalArgumentException If login fails due to incorrect username or password.
     */
    public String login(String username, String password) {
        boolean isCorrectCredentials = userRepository.isValidUser(username, password);

        if (isCorrectCredentials) {
            // Find the user by username.
            User user = findUserByUsername(username);
            // Generate a JWT token for the user.
            String token = jwtTokenHandler.generateToken(user);
            return "You have successfully logged in. Here's your token: " + token;
        } else {
            throw new IllegalArgumentException("Login failed, incorrect username or password");
        }
    }

    /**
     * Finds a user by username.
     *
     * @param username The username of the user to find.
     * @return The found user.
     * @throws UserNotFoundException If the user with the specified username is not found.
     */
    private User findUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new UserNotFoundException("User with username: {" + username + "} could not be found");
        }
    }

    /**
     * Retrieves information about a user, including their folders and images.
     *
     * @param username The username of the requesting user.
     * @param userId   The ID of the user for whom information is requested.
     * @return An optional map containing user information.
     * @throws ForbiddenPathException If the requesting user is not permitted to see the requested user's information.
     */
    @Transactional
    public Optional<Map<String, Object>> getUserInfo(String username, int userId) {
        try {
            // Check if the requesting user is permitted to see the requested user's information.
            boolean isUserPermittedToSeeInfo = isUserPermitted(username, userId);

            if (isUserPermittedToSeeInfo) {
                // Retrieve optional user information from the repository.
                Optional<User> userOptional = userRepository.getUserInfo(userId);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    // Build and return a map containing user information.
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

    /**
     * Checks if a user is permitted to see the information of another user.
     *
     * @param username The username of the requesting user.
     * @param userId   The ID of the user whose information is requested.
     * @return true if the requesting user is permitted, false otherwise.
     */
    private boolean isUserPermitted(String username, int userId) {
        return userRepository.compareUsernameWithId(username, userId);
    }

    /**
     * Builds a map containing information about a user, including their folders and images.
     *
     * @param user The user for whom information is being built.
     * @return A map containing user information.
     */
    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());

        List<Map<String, Object>> folders = user.getFolders().stream().map(this::buildFolderInfo).collect(Collectors.toList());
        result.put("folders", folders);

        return result;
    }

    /**
     * Builds a map containing information about a folder, including its name and images.
     *
     * @param folder The folder for which information is being built.
     * @return A map containing folder information.
     */
    private Map<String, Object> buildFolderInfo(Folder folder) {
        Map<String, Object> folderMap = new HashMap<>();
        folderMap.put("name", folder.getName());
        folderMap.put("id", folder.getId());

        List<Map<String, Object>> images = folder.getImages().stream().map(this::buildImageInfo).collect(Collectors.toList());
        folderMap.put("images", images);

        return folderMap;
    }

    /**
     * Builds a map containing information about an image, including its name, ID, and size.
     *
     * @param image The image for which information is being built.
     * @return A map containing image information.
     */
    private Map<String, Object> buildImageInfo(Image image) {
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("name", image.getName());
        imageMap.put("id", image.getId());
        imageMap.put("size", image.getSize());

        return imageMap;
    }
}