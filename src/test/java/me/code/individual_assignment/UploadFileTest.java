package me.code.individual_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.code.individual_assignment.DownloadImageData.DownloadImageData;
import me.code.individual_assignment.controller.UserController;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.model.User;
import me.code.individual_assignment.repository.FolderRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The UploadFileTest class contains integration tests for uploading files.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class UploadFileTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Cleans up the database by deleting entries from the "images," "folders," and "users" tables.
     */
    @Test
    public void cleanupDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images", "folders", "users");
    }

    /**
     * Tests user registration by making a POST request to the "/register" endpoint.
     * Verifies that the registration is successful and returns the expected user JSON.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testRegistrationInDatabase() throws Exception {
        // Arrange
        var username = "test1";
        var password = "test1";

        var dto = new UserController.UserDTO(username, password);
        var json = mapper.writeValueAsString(dto);

        // Act
        var builder = MockMvcRequestBuilders.post("/register")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        // Assert
        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", org.hamcrest.Matchers.is(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", org.hamcrest.Matchers.is(password)));
    }

    /**
     * Tests creating a folder in the database using the {@link FolderService}.
     * Verifies that the created folder has the correct name and is associated with the correct user.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testCreateFolderInDatabase() throws Exception {
        // Arrange
        String folderName = "A test folder";
        User user = userRepository.findByUsername("test1");
        int userId = user.getId();

        // Act
        Folder folder = folderService.createNewFolder(userId, folderName);

        // Assert
        assertEquals(folderName, folder.getName());
        assertEquals(userId, folder.getUser().getId());
    }

    /**
     * Tests uploading an image to the repository using the {@link ImageService}.
     * Verifies that the uploaded image has the correct file name, size, and content type.
     *
     * @throws Exception If an error occurs during the test.
     */
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
}





