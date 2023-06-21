package com.intellias.parking.domain;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.mapper.ParkingSpaceMapper;
import com.intellias.parking.persistence.ParkingSpaceRepository;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import com.intellias.parking.persistence.entity.Status;
import com.intellias.parking.service.ParkingSpaceService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class ParkingSpaceServiceTest {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;

    private ParkingSpaceService parkingSpaceService;

    @BeforeEach
    void setUp() {
        parkingSpaceService = new ParkingSpaceService(parkingSpaceRepository, parkingSpaceMapper);
    }

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void findAll_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresent() {
        ParkingSpaceEntity firstAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("First Parking Space")
                .status(Status.AVAILABLE)
                .build());
        ParkingSpaceEntity secondAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("Second Parking Space")
                .status(Status.BOOKED)
                .build());

        List<ParkingSpaceDTO> expected = List.of(parkingSpaceMapper.toDTO(firstAddedEntity), parkingSpaceMapper.toDTO(secondAddedEntity));
        List<ParkingSpaceDTO> actual = parkingSpaceService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnEmptyParkingSpacesList_WhenParkingSpacesAreNotPresent() {
        List<ParkingSpaceDTO> expected = Collections.emptyList();
        List<ParkingSpaceDTO> actual = parkingSpaceService.findAll();

        assertEquals(expected, actual);
    }

}
