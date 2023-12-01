package me.code.individual_assignment;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class UploadFileTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testImageUpload() throws Exception {
        // Prepare a sample image file
        MockMultipartFile imageFile = new MockMultipartFile("image", "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE, "Some image data".getBytes());

        // Replace 1 with the actual folder ID
        int folderId = 1;

        // Perform a POST request to the /image/upload/{folderId} endpoint with the image file
        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/upload/" + folderId)
                        .file(imageFile)
                        .header("Authorization", "your-sample-token"))
                // Assert the expected HTTP status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Add more assertions based on your specific requirements
                .andExpect(MockMvcResultMatchers.content().json("{}"));
    }
}
