package com.example.me.code.individual_assignment.controller;

import com.example.me.code.individual_assignment.exceptions.ImageSizeTooLargeException;
import com.example.me.code.individual_assignment.exceptions.InvalidTokenException;
import com.example.me.code.individual_assignment.security.JwtTokenHandler;
import com.example.me.code.individual_assignment.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenHandler jwtTokenHandler) {
        this.imageService = imageService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("/upload/{folderId}")
    public ResponseEntity<String> uploadImage(@RequestHeader("Authorization") String token, @RequestParam("image") MultipartFile file, @PathVariable int folderId) throws InvalidTokenException, ImageSizeTooLargeException {
        boolean isValid = jwtTokenHandler.validateToken(token);

        if (isValid) {
            int userId = jwtTokenHandler.getTokenId(token);
            return ResponseEntity.ok(imageService.uploadImage(file, userId, folderId));
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}
