package com.intellias.parking.controller.json;

import com.intellias.parking.controller.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@JsonTest
public class UserJsonTest {

    @Autowired
    private JacksonTester<UserDTO> json;

    @Test
    void testSerialize() throws Exception {
        UserDTO user = new UserDTO(1L, "user@mail.com", "000-000-000");
        JsonContent<UserDTO> jsonContent = json.write(user);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(user.getId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.email")
                .isEqualTo(user.getEmail());
        assertThat(jsonContent).extractingJsonPathStringValue("@.phoneNumber")
                .isEqualTo(user.getPhoneNumber());
    }

    @Test
    void testDeserialize() throws Exception {
        UserDTO user = new UserDTO(1L, "user@mail.com", "000-000-000");

        var content = """
                {
                    "id": 1,
                    "email": "user@mail.com",
                    "phoneNumber": "000-000-000"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

}
