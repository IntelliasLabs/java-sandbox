package com.intellias.parking.controller.json;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@JsonTest
public class ParkingSpaceJsonTest {

    @Autowired
    private JacksonTester<ParkingSpaceDTO> json;

    @Test
    void testSerialize() throws Exception {
        ParkingSpaceDTO parkingSpace = new ParkingSpaceDTO(1L, "Space 1", "Location 1");
        JsonContent<ParkingSpaceDTO> jsonContent = json.write(parkingSpace);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(parkingSpace.getId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(parkingSpace.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("@.location")
                .isEqualTo(parkingSpace.getLocation());
    }

    @Test
    void testDeserialize() throws Exception {
        ParkingSpaceDTO parkingSpace = new ParkingSpaceDTO(1L, "Space 1", "Location 1");

        var content = """
                {
                    "id": 1,
                    "name": "Space 1",
                    "location": "Location 1"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(parkingSpace);
    }

}
