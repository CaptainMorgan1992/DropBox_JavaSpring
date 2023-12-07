package me.code.individual_assignment.service;

import me.code.individual_assignment.dto.DownloadImageData;
import me.code.individual_assignment.exceptions.*;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.repository.FolderRepository;
import me.code.individual_assignment.repository.ImageRepository;
import me.code.individual_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * The ImageService class provides services related to image operations.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final FolderRepository folderRepository;
    private UserRepository userRepository;

    /**
     * Constructor for the ImageService class.
     *
     * @param imageRepository  The repository for image data.
     * @param folderRepository The repository for folder data.
     * @param userRepository   The repository for user data.
     */
    @Autowired
    public ImageService(ImageRepository imageRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Uploads an image file for a specific user to a specified folder.
     *
     * @param file     The image file to be uploaded.
     * @param userId   The ID of the user for whom the image is uploaded.
     * @param folderId The ID of the folder to which the image is uploaded.
     * @return DownloadImageData containing a resource and image details.
     * @throws FailedImageUploadException If the image upload fails.
     * @throws ImageSizeTooLargeException If the image size exceeds the allowed limit.
     * @throws ByteConversionException    If there is an issue converting the image to bytes.
     */
    public DownloadImageData uploadImage(MultipartFile file, int userId, int folderId) {
        try {
            // Check if the image size is valid.
            if (isValidImageSize(file)) {
                Image image;
                Folder folder;
                try {
                    // Find the folder for the specified user and folder ID.
                    folder = findFolder(userId, folderId);
                    // Create a new image with the provided file and associated folder.
                    image = new Image(file, folder);
                } catch (Exception e) {
                    throw new ByteConversionException("Failed to convert image to bytes");
                }
                // Add the image to the folder and save it to the repository.
                folder.getImages().add(image);
                imageRepository.save(image);
                // Create a ByteArrayResource for the image data and return the DownloadImageData.
                ByteArrayResource resource = new ByteArrayResource(image.getData());
                return new DownloadImageData(resource, image);
            } else {
                throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
            }
        } catch (Exception e) {
            throw new FailedImageUploadException("Could not upload image: " + e.getMessage());
        }
    }

    /**
     * Checks if the size of the image is valid.
     *
     * @param file The image file.
     * @return true if the image size is valid, false otherwise.
     */
    public boolean isValidImageSize(MultipartFile file) {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        return imageSize <= maxSize;
    }

    /**
     * Finds a folder for a specific user and folder ID.
     *
     * @param userId   The ID of the user.
     * @param folderId The ID of the folder.
     * @return The found folder.
     * @throws FolderNotFoundException If the folder is not found.
     */
    public Folder findFolder(int userId, int folderId) {
        boolean isFolderPresent = folderRepository.existsByUserIdAndFolderId(userId, folderId);

        if (isFolderPresent) {
            // Retrieve and return the folder by its ID.
            Optional<Folder> folder = folderRepository.findById(folderId);
            return folder.get();
        } else {
            throw new FolderNotFoundException("Folder not found for folderId: " + folderId);
        }
    }

    /**
     * Deletes an image for a specific user by image ID.
     *
     * @param userId  The ID of the user.
     * @param imageId The ID of the image to be deleted.
     * @return A message indicating the success of the deletion.
     * @throws ImageDoesNotBelongToUserException If the image does not belong to the specified user.
     */
    public String deleteImage(int userId, int imageId) throws ImageDoesNotBelongToUserException {
        try {
            // Check if the image exists and belongs to the specified user.
            boolean isImageExisting = doesImageExist(imageId);
            boolean doesImageBelongToUser = isImageBelongingToUser(userId, imageId);

            if (isImageExisting && doesImageBelongToUser) {
                // Delete the image by its ID.
                imageRepository.deleteById(imageId);
                return "Image deleted successfully";
            } else {
                throw new ImageDoesNotBelongToUserException("Image does not exist");
            }
        } catch (Exception e) {
            throw new ImageDoesNotBelongToUserException("Image could not be deleted. The image either does not belong to the user who's trying to delete the image, or the image does not exist.");
        }
    }

    /**
     * Downloads an image by its ID.
     *
     * @param imageId The ID of the image to be downloaded.
     * @return DownloadImageData containing a resource and image details.
     * @throws ImageDoesNotBelongToUserException If the image does not belong to the specified user.
     */
    public DownloadImageData downloadImage(int imageId) throws ImageDoesNotBelongToUserException {
        // Check if the image exists.
        boolean isImageExisting = doesImageExist(imageId);
        // Retrieve the image by its ID.
        Image image = getImageById(imageId);

        if (isImageExisting) {
            // Create a ByteArrayResource for the image data and return the DownloadImageData.
            ByteArrayResource resource = new ByteArrayResource(image.getData());
            return new DownloadImageData(resource, image);
        } else {
            throw new ImageDoesNotBelongToUserException("Image does not exist");
        }
    }

    /**
     * Retrieves an image by its ID.
     *
     * @param id The ID of the image.
     * @return The retrieved image or null if not found.
     */
    private Image getImageById(int id) {
        return imageRepository.findById(id).orElse(null);
    }

    /**
     * Checks if an image belongs to a specific user.
     *
     * @param userId  The ID of the user.
     * @param imageId The ID of the image.
     * @return true if the image belongs to the user, false otherwise.
     */
    public boolean isImageBelongingToUser(int userId, int imageId) {
        return imageRepository.existsByFolderUserIdAndImageId(userId, imageId);
    }

    /**
     * Checks if an image with a specific ID exists.
     *
     * @param imageId The ID of the image.
     * @return true if the image exists, false otherwise.
     */
    public boolean doesImageExist(int imageId) {
        return imageRepository.existsById(imageId);
    }
}
