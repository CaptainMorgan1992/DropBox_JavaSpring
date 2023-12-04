package me.code.individual_assignment;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.code.individual_assignment.DownloadImageData.DownloadImageData;
import me.code.individual_assignment.controller.UserController;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.repository.ImageRepository;
import me.code.individual_assignment.service.FolderService;
import me.code.individual_assignment.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class UploadFileTest {

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

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images", "folders", "users");
    }

    @Test
    public void testRegistrationInDatabase() throws Exception {

        //Arrange
        var username = "test1";
        var password = "test1";

        var dto = new UserController.UserDTO(username, password);
        var json = mapper.writeValueAsString(dto);

        //act
        var builder = MockMvcRequestBuilders.post("/register")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //assert
        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", org.hamcrest.Matchers.is(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", org.hamcrest.Matchers.is(password)));
    }

    @Test
    public void testCreateFolderInDatabase() throws Exception {

        //Arrange
        String folderName = "A test folder";
        int userId = 2;

        //act
        Folder folder = folderService.createNewFolder(userId, folderName);

        //assert
        assertEquals(folderName, folder.getName());
        assertEquals(2, folder.getUser().getId());
    }

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

        TestTransaction.flagForCommit();
        TestTransaction.end();


        // Assert
        assertEquals(imageFileName, uploadedImage.getFileName());
        assertEquals(imageData.length, uploadedImage.getFileSize());
        assertEquals("image/jpeg", uploadedImage.getContentType());

    }
}





