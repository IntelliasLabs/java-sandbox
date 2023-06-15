package com.intellias.parking.service.user;

import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.service.exception.RecordNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getAllUsers_ShouldReturnUsersList_WhenUsersArePresent() {
        UserDTO firstUser = new UserDTO();
        firstUser.setId(1L);
        firstUser.setEmail("user1@example.com");
        firstUser.setPhoneNumber("1234567890");

        UserDTO secondUser = new UserDTO();
        secondUser.setId(2L);
        secondUser.setEmail("user2@example.com");
        secondUser.setPhoneNumber("9876543210");

        List<UserDTO> expectedUsers = new ArrayList<>(List.of(firstUser, secondUser));
        List<UserDTO> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void getUserById_ShouldReturnUser_WhenUserWithSpecifiedIdIsPresent() {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId(1L);
        expectedUser.setEmail("user1@example.com");
        expectedUser.setPhoneNumber("1234567890");

        assertEquals(expectedUser, userService.getUserById(1L));
    }

    @Test
    public void getUserById_ShouldThrowException_WhenUserWithSpecifiedIdIsNotPresent() {
        assertThrows(RecordNotFoundException.class, () -> userService.getUserById(100L));
    }

}