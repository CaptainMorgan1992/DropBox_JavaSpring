package me.code.individual_assignment;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.repository.ImageRepository;
import me.code.individual_assignment.service.FolderService;
import me.code.individual_assignment.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class DeleteImageTest {

    @Autowired
    private ImageService imageService; // Inject your image upload service

    @Autowired
    private ImageRepository imageRepository; // Inject your image repository

    @Autowired
    FolderService folderService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDeletionOfImage() {

        //Arrange
        int userId = 1;
        int imageId = 15;

        //Act
        imageService.deleteImage(userId, imageId);

        //Assert
        Optional<Image> deletedImage = imageRepository.findById(imageId);
        assertEquals(Optional.empty(), deletedImage);
    }
}
