package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.intellias.parking.utils.TestUtils.asJsonStringWithTimeModule;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private RequestBookingDTO validRequestBooking;

    @BeforeEach
    public void setUp() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);

        validRequestBooking = new RequestBookingDTO();
        validRequestBooking.setParkingSpaceId(9L);
        validRequestBooking.setUserId(1L);
        validRequestBooking.setStartTime(startTime);
        validRequestBooking.setEndTime(endTime);
    }

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void requestBooking_ShouldReturn200_WhenBookingIsSuccessful() throws Exception {
        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(validRequestBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parkingSpace.id", equalTo(Math.toIntExact(validRequestBooking.getParkingSpaceId()))))
                .andExpect(jsonPath("$.user.id", equalTo(Math.toIntExact(validRequestBooking.getUserId()))))
                .andExpect(jsonPath("$.bookingStatus", equalTo("SUCCESSFUL")))
                .andExpect(jsonPath("$.startTime", equalTo(validRequestBooking.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.endTime", equalTo(validRequestBooking.getEndTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    public void requestBooking_ShouldReturn404_WhenParkingSpaceWithSpecifiedIdDoesNotExist() throws Exception {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setParkingSpaceId(100L);

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(invalidRequestBooking)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.message", equalTo("parking_space with ID 100 not found.")));
    }

    @Test
    public void requestBooking_ShouldReturn404_WhenUserWithSpecifiedIdDoesNotExist() throws Exception {
        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setUserId(100L);

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(invalidRequestBooking)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.message", equalTo("user with ID 100 not found.")));
    }

    @Test
    public void requestBooking_ShouldReturn400_WhenSpecifiedParkingTimeIsInThePast() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusDays(10);
        LocalDateTime endTime = startTime.plusDays(1);

        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setStartTime(startTime);
        invalidRequestBooking.setEndTime(endTime);

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(invalidRequestBooking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("startTime cannot be in the past.")));
    }

    @Test
    public void requestBooking_ShouldReturn400_WhenParkingSpaceIsAlreadyBookedForSpecifiedTimeSlot() throws Exception {
        String errorMessage = String.format("Parking space with ID %d on location Location3 not available from %s until %s.",
                validRequestBooking.getParkingSpaceId(), validRequestBooking.getStartTime(), validRequestBooking.getEndTime());

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(validRequestBooking)))
                .andExpect(status().isOk());

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(validRequestBooking)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", equalTo("CONFLICT")))
                .andExpect(jsonPath("$.message",
                        equalTo(errorMessage)));
    }

    @Test
    public void requestBooking_ShouldReturn400_WhenStartTimeIsLaterThanEndTime() throws Exception {
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1L);
        LocalDateTime startTime = endTime.plusDays(1);

        RequestBookingDTO invalidRequestBooking = validRequestBooking;
        invalidRequestBooking.setStartTime(startTime);
        invalidRequestBooking.setEndTime(endTime);

        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(invalidRequestBooking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("startTime cannot be later that endTime.")));
    }

}