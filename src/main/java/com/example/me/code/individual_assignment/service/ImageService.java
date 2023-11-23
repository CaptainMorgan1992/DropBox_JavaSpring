package com.example.me.code.individual_assignment.service;

import com.example.me.code.individual_assignment.exceptions.ByteConversionException;
import com.example.me.code.individual_assignment.exceptions.FailedImageUploadException;
import com.example.me.code.individual_assignment.exceptions.FolderNotFoundException;
import com.example.me.code.individual_assignment.exceptions.ImageSizeTooLargeException;
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

    public String uploadImage(MultipartFile file, int userId, int folderId)
            throws ImageSizeTooLargeException, ByteConversionException, FailedImageUploadException {
        try {
            if (isValidImageSize(file)) {
                Image image;
                Folder folder;
                try {
                    folder = findFolder(userId, folderId);
                    System.out.println(folder);
                    image = new Image(file, folder);
                    image.setFolder(folder);
                    image.setData(file.getBytes());
                    image.setSize(file.getSize());
                    image.setName(file.getOriginalFilename());
                    imageRepository.save(image);
                    System.out.println(image);
                    return "uploaded successfully";
                } catch (Exception e) {
                    throw new ByteConversionException("Failed to convert image to bytes");
                }
            } else {
                throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
            }
        } catch (Exception e) {
            throw new FailedImageUploadException("Could not upload image: " + e.getMessage());
        }
    }

    public boolean isValidImageSize(MultipartFile file) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        System.out.println(imageSize);
        long maxSize = 2 * 1024 * 1024; // 2mb

        return imageSize <= maxSize;
    }

    private Folder findFolder(int userId, int folderId) throws FolderNotFoundException {
        boolean isFolderPresent = folderRepository.existsByUserIdAndFolderId(userId, folderId);
        System.out.println(isFolderPresent);

        if(isFolderPresent) {
            Optional<Folder> folder = folderRepository.findById(folderId);
            return folder.get();
        }
        else {
            throw new FolderNotFoundException("Folder not found for folderId: " + folderId);
        }
    }


}
