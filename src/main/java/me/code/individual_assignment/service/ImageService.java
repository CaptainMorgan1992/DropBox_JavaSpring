package me.code.individual_assignment.service;

import me.code.individual_assignment.DownloadImageData.DownloadImageData;
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

@Service
public class ImageService {

    private ImageRepository imageRepository;
    private FolderRepository folderRepository;
    private UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public DownloadImageData uploadImage(MultipartFile file, int userId, int folderId)
            throws ImageSizeTooLargeException, ByteConversionException, FailedImageUploadException {
        try {
            if (isValidImageSize(file)) {
                Image image;
                Folder folder;
                try {
                    folder = findFolder(userId, folderId);
                    image = new Image(file, folder);
                } catch (Exception e) {
                    throw new ByteConversionException("Failed to convert image to bytes");
                }
                folder.getImages().add(image);
                imageRepository.save(image);
                ByteArrayResource resource = new ByteArrayResource(image.getData());
                return new DownloadImageData(resource, image);


            } else {
                throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
            }
        } catch (Exception e) {
            throw new FailedImageUploadException("Could not upload image: " + e.getMessage());
        }
    }

    public boolean isValidImageSize(MultipartFile file) {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        return imageSize <= maxSize;
    }

    public Folder findFolder(int userId, int folderId) throws FolderNotFoundException {
        boolean isFolderPresent = folderRepository.existsByUserIdAndFolderId(userId, folderId);

        if(isFolderPresent) {
            Optional<Folder> folder = folderRepository.findById(folderId);
            return folder.get();
        }
        else {
            throw new FolderNotFoundException("Folder not found for folderId: " + folderId);
        }
    }

    public String deleteImage(int userId, int imageId) throws ImageDoesNotBelongToUserException {

        try {
            boolean isImageExisting = doesImageExist(imageId);
            boolean doesImageBelongToUser = isImageBelongingToUser(userId, imageId);

            if (isImageExisting && doesImageBelongToUser) {
                imageRepository.deleteById(imageId);
                return "Image deleted successfully";
            } else {
                throw new ImageDoesNotBelongToUserException("Image does not exist");
            }
        } catch (Exception e) {
            throw new ImageDoesNotBelongToUserException("Image could not be deleted. The image either does not belong to the user who's trying to delete the image, or the image does not exist.");
        }
    }

    public DownloadImageData downloadImage(int imageId) throws ImageDoesNotBelongToUserException {
            boolean isImageExisting = doesImageExist(imageId);
            //boolean doesImageBelongToUser = isImageBelongingToUser(userId, imageId);
            Image image = getImageById(imageId);

            if (isImageExisting) {
                ByteArrayResource resource = new ByteArrayResource(image.getData());

                return new DownloadImageData(resource, image);
            } else {
                    throw new ImageDoesNotBelongToUserException("Image does not exist");
            }
    }

    private Image getImageById(int id) {
        return imageRepository.findById(id).orElse(null);
    }

    public boolean isImageBelongingToUser(int userId, int imageId) {
        return imageRepository.existsByFolderUserIdAndImageId(userId, imageId);
    }

    public boolean doesImageExist(int imageId) {
        return imageRepository.existsById(imageId);
    }
}
