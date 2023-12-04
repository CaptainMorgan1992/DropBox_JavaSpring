package me.code.individual_assignment;


import jakarta.transaction.Transactional;
import me.code.individual_assignment.DownloadImageData.DownloadImageData;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.repository.ImageRepository;
import me.code.individual_assignment.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class UploadFileTest {

    @Autowired
    private ImageService imageService; // Inject your image upload service

    @Autowired
    private ImageRepository imageRepository; // Inject your image repository

/*
    @Test
    @Transactional
    public void testImageUploadToRepository() throws Exception {
        // Arrange
        String imageFileName = "kavaj.jpg";
        Path imagePath = Paths.get("src/main/java/me/code/individual_assignment/utility", imageFileName);
        byte[] imageData = Files.readAllBytes(imagePath);

        MultipartFile multipartFile = new MockMultipartFile("file", imageFileName, MimeTypeUtils.IMAGE_JPEG_VALUE, imageData);

        int userId = 1;
        int folderId = 3;

        // Act
       DownloadImageData uploadedImage = imageService.uploadImage(multipartFile, userId, folderId);


            // Assert
            assertNotNull(uploadedImagePath);
                    }

 */

}





