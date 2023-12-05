package me.code.individual_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.code.individual_assignment.controller.UserController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



/**
 * The RegistrationTest class contains integration tests for user registration.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class RegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Cleans up the database by deleting entries from the "users" table before each test.
     */
    @BeforeEach
    public void CleanupDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    /**
     * Tests user registration by making a POST request to the "/register" endpoint.
     * Verifies that the registration is successful and returns the expected user JSON.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testRegistrationInTestDatabase() throws Exception {
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

}



