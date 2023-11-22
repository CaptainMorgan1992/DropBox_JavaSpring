package com.example.me.code.individual_assignment.controller;

import com.example.me.code.individual_assignment.exceptions.InvalidTokenException;
import com.example.me.code.individual_assignment.model.Folder;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import com.example.me.code.individual_assignment.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private FolderService folderService;
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public FolderController(FolderService folderService, JwtTokenHandler jwtTokenHandler) {
        this.folderService = folderService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("/create")
    public ResponseEntity<Folder> createNewFolder(@RequestHeader("Authorization") String token) throws InvalidTokenException{
        boolean isValid = jwtTokenHandler.validateToken(token);
        if(isValid) {
            Folder folder = folderService.createNewFolder(userId);
            return ResponseEntity.ok().body(folder);
        } else throw new InvalidTokenException("Access denied.");
    }
}
