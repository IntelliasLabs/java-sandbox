package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.user.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void findAll_ShouldReturnUsersList_WhenUsersArePresent() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setUsername("user1");
        firstUser.setPassword("password1");
        firstUser.setEmail("user1@example.com");
        firstUser.setPhoneNumber("1234567890");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setUsername("user2");
        secondUser.setPassword("password2");
        secondUser.setEmail("user2@example.com");
        secondUser.setPhoneNumber("9876543210");

        List<User> expectedUsers = new ArrayList<>(List.of(firstUser, secondUser));
        List<User> actualUsers = userRepository.findAll();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void findAll_ShouldReturnEmptyList_WhenUsersAreNotPresent() {
        userRepository.deleteAll();
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void findById_ShouldReturnUser_WhenUserWithSpecifiedIdIsPresent() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("user1");
        expectedUser.setPassword("password1");
        expectedUser.setEmail("user1@example.com");
        expectedUser.setPhoneNumber("1234567890");

        User actualUser = userRepository.findById(1L).get();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void findById_ShouldReturnEmptyOptional_WhenUserWithSpecifiedIdIsNotPresent() {
        assertEquals(Optional.empty(), userRepository.findById(100L));
    }

}
