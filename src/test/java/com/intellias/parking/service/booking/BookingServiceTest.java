package com.intellias.parking.service.booking;

import com.intellias.parking.controller.dto.booking.BookingStatus;
import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import com.intellias.parking.controller.dto.booking.RequestBookingSummaryDTO;
import com.intellias.parking.service.exception.ParkingSpaceNotAvailableException;
import com.intellias.parking.service.exception.RecordNotFoundException;
import jakarta.validation.ValidationException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    private RequestBookingDTO validRequestBooking;

    @BeforeEach
    public void setup() {
        Long userId = 1L;
        Long parkingSpaceId = 1L;
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);

        validRequestBooking = new RequestBookingDTO(userId, parkingSpaceId, startTime, endTime);
    }

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void requestBooking_ShouldReturnRequestBookingSummaryDTO_WhenBookingWasSuccessful() {
        RequestBookingSummaryDTO actualBookingSummary = bookingService.requestBooking(validRequestBooking);

        assertEquals(validRequestBooking.getUserId(), actualBookingSummary.getUser().getId());
        assertEquals(validRequestBooking.getParkingSpaceId(), actualBookingSummary.getParkingSpace().getId());
        assertEquals(validRequestBooking.getStartTime(), actualBookingSummary.getStartTime());
        assertEquals(validRequestBooking.getEndTime(), actualBookingSummary.getEndTime());
        assertEquals(BookingStatus.SUCCESSFUL, actualBookingSummary.getBookingStatus());
    }

    @Test
    public void requestBooking_ShouldThrowException_WhenStartTimeIsInThePast() {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.requestBooking(invalidRequestBooking));
        assertEquals("startTime cannot be in the past.", exception.getMessage());
    }

    @Test
    public void requestBooking_ShouldThrowException_WhenParkingSpaceWithSpecifiedIdDoesNotExist() {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setParkingSpaceId(100L);

        RecordNotFoundException exception =
                assertThrows(RecordNotFoundException.class, () -> bookingService.requestBooking(invalidRequestBooking));
        assertEquals("parking_space with ID 100 not found.", exception.getMessage());
    }

    @Test
    public void requestBooking_ShouldThrowException_WhenUserWithSpecifiedIdDoesNotExist() {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setUserId(100L);

        RecordNotFoundException exception =
                assertThrows(RecordNotFoundException.class, () -> bookingService.requestBooking(invalidRequestBooking));
        assertEquals("user with ID 100 not found.", exception.getMessage());
    }

    @Test
    public void requestBooking_ShouldThrowException_WhenParkingSpaceIsAlreadyBookedForSpecifiedTimeSlot() {
        bookingService.requestBooking(validRequestBooking);

        ParkingSpaceNotAvailableException exception =
                assertThrows(ParkingSpaceNotAvailableException.class, () -> bookingService.requestBooking(validRequestBooking));

        String expectedMessage = String.format("Parking space with ID %d on location Location1 not available from %s until %s.",
                validRequestBooking.getParkingSpaceId(), validRequestBooking.getStartTime(), validRequestBooking.getEndTime());

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void requestBooking_ShouldThrowException_WhenStartTimeIsLaterThanEndTime() {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setStartTime(validRequestBooking.getEndTime().plusHours(1L));

        ValidationException exception =
                assertThrows(ValidationException.class, () -> bookingService.requestBooking(validRequestBooking));

        String expectedMessage = "startTime cannot be later that endTime.";

        assertEquals(expectedMessage, exception.getMessage());
    }

}