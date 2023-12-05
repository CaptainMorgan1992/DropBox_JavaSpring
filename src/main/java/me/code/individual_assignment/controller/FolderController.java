package me.code.individual_assignment.controller;

import me.code.individual_assignment.exceptions.InvalidTokenException;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.security.JwtTokenHandler;
import me.code.individual_assignment.service.FolderService;
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

    /**
     * Data class representing data transfer for folder operations.
     */
    private record FolderDTO(String name) {
    }

    ;

    /**
     * Constructor for creating an instance of FolderController.
     *
     * @param folderService   Service for folder management.
     * @param jwtTokenHandler Handler for JWT tokens.
     */
    @Autowired
    public FolderController(FolderService folderService, JwtTokenHandler jwtTokenHandler) {
        this.folderService = folderService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    /**
     * Handles a POST request to create a new folder.
     *
     * @param token     Authentication token taken from the Authorization header.
     * @param folderDTO Data object containing information about the new folder.
     * @return ResponseEntity containing the result of folder creation.
     * @throws InvalidTokenException Thrown if the authentication token is invalid.
     */
    @PostMapping("/create")
    public ResponseEntity<Folder> createNewFolder(
            @RequestHeader(value = "Authorization") String token, @RequestBody FolderDTO folderDTO) {
        boolean isValid = jwtTokenHandler.validateToken(token);
        if (isValid) {
            int userId = jwtTokenHandler.getTokenId(token);
            return ResponseEntity.ok().body(folderService.createNewFolder(userId, folderDTO.name));
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}

