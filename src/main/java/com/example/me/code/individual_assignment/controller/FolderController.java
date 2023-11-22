package com.example.me.code.individual_assignment.controller;

import com.example.me.code.individual_assignment.exceptions.InvalidTokenException;
import com.example.me.code.individual_assignment.model.Folder;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import com.example.me.code.individual_assignment.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private FolderService folderService;
    private JwtTokenHandler jwtTokenHandler;

    private record FolderDTO(String name){};

    @Autowired
    public FolderController(FolderService folderService, JwtTokenHandler jwtTokenHandler) {
        this.folderService = folderService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("/create")
    public ResponseEntity<Folder> createNewFolder(@RequestHeader("Authorization") String token, @RequestBody FolderDTO folderDTO) throws InvalidTokenException{
        boolean isValid = jwtTokenHandler.validateToken(token);
        if(isValid) {
            int userId = jwtTokenHandler.getTokenId(token);
            return ResponseEntity.ok().body(folderService.createNewFolder(userId, folderDTO.name));
        } else throw new InvalidTokenException("Access denied.");
    }
}
