package com.intellias.parking.controller;

import com.intellias.parking.persistence.ParkingSpaceRepository;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import com.intellias.parking.persistence.entity.Status;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class ParkingSpacesControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getAll_ShouldReturn200AndParkingSpacesList_WhenParkingSpacesArePresent() throws Exception {
        ParkingSpaceEntity firstAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("First Parking Space")
                .status(Status.AVAILABLE)
                .build());
        ParkingSpaceEntity secondAddedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("Second Parking Space")
                .status(Status.BOOKED)
                .build());

        mockMvc.perform(get(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].name", equalTo(firstAddedEntity.getName())))
                .andExpect(jsonPath("$[0].status", equalTo(firstAddedEntity.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].name", equalTo(secondAddedEntity.getName())))
                .andExpect(jsonPath("$[1].status", equalTo(secondAddedEntity.getStatus().toString())));
    }

    @Test
    public void getAll_ShouldReturn200AndEmptyList_WhenParkingSpacesAreNotPresent() throws Exception {
        mockMvc.perform(get(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getById_ShouldReturn400WithErrorMessage_WhenPathParameterIsInvalid() throws Exception {
        mockMvc.perform(get(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH + "/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Record ID should be a positive integer.")));
    }

    @Test
    public void getById_ShouldReturn404WithErrorMessage_WhenParkingSpaceWithThisIdDoesNotExist() throws Exception {
        mockMvc.perform(get(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH + "/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Parking space with ID 100 not found.")));
    }

    @Test
    public void getById_ShouldReturn200WithParkingSpace_WhenParkingSpaceWithThisIdExists() throws Exception {
        ParkingSpaceEntity addedEntity = parkingSpaceRepository.save(ParkingSpaceEntity.builder()
                .name("First Parking Space")
                .status(Status.AVAILABLE)
                .build());

        mockMvc.perform(get(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH + "/" + addedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(addedEntity.getName())))
                .andExpect(jsonPath("$.status", equalTo(addedEntity.getStatus().toString())));
    }

}
