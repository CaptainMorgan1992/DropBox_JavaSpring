package me.code.individual_assignment.controller;

import me.code.individual_assignment.exceptions.InvalidTokenException;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.security.JwtTokenHandler;
import me.code.individual_assignment.service.FolderService;
import me.code.individual_assignment.utility.EnvironmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling requests related to folders.
 */
@RestController
@RequestMapping("/folder")
public class FolderController {

    private FolderService folderService;
    private JwtTokenHandler jwtTokenHandler;
    private final EnvironmentUtils environmentUtils;

    /**
     * Data class representing data transfer for folder operations.
     */
    private record FolderDTO(String name) {};

    /**
     * Constructor for creating an instance of FolderController.
     *
     * @param folderService    Service for folder management.
     * @param jwtTokenHandler  Handler for JWT tokens.
     */
    @Autowired
    public FolderController(FolderService folderService, JwtTokenHandler jwtTokenHandler, EnvironmentUtils environmentUtils) {
        this.folderService = folderService;
        this.jwtTokenHandler = jwtTokenHandler;
        this.environmentUtils = environmentUtils;
    }

    /**
     * Handles a POST request to create a new folder.
     *
     * @param token      Authentication token taken from the Authorization header.
     * @param folderDTO  Data object containing information about the new folder.
     * @return           ResponseEntity containing the result of folder creation.
     * @throws InvalidTokenException Thrown if the authentication token is invalid.
     */
    @PostMapping("/create")
    public ResponseEntity<Folder> createNewFolder(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody FolderDTO folderDTO) throws InvalidTokenException {

        int userId = 0;
        // If not in the test environment, validate the authentication token
        if (!environmentUtils.isTestEnvironment()) {
            // Validate the authentication token
            boolean isValid = jwtTokenHandler.validateToken(token);

            // If the token is valid, retrieve the user ID and create a new folder
            if (isValid) {
                userId = jwtTokenHandler.getTokenId(token);
                return ResponseEntity.ok().body(folderService.createNewFolder(userId, folderDTO.name));
            } else {
                // Throw an exception if the token is invalid
                throw new InvalidTokenException("Access denied.");
            }
        } else {
            // If in the test environment, create a new folder without token validation
            return ResponseEntity.ok().body(folderService.createNewFolder(userId, folderDTO.name));
        }
    }
}

