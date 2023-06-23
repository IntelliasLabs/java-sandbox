package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.persistence.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@JsonTest
public class ParkingSpaceJsonTests {

    @Autowired
    private JacksonTester<ParkingSpaceDTO> json;

    @Test
    public void testSerialize() throws Exception {
        ParkingSpaceDTO parkingSpaceDTO = new ParkingSpaceDTO(1, "First parking space", Status.AVAILABLE);
        JsonContent<ParkingSpaceDTO> jsonContent = json.write(parkingSpaceDTO);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(parkingSpaceDTO.getId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(parkingSpaceDTO.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(parkingSpaceDTO.getStatus().toString());
    }

    @Test
    public void testDeserialize() throws Exception {
        var content = """
                {
                    "id": "1",
                    "name": "First parking space",
                    "status": "AVAILABLE"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ParkingSpaceDTO(1, "First parking space", Status.AVAILABLE));
    }

}
