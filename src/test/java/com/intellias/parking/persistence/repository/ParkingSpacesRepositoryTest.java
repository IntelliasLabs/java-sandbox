package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.space.ParkingSpace;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class ParkingSpacesRepositoryTest {

    @Autowired
    private ParkingSpacesRepository parkingSpacesRepository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void findAll_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresent() {
        ParkingSpace firstParkingSpace = new ParkingSpace();
        firstParkingSpace.setId(1L);
        firstParkingSpace.setName("Space1");
        firstParkingSpace.setLocation("Location1");

        ParkingSpace lastParkingSpace = new ParkingSpace();
        lastParkingSpace.setId(12L);
        lastParkingSpace.setName("Space4");
        lastParkingSpace.setLocation("Location3");

        List<ParkingSpace> parkingSpaces = parkingSpacesRepository.findAll();
        int actualListSize = parkingSpaces.size();
        int expectedListSize = 12;

        assertEquals(expectedListSize, actualListSize);
        assertEquals(firstParkingSpace, parkingSpaces.get(0));
        assertEquals(lastParkingSpace, parkingSpaces.get(11));
    }

    @Test
    public void findAll_ShouldReturnEmptyList_WhenParkingSpacesAreNotPresent() {
        parkingSpacesRepository.deleteAll();
        assertEquals(0, parkingSpacesRepository.findAll().size());
    }

    @Test
    public void findAllByLocation_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresentForSpecifiedLocation() {
        ParkingSpace firstParkingSpace = new ParkingSpace();
        firstParkingSpace.setId(1L);
        firstParkingSpace.setName("Space1");
        firstParkingSpace.setLocation("Location1");

        ParkingSpace secondParkingSpace = new ParkingSpace();
        secondParkingSpace.setId(2L);
        secondParkingSpace.setName("Space2");
        secondParkingSpace.setLocation("Location1");

        ParkingSpace thirdParkingSpace = new ParkingSpace();
        thirdParkingSpace.setId(3L);
        thirdParkingSpace.setName("Space3");
        thirdParkingSpace.setLocation("Location1");

        ParkingSpace fourthParkingSpace = new ParkingSpace();
        fourthParkingSpace.setId(4L);
        fourthParkingSpace.setName("Space4");
        fourthParkingSpace.setLocation("Location1");

        List<ParkingSpace> expectedParkingSpaces =
                new ArrayList<>(List.of(firstParkingSpace, secondParkingSpace, thirdParkingSpace, fourthParkingSpace));
        List<ParkingSpace> actualParkingSpaces = parkingSpacesRepository.findAllByLocation("Location1");

        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    public void findAllByLocation_ShouldReturnEmptyList_WhenParkingSpacesAreNotPresentForSpecifiedLocation() {
        assertEquals(0, parkingSpacesRepository.findAllByLocation("UnknownLocation").size());
    }

    @Test
    public void findById_ShouldReturnParkingSpace_WhenParkingSpaceWithSpecifiedIdIsPresent() {
        ParkingSpace expectedParkingSpace = new ParkingSpace();
        expectedParkingSpace.setId(1L);
        expectedParkingSpace.setName("Space1");
        expectedParkingSpace.setLocation("Location1");

        ParkingSpace actualParkingSpace = parkingSpacesRepository.findById(1L).get();

        assertEquals(expectedParkingSpace, actualParkingSpace);
    }

    @Test
    public void findById_ShouldReturnEmptyOptional_WhenParkingSpaceWithSpecifiedIdIsNotPresent() {
        assertEquals(Optional.empty(), parkingSpacesRepository.findById(100L));
    }

    @Test
    public void findAllAvailableParkingSpacesByLocationForPeriod_ShouldReturnParkingSpacesList_WhenAllParkingSpacesAreAvailable() {
        List<ParkingSpace> expectedParkingSpaces = new ArrayList<>(List.of(new ParkingSpace(1L, "Space1", "Location1"),
                new ParkingSpace(2L, "Space2", "Location1"),
                new ParkingSpace(3L, "Space3", "Location1"),
                new ParkingSpace(4L, "Space4", "Location1")));

        List<ParkingSpace> actualParkingSpaces = parkingSpacesRepository.findAllAvailableParkingSpacesByLocationForPeriod("Location1", LocalDateTime.now(), LocalDateTime.now());
        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    public void findAllAvailableParkingSpacesByLocationForPeriod_ShouldReturnParkingSpacesList_WhenOneParkingSpaceWasBooked() {
        List<ParkingSpace> expectedParkingSpaces = new ArrayList<>(List.of(new ParkingSpace(2L, "Space2", "Location1"),
                new ParkingSpace(3L, "Space3", "Location1"),
                new ParkingSpace(4L, "Space4", "Location1")));

        List<ParkingSpace> actualParkingSpaces = parkingSpacesRepository.findAllAvailableParkingSpacesByLocationForPeriod(
                "Location1",
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(10, 0, 0)),
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(12, 0, 0)));
        assertEquals(expectedParkingSpaces, actualParkingSpaces);
    }

    @Test
    public void findAllAvailableParkingSpacesByLocationForPeriod_ShouldReturnEmptyList_WhenNoParkingSpaceAvailable() {
        List<ParkingSpace> expectedParkingSpaces = Collections.emptyList();

        List<ParkingSpace> actualParkingSpace = parkingSpacesRepository.findAllAvailableParkingSpacesByLocationForPeriod(
                "UnknownLocation",
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(10, 0, 0)),
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(12, 0, 0)));
        assertEquals(expectedParkingSpaces, actualParkingSpace);
    }

    @Test
    public void findAllAvailableParkingSpaces_ShouldReturnParkingSpacesList_WhenAllParkingSpacesAreAvailable() {
        List<ParkingSpace> actualParkingSpaces = parkingSpacesRepository.findAllAvailableParkingSpaces(LocalDateTime.now(), LocalDateTime.now());
        int expectedListSize = 12;
        assertEquals(expectedListSize, actualParkingSpaces.size());
    }

    @Test
    public void findAllAvailableParkingSpaces_ShouldReturnParkingSpacesList_WhenOneParkingSpaceWasBooked() {
        int expectedListSize = 11;

        List<ParkingSpace> actualParkingSpaces = parkingSpacesRepository.findAllAvailableParkingSpaces(
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(10, 0, 0)),
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(12, 0, 0)));
        assertEquals(expectedListSize, actualParkingSpaces.size());
    }

    @Test
    public void findAllAvailableParkingSpaces_ShouldReturnEmptyList_WhenNoParkingSpaceAvailable() {
        List<ParkingSpace> expectedParkingSpaces = Collections.emptyList();

        List<ParkingSpace> actualParkingSpace = parkingSpacesRepository.findAllAvailableParkingSpacesByLocationForPeriod(
                "UnknownLocation",
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(10, 0, 0)),
                LocalDateTime.of(LocalDate.of(2022, 6, 30), LocalTime.of(12, 0, 0)));
        assertEquals(expectedParkingSpaces, actualParkingSpace);
    }

}
