package com.intellias.parking.service.space;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.persistence.entity.booking.Booking;
import com.intellias.parking.persistence.entity.space.ParkingSpace;
import com.intellias.parking.persistence.entity.user.User;
import com.intellias.parking.persistence.repository.BookingRepository;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import com.intellias.parking.service.exception.RecordNotFoundException;
import jakarta.validation.ValidationException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class ParkingSpaceServiceTest {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private BookingRepository bookingRepository;

    private List<ParkingSpaceDTO> parkingSpaces;

    private Booking booking;

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

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(1L);
        parkingSpace.setName("Space1");
        parkingSpace.setLocation("Location1");

        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("password1");
        user.setEmail("user1@example.com");
        user.setPhoneNumber("1234567890");

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusDays(1);
        LocalDateTime endTime = startTime.plusDays(2);

        booking = new Booking();
        booking.setParkingSpace(parkingSpace);
        booking.setUser(user);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
    }

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getAllParkingSpaces_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresent() {
        List<ParkingSpaceDTO> actualParkingSpaces = parkingSpaceService.getAllParkingSpaces();

        assertEquals(parkingSpaces, actualParkingSpaces);
    }

    @Test
    public void getAllParkingSpacesByLocation_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresentForSpecifiedLocation() {
        List<ParkingSpaceDTO> expectedParkingSpaces =
                new ArrayList<>(List.of(parkingSpaces.get(0), parkingSpaces.get(1), parkingSpaces.get(2), parkingSpaces.get(3)));
        List<ParkingSpaceDTO> actualParkingSpaces = parkingSpaceService.getAllParkingSpacesByLocation("Location1");

        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    public void getAllParkingSpacesByLocation_ShouldReturnEmptyList_WhenParkingSpacesAreNotPresentForSpecifiedLocation() {
        assertEquals(0, parkingSpaceService.getAllParkingSpacesByLocation("UnknownLocation").size());
    }

    @Test
    @Transactional
    public void getAllAvailableParkingSpaces_ShouldReturnParkingSpacesList_WhenAllParkingSpacesAreAvailable() {
        List<ParkingSpaceDTO> actualParkingSpaces = parkingSpaceService.getAllAvailableParkingSpaces();

        assertEquals(parkingSpaces, actualParkingSpaces);
    }

    @Test
    @Transactional
    public void getAllAvailableParkingSpaces_ShouldReturnParkingSpacesList_WhenOneParkingSpaceWasBooked() {
        bookingRepository.save(booking);

        int actualParkingSpacesNumber = parkingSpaceService.getAllAvailableParkingSpaces().size();
        int expectedParkingSpacesNumber = parkingSpaces.size() - 1;

        assertEquals(expectedParkingSpacesNumber, actualParkingSpacesNumber);
    }

    @Test
    @Transactional
    public void getAvailableParkingSpacesByLocation_ShouldReturnParkingSpacesList_WhenAllParkingSpacesAreAvailableForSpecifiedLocation() {
        List<ParkingSpaceDTO> expectedParkingSpaces =
                new ArrayList<>(List.of(parkingSpaces.get(0), parkingSpaces.get(1), parkingSpaces.get(2), parkingSpaces.get(3)));
        List<ParkingSpaceDTO> actualParkingSpaces = parkingSpaceService.getAvailableParkingSpacesByLocation("Location1");

        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    @Transactional
    public void getAvailableParkingSpacesByLocation_ShouldReturnEmptyList_WhenNoParkingSpacesAvailableForSpecifiedLocation() {
        assertEquals(0, parkingSpaceService.getAvailableParkingSpacesByLocation("UnknownLocation").size());
    }

    @Test
    @Transactional
    public void getAvailableParkingSpacesByLocation_ShouldReturnParkingSpacesList_WhenOneParkingSpaceWasBookedForSpecifiedLocation() {
        bookingRepository.save(booking);

        int actualParkingSpacesNumber = parkingSpaceService.getAvailableParkingSpacesByLocation("Location1").size();
        int expectedParkingSpacesNumber = 3;

        assertEquals(expectedParkingSpacesNumber, actualParkingSpacesNumber);
    }

    @Test
    @Transactional
    public void getAvailableParkingSpacesByLocationForPeriod_ShouldReturnParkingSpacesList_WhenAllParkingSpacesAreAvailableForSpecifiedLocationAndPeriod() {
        List<ParkingSpaceDTO> expectedParkingSpaces =
                new ArrayList<>(List.of(parkingSpaces.get(0), parkingSpaces.get(1), parkingSpaces.get(2), parkingSpaces.get(3)));
        List<ParkingSpaceDTO> actualParkingSpaces = parkingSpaceService.getAvailableParkingSpacesByLocationForPeriod("Location1", LocalDateTime.now(), LocalDateTime.now().plusHours(4L));

        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    @Transactional
    public void getAvailableParkingSpacesByLocationForPeriod_ShouldReturnParkingSpacesList_WhenOneParkingSpaceWasBookedForSpecifiedLocation() {
        bookingRepository.save(booking);

        int actualParkingSpacesNumber = parkingSpaceService.getAvailableParkingSpacesByLocationForPeriod("Location1",
                LocalDateTime.now(), LocalDateTime.now().plusHours(4L)).size();
        int expectedParkingSpacesNumber = 3;

        assertEquals(expectedParkingSpacesNumber, actualParkingSpacesNumber);
    }

    @Test
    public void getAvailableParkingSpacesByLocationForPeriod_ShouldThrowException_WhenStartTimeIsLaterThanEndTime() {
        InvalidQueryParametersException exception =
                assertThrows(InvalidQueryParametersException.class, () -> parkingSpaceService.getAvailableParkingSpacesByLocationForPeriod("Location1",
                        LocalDateTime.now(), LocalDateTime.now().minusDays(1L)));

        String expectedMessage = "startTime cannot be later that endTime.";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void getParkingSpaceById_ShouldReturnParkingSpace_WhenThereIsParkingSpaceWithSpecifiedId() {
        ParkingSpaceDTO expectedParkingSpace = parkingSpaces.get(0);
        ParkingSpaceDTO actualParkingSpace = parkingSpaceService.getParkingSpaceById(1L);

        assertEquals(expectedParkingSpace, actualParkingSpace);
    }

    @Test
    public void getParkingSpaceById_ShouldThrowException_WhenThereIsNoParkingSpaceWithSpecifiedId() {
        assertThrows(RecordNotFoundException.class, () -> parkingSpaceService.getParkingSpaceById(100L));
    }

}