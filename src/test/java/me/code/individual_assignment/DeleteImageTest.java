package me.code.individual_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.code.individual_assignment.DownloadImageData.DownloadImageData;
import me.code.individual_assignment.controller.UserController;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.Image;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.repository.FolderRepository;
import me.code.individual_assignment.repository.ImageRepository;
import me.code.individual_assignment.repository.UserRepository;
import me.code.individual_assignment.service.FolderService;
import me.code.individual_assignment.service.ImageService;
import me.code.individual_assignment.service.UserService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class DeleteImageTest {

    @Autowired
    ImageService imageService; // Inject your image upload service

    @Autowired
    FolderService folderService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

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
        User user = userRepository.findByUsername("test1");
        int userId = user.getId();

        //act
        Folder folder = folderService.createNewFolder(userId, folderName);

        //assert
        assertEquals(folderName, folder.getName());
        assertEquals(userId, folder.getUser().getId());
    }

    @Test
    @Transactional
    public void testImageUploadToRepository() throws Exception {

        // Arrange
        String imageFileName = "kavaj.jpg";
        Path imagePath = Paths.get("src/main/java/me/code/individual_assignment/utility", imageFileName);
        byte[] imageData = Files.readAllBytes(imagePath);

        MultipartFile multipartFile = new MockMultipartFile("file", imageFileName, MimeTypeUtils.IMAGE_JPEG_VALUE, imageData);

        User user = userRepository.findByUsername("test1");
        int userId = user.getId();
        int folderId = folderRepository.findByName("A test folder");

        // Act
        DownloadImageData uploadedImage = imageService.uploadImage(multipartFile, userId, folderId);

        TestTransaction.flagForCommit();
        TestTransaction.end();


        // Assert
        assertEquals(imageFileName, uploadedImage.getFileName());
        assertEquals(imageData.length, uploadedImage.getFileSize());
        assertEquals("image/jpeg", uploadedImage.getContentType());

    }



    @Test
    public void testDeletionOfImage() {

        //Arrange
        User user = userRepository.findByUsername("test1");
        int userId = user.getId();
        Image image = imageRepository.findByName("kavaj.jpg");
        int imageId = image.getId();

        //Act
        imageService.deleteImage(userId, imageId);

        //Assert
        Optional<Image> deletedImage = imageRepository.findById(imageId);
        assertEquals(Optional.empty(), deletedImage);
    }

    @Test
    public void cleanupDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images", "folders", "users");
    }


}
