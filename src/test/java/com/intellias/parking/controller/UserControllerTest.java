package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.persistence.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getAllUsers_ShouldReturn200_WhenNoUsersArePresent() throws Exception {
        repository.deleteAll();

        mockMvc.perform(get(UserController.API_VERSION + UserController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllUsers_ShouldReturn200_WhenUsersArePresent() throws Exception {
        List<UserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(new UserDTO(1L, "user1@example.com", "1234567890"));
        userDTOs.add(new UserDTO(2L, "user2@example.com", "9876543210"));

        mockMvc.perform(get(UserController.API_VERSION + UserController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userDTOs.size())))
                .andExpect(jsonPath("$[0].email", equalTo(userDTOs.get(0).getEmail())))
                .andExpect(jsonPath("$[0].phoneNumber", equalTo(userDTOs.get(0).getPhoneNumber())))
                .andExpect(jsonPath("$[1].email", equalTo(userDTOs.get(1).getEmail())))
                .andExpect(jsonPath("$[1].phoneNumber", equalTo(userDTOs.get(1).getPhoneNumber())));
    }

}