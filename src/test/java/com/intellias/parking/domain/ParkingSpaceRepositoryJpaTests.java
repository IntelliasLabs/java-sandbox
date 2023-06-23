package com.intellias.parking.domain;

import com.intellias.parking.persistence.ParkingSpaceRepository;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import com.intellias.parking.persistence.entity.Status;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class ParkingSpaceRepositoryJpaTests {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
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

        List<ParkingSpaceEntity> expected = List.of(firstAddedEntity, secondAddedEntity);
        List<ParkingSpaceEntity> actual = parkingSpaceRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnEmptyParkingSpacesList_WhenParkingSpacesAreNotPresent() {
        List<ParkingSpaceEntity> expected = Collections.emptyList();
        List<ParkingSpaceEntity> actual = parkingSpaceRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void findById_ShouldReturnParkingSpace_WhenParkingSpaceWithThisIdExists() {
        ParkingSpaceEntity addedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("First Parking Space")
                .status(Status.AVAILABLE)
                .build());

        Optional<ParkingSpaceEntity> expected = Optional.of(addedEntity);
        Optional<ParkingSpaceEntity> actual = parkingSpaceRepository.findById(addedEntity.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void findById_ShouldEmptyOptional_WhenParkingSpaceWithThisIdDoesNotExist() {
        Optional<ParkingSpaceEntity> expected = Optional.empty();
        Optional<ParkingSpaceEntity> actual = parkingSpaceRepository.findById(100L);
        assertEquals(expected, actual);
    }

}