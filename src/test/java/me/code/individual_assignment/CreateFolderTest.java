package me.code.individual_assignment;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.code.individual_assignment.controller.UserController;
import me.code.individual_assignment.model.Folder;
import me.code.individual_assignment.service.FolderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class CreateFolderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    FolderService folderService;

    @Test
    public void testCreateFolderInDatabase() throws Exception {

        //Arrange
        String folderName = "A test folder";


        //act
        Folder folder = folderService.createNewFolder(2, folderName);
        var json = mapper.writeValueAsString(folder);

        //assert
        assertEquals(folderName, folder.getName());
        assertEquals(2, folder.getUser().getId());
    }
}
