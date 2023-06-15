package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import com.intellias.parking.persistence.repository.ParkingSpacesRepository;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.intellias.parking.utils.TestUtils.asJsonStringWithTimeModule;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
class ParkingSpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingSpacesRepository repository;

    private List<ParkingSpaceDTO> parkingSpaces;

    private RequestBookingDTO requestBooking;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    public void setUp() {
        parkingSpaces = new ArrayList<>();
        parkingSpaces.add(new ParkingSpaceDTO(1L, "Space1", "Location1"));
        parkingSpaces.add(new ParkingSpaceDTO(2L, "Space2", "Location1"));
        parkingSpaces.add(new ParkingSpaceDTO(3L, "Space3", "Location1"));
        parkingSpaces.add(new ParkingSpaceDTO(4L, "Space4", "Location1"));
        parkingSpaces.add(new ParkingSpaceDTO(5L, "Space1", "Location2"));
        parkingSpaces.add(new ParkingSpaceDTO(6L, "Space2", "Location2"));
        parkingSpaces.add(new ParkingSpaceDTO(7L, "Space3", "Location2"));
        parkingSpaces.add(new ParkingSpaceDTO(8L, "Space4", "Location2"));
        parkingSpaces.add(new ParkingSpaceDTO(9L, "Space1", "Location3"));
        parkingSpaces.add(new ParkingSpaceDTO(10L, "Space2", "Location3"));
        parkingSpaces.add(new ParkingSpaceDTO(11L, "Space3", "Location3"));
        parkingSpaces.add(new ParkingSpaceDTO(12L, "Space4", "Location3"));

        LocalDateTime startTime = LocalDateTime.now().plusSeconds(3);
        LocalDateTime endTime = startTime.plusDays(1);

        requestBooking = new RequestBookingDTO();
        requestBooking.setParkingSpaceId(9L);
        requestBooking.setUserId(1L);
        requestBooking.setStartTime(startTime);
        requestBooking.setEndTime(endTime);
    }

    @Test
    public void getAllParkingSpaces_ShouldReturn200_WhenParkingSpacesArePresent() throws Exception {
        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(parkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(parkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(parkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(parkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(parkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[10].name", equalTo(parkingSpaces.get(10).getName())))
                .andExpect(jsonPath("$[10].location", equalTo(parkingSpaces.get(10).getLocation())))
                .andExpect(jsonPath("$[11].name", equalTo(parkingSpaces.get(11).getName())))
                .andExpect(jsonPath("$[11].location", equalTo(parkingSpaces.get(11).getLocation())));
    }

    @Test
    public void getAllParkingSpaces_ShouldReturn200_WhenNoParkingSpacesArePresent() throws Exception {
        repository.deleteAll();

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllParkingSpacesByLocation_ShouldReturn200_WhenParkingSpacesArePresentForSpecifiedLocation() throws Exception {
        List<ParkingSpaceDTO> expectedParkingSpaces = new ArrayList<>();
        expectedParkingSpaces.add(parkingSpaces.get(0));
        expectedParkingSpaces.add(parkingSpaces.get(1));
        expectedParkingSpaces.add(parkingSpaces.get(2));
        expectedParkingSpaces.add(parkingSpaces.get(3));

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH + "/location/Location1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedParkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(expectedParkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(expectedParkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(expectedParkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(expectedParkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[2].name", equalTo(expectedParkingSpaces.get(2).getName())))
                .andExpect(jsonPath("$[2].location", equalTo(expectedParkingSpaces.get(2).getLocation())))
                .andExpect(jsonPath("$[3].name", equalTo(expectedParkingSpaces.get(3).getName())))
                .andExpect(jsonPath("$[3].location", equalTo(expectedParkingSpaces.get(3).getLocation())));
    }

    @Test
    public void getAllParkingSpacesByLocation_ShouldReturn200_WhenParkingSpacesAreNotPresentForSpecifiedLocation() throws Exception {
        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH + "/location/UnknownLocation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAvailableParkingSpaces_ShouldReturn200_WhenAllParkingSpacesAreAvailable() throws Exception {
        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH + "/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(parkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(parkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(parkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(parkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(parkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[10].name", equalTo(parkingSpaces.get(10).getName())))
                .andExpect(jsonPath("$[10].location", equalTo(parkingSpaces.get(10).getLocation())))
                .andExpect(jsonPath("$[11].name", equalTo(parkingSpaces.get(11).getName())))
                .andExpect(jsonPath("$[11].location", equalTo(parkingSpaces.get(11).getLocation())));
    }

    @Test
    public void getAvailableParkingSpaces_ShouldReturn200_WhenOneParkingSpaceWasBooked() throws Exception {
        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(requestBooking)))
                .andExpect(status().isOk());

        Thread.sleep(5000);

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH + "/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(parkingSpaces.size() - 1)));
    }

    @Test
    public void getAvailableParkingSpacesByLocation_ShouldReturn200_WhenTimePeriodIsNotSpecifiedAndAllParkingSpacesAreAvailableForSpecifiedLocation() throws Exception {
        List<ParkingSpaceDTO> expectedParkingSpaces = new ArrayList<>();
        expectedParkingSpaces.add(parkingSpaces.get(4));
        expectedParkingSpaces.add(parkingSpaces.get(5));
        expectedParkingSpaces.add(parkingSpaces.get(6));
        expectedParkingSpaces.add(parkingSpaces.get(7));

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH + "/available/location/Location2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedParkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(expectedParkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(expectedParkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(expectedParkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(expectedParkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[2].name", equalTo(expectedParkingSpaces.get(2).getName())))
                .andExpect(jsonPath("$[2].location", equalTo(expectedParkingSpaces.get(2).getLocation())))
                .andExpect(jsonPath("$[3].name", equalTo(expectedParkingSpaces.get(3).getName())))
                .andExpect(jsonPath("$[3].location", equalTo(expectedParkingSpaces.get(3).getLocation())));
    }

    @Test
    public void getAvailableParkingSpacesByLocation_ShouldReturn200_WhenAllParkingSpacesAreAvailableForSpecifiedTimePeriodAndLocation() throws Exception {
        List<ParkingSpaceDTO> expectedParkingSpaces = new ArrayList<>();
        expectedParkingSpaces.add(parkingSpaces.get(4));
        expectedParkingSpaces.add(parkingSpaces.get(5));
        expectedParkingSpaces.add(parkingSpaces.get(6));
        expectedParkingSpaces.add(parkingSpaces.get(7));

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH
                        + "/available/location/Location2?startTime=2023-07-30T11:00:00&endTime=2023-07-30T12:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedParkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(expectedParkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(expectedParkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(expectedParkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(expectedParkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[2].name", equalTo(expectedParkingSpaces.get(2).getName())))
                .andExpect(jsonPath("$[2].location", equalTo(expectedParkingSpaces.get(2).getLocation())))
                .andExpect(jsonPath("$[3].name", equalTo(expectedParkingSpaces.get(3).getName())))
                .andExpect(jsonPath("$[3].location", equalTo(expectedParkingSpaces.get(3).getLocation())));
    }

    @Test
    public void getAvailableParkingSpacesByLocation_ShouldReturn200_WhenSomeParkingSpaceWasBookedForSpecifiedTimePeriodAndLocation() throws Exception {
        mockMvc.perform(post(BookingController.API_VERSION + BookingController.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonStringWithTimeModule(requestBooking)))
                .andExpect(status().isOk());

        List<ParkingSpaceDTO> expectedParkingSpaces = new ArrayList<>();
        expectedParkingSpaces.add(parkingSpaces.get(9));
        expectedParkingSpaces.add(parkingSpaces.get(10));
        expectedParkingSpaces.add(parkingSpaces.get(11));

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH
                        + "/available/location/Location3?"
                        + String.format("startTime=%s&endTime=%s", requestBooking.getStartTime(), requestBooking.getEndTime()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedParkingSpaces.size())))
                .andExpect(jsonPath("$[0].name", equalTo(expectedParkingSpaces.get(0).getName())))
                .andExpect(jsonPath("$[0].location", equalTo(expectedParkingSpaces.get(0).getLocation())))
                .andExpect(jsonPath("$[1].name", equalTo(expectedParkingSpaces.get(1).getName())))
                .andExpect(jsonPath("$[1].location", equalTo(expectedParkingSpaces.get(1).getLocation())))
                .andExpect(jsonPath("$[2].name", equalTo(expectedParkingSpaces.get(2).getName())))
                .andExpect(jsonPath("$[2].location", equalTo(expectedParkingSpaces.get(2).getLocation())));
    }

    @Test
    public void getAvailableParkingSpacesByLocation_ShouldReturn200_WhenNoParkingSpacesAvailableForSpecifiedTimePeriodAndLocation() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(3);
        LocalDateTime endTime = startTime.plusDays(1);

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH
                        + "/available/location/UnknownLocation?"
                        + String.format("startTime=%s&endTime=%s", startTime, endTime))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAvailableParkingSpacesByLocation_ShouldReturn400_WhenStartTimeIsLaterThanEndTime() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(3);
        LocalDateTime endTime = startTime.minusDays(1);

        mockMvc.perform(get(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH
                        + "/available/location/UnknownLocation?"
                        + String.format("startTime=%s&endTime=%s", startTime, endTime))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("startTime cannot be later that endTime.")));
    }

}