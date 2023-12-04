package me.code.individual_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.code.individual_assignment.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class RegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testRegistrationInTestDatabase() throws Exception {

        //Arrange
        var username = "test2";
        var password = "test2";

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
}



