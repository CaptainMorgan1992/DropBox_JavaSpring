package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.model.Folder;
import com.example.me.code.individual_assignment.model.Image;
import com.example.me.code.individual_assignment.repository.FolderRepository;
import com.example.me.code.individual_assignment.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImageService {

    private ImageRepository imageRepository;
    private FolderRepository folderRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, FolderRepository folderRepository) {
        this.imageRepository = imageRepository;
        this.folderRepository = folderRepository;
    }

    public String uploadImage(MultipartFile file, int userId) {
        try {
            if (isValidImageSize(file)) {
                Image image;
                try {
                    Folder folder = findFolder(userId);
                    image = new Image(file, folder);
                } catch (Exception e) {
                    throw new ByteConversionException("Failed to convert image to bytes");
                }
                imageRepository.save(image);

                return "Uploaded successfully";
            } else {
                throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
            }
        } catch (Exception e) {
            throw new FailedImageUploadException("Could not upload image: " + e.getMessage());
        }
    }

    public boolean isValidImageSize(MultipartFile file) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        return imageSize <= maxSize;
    }

    private Folder findFolder(int userId) {
        Optional<Folder> folder = folderRepository.findByUserId(userId);
        return folder.orElse(null);
    }
}
