package com.intellias.parking.controller.json.booking;

import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@JsonTest
public class RequestBookingJsonTest {

    @Autowired
    private JacksonTester<RequestBookingDTO> json;

    private RequestBookingDTO requestBooking;

    @BeforeEach
    public void init() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.of(2023, 7, 30), LocalTime.of(10, 0, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.of(2023, 7, 30), LocalTime.of(12, 0, 0));

        requestBooking = new RequestBookingDTO(1L, 1L, startTime, endTime);
    }

    @Test
    void testSerialize() throws Exception {
        JsonContent<RequestBookingDTO> jsonContent = json.write(requestBooking);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.parkingSpaceId")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(requestBooking.getParkingSpaceId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathNumberValue("@.userId")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(requestBooking.getUserId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.startTime")
                .isEqualTo(requestBooking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(jsonContent).extractingJsonPathStringValue("@.endTime")
                .isEqualTo(requestBooking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "parkingSpaceId": 1,
                    "userId": 1,
                    "startTime": "2023-07-30T10:00:00",
                    "endTime": "2023-07-30T12:00:00"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(requestBooking);
    }

}
