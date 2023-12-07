package me.code.individual_assignment.controller;

import me.code.individual_assignment.dto.DownloadImageData;
import me.code.individual_assignment.exceptions.InvalidTokenException;
import me.code.individual_assignment.security.JwtTokenHandler;
import me.code.individual_assignment.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller class for handling image-related operations.
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;
    private final JwtTokenHandler jwtTokenHandler;

    /**
     * Constructor for ImageController.
     *
     * @param imageService    The service handling image operations.
     * @param jwtTokenHandler The handler for JWT tokens.
     */
    @Autowired
    public ImageController(ImageService imageService, JwtTokenHandler jwtTokenHandler) {
        this.imageService = imageService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    /**
     * Uploads an image to the specified folder.
     *
     * @param token    The JWT token for authentication.
     * @param file     The image file to upload.
     * @param folderId The ID of the folder where the image should be uploaded.
     * @return ResponseEntity containing the uploaded image data.
     */
    @PostMapping("/upload/{folderId}")
    public ResponseEntity<Resource> uploadImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile file,
            @PathVariable int folderId) {

        // Validate the JWT token
        boolean isValid = jwtTokenHandler.validateToken(token);
        if (!isValid) {
            throw new InvalidTokenException("Access denied.");
        }

        // Get user ID from the token
        int userId = jwtTokenHandler.getTokenId(token);

        // Upload the image and return the response entity
        DownloadImageData result = imageService.uploadImage(file, userId, folderId);
        return result.toResponseEntity();
    }

    /**
     * Downloads an image with the specified ID.
     *
     * @param imageId The ID of the image to download.
     * @param token   The JWT token for authentication.
     * @return ResponseEntity containing the downloaded image data.
     */
    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId, @RequestHeader("Authorization") String token) {
        // Validate the JWT token
        boolean isValid = jwtTokenHandler.validateToken(token);
        if (isValid) {
            // Download the image and return the response entity
            DownloadImageData result = imageService.downloadImage(imageId);
            return result.toResponseEntity();
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    /**
     * Deletes an image with the specified ID.
     *
     * @param token   The JWT token for authentication.
     * @param imageId The ID of the image to delete.
     * @return A message indicating the result of the deletion.
     */
    @DeleteMapping("/delete/{imageId}")
    public String deleteImage(@RequestHeader("Authorization") String token, @PathVariable int imageId) {
        // Validate the JWT token
        boolean isValid = jwtTokenHandler.validateToken(token);
        if (isValid) {
            // Get user ID from the token and delete the image
            int userId = jwtTokenHandler.getTokenId(token);
            return imageService.deleteImage(userId, imageId);
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}










