package com.intellias.parking.controller.json.booking;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.dto.UserDTO;
import com.intellias.parking.controller.dto.booking.BookingStatus;
import com.intellias.parking.controller.dto.booking.RequestBookingSummaryDTO;
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
public class RequestBookingSummaryJsonTest {

    @Autowired
    private JacksonTester<RequestBookingSummaryDTO> json;

    private RequestBookingSummaryDTO requestBookingSummary;
    private ParkingSpaceDTO parkingSpace;
    private UserDTO user;

    @BeforeEach
    public void init() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.of(2023, 7, 30), LocalTime.of(10, 0, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.of(2023, 7, 30), LocalTime.of(12, 0, 0));
        BookingStatus status = BookingStatus.SUCCESSFUL;

        parkingSpace = new ParkingSpaceDTO(1L, "Space 1", "Location 1");
        user = new UserDTO(1L, "user@mail.com", "000-000-000");
        requestBookingSummary = new RequestBookingSummaryDTO(parkingSpace, user, startTime, endTime, status);
    }


    @Test
    void testSerialize() throws Exception {
        JsonContent<RequestBookingSummaryDTO> jsonContent = json.write(requestBookingSummary);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.parkingSpace.id")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(parkingSpace.getId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.parkingSpace.name")
                .isEqualTo(parkingSpace.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("@.parkingSpace.location")
                .isEqualTo(parkingSpace.getLocation());

        assertThat(jsonContent).extractingJsonPathNumberValue("@.user.id")
                .satisfies(number -> assertThat(number.longValue()).isCloseTo(user.getId(), within(0L)));
        assertThat(jsonContent).extractingJsonPathStringValue("@.user.email")
                .isEqualTo(user.getEmail());
        assertThat(jsonContent).extractingJsonPathStringValue("@.user.phoneNumber")
                .isEqualTo(user.getPhoneNumber());

        assertThat(jsonContent).extractingJsonPathStringValue("@.startTime")
                .isEqualTo(requestBookingSummary.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(jsonContent).extractingJsonPathStringValue("@.endTime")
                .isEqualTo(requestBookingSummary.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(jsonContent).extractingJsonPathStringValue("@.bookingStatus")
                .isEqualTo(requestBookingSummary.getBookingStatus().toString());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "parkingSpace": {
                      "id": 1,
                      "name": "Space 1",
                      "location": "Location 1"
                    },
                    "user": {
                      "id": 1,
                      "email": "user@mail.com",
                      "phoneNumber": "000-000-000"
                    },
                    "startTime": "2023-07-30T10:00:00",
                    "endTime": "2023-07-30T12:00:00",
                    "bookingStatus": "SUCCESSFUL"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(requestBookingSummary);
    }

}
