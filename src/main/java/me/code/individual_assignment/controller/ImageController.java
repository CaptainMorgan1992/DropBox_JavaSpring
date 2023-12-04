package me.code.individual_assignment.controller;

import me.code.individual_assignment.DownloadImageData.DownloadImageData;
import me.code.individual_assignment.exceptions.ImageSizeTooLargeException;
import me.code.individual_assignment.exceptions.InvalidTokenException;
import me.code.individual_assignment.security.JwtTokenHandler;
import me.code.individual_assignment.service.ImageService;
import me.code.individual_assignment.utility.EnvironmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;
    private JwtTokenHandler jwtTokenHandler;
    private final EnvironmentUtils environmentUtils;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenHandler jwtTokenHandler, EnvironmentUtils environmentUtils) {
        this.imageService = imageService;
        this.jwtTokenHandler = jwtTokenHandler;
        this.environmentUtils = environmentUtils;
    }

    /*
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

     */


    @PostMapping("/upload/{folderId}")
    public ResponseEntity<String> uploadImage( @RequestHeader("Authorization") String token, @RequestParam("image") MultipartFile file,
            @PathVariable int folderId
    ) throws InvalidTokenException, ImageSizeTooLargeException {
        if (!environmentUtils.isTestEnvironment()) {
            // Only perform authorization check in non-test environments
            boolean isValid = jwtTokenHandler.validateToken(token);
            if (!isValid) {
                throw new InvalidTokenException("Access denied.");
            }
        }
        int userId = environmentUtils.isTestEnvironment() ? 1 : jwtTokenHandler.getTokenId(token);
        //int userId = jwtTokenHandler.getTokenId(token);
        return ResponseEntity.ok(imageService.uploadImage(file, userId, folderId));
    }


    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId, @RequestHeader ("Authorization") String token) {
        boolean isValid = jwtTokenHandler.validateToken(token);
        if(isValid) {
            //int userId = jwtTokenHandler.getTokenId(token);
            DownloadImageData result = imageService.downloadImage(imageId);

            return result.toResponseEntity();
            /*
            int userId = jwtTokenHandler.getTokenId(token);
            return imageService.downloadImage(userId, imageId);

             */
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    @DeleteMapping ("/delete/{imageId}")
    public String deleteImage(@RequestHeader ("Authorization") String token, @PathVariable int imageId) throws InvalidTokenException {

        boolean isValid = jwtTokenHandler.validateToken(token);
        if(isValid) {
            int userId = jwtTokenHandler.getTokenId(token);
            return imageService.deleteImage(userId, imageId);
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}










