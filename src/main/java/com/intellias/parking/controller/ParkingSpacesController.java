package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ParkingSpacesController.API_VERSION + ParkingSpacesController.PATH)
@RequiredArgsConstructor
@Slf4j
public class ParkingSpacesController {
    public final static String PATH = "parking-spaces";
    public final static String API_VERSION = "/api/v1/";

    private final ParkingSpaceService parkingSpaceService;

    @RequestMapping
    public List<ParkingSpaceDTO> getAll() {
        List<ParkingSpaceDTO> parkingSpaces = parkingSpaceService.findAll();
        log.info("Parking spaces found: {}", parkingSpaces);

        return parkingSpaces;
    }

}
