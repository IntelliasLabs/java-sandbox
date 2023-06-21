package com.intellias.parking;

import com.intellias.parking.controller.ParkingSpacesController;
import com.intellias.parking.persistence.ParkingSpaceRepository;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import com.intellias.parking.persistence.entity.Status;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class ParkingApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getRequestWithoutParameters_ShouldReturnParkingSpacesList_WhenParkingSpacesArePresent() {
        ParkingSpaceEntity firstAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("First Parking Space")
                .status(Status.AVAILABLE)
                .build());
        ParkingSpaceEntity secondAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("Second Parking Space")
                .status(Status.BOOKED)
                .build());

        webTestClient
                .get()
                .uri(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo(firstAddedEntity.getName())
                .jsonPath("$[0].status").isEqualTo(firstAddedEntity.getStatus().toString())
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].name").isEqualTo(secondAddedEntity.getName())
                .jsonPath("$[1].status").isEqualTo(secondAddedEntity.getStatus().toString());
    }

    @Test
    public void getRequestWithoutParameters_ShouldReturnEmptyParkingSpacesList_WhenParkingSpacesAreNotPresent() {
        webTestClient
                .get()
                .uri(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEmpty();
    }

}
