package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.service.space.ParkingSpaceService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ParkingSpaceController.API_VERSION + ParkingSpaceController.PATH)
@RequiredArgsConstructor
public class ParkingSpaceController {

    public final static String PATH = "parking-spaces";
    public final static String API_VERSION = "/api/v1/";
    private final ParkingSpaceService parkingSpaceService;

    @GetMapping
    public ResponseEntity<List<ParkingSpaceDTO>> getAllParkingSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAllParkingSpaces());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<ParkingSpaceDTO>> getAllParkingSpacesByLocation(@PathVariable("location") String location) {
        return ResponseEntity.ok(parkingSpaceService.getAllParkingSpacesByLocation(location));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpaceDTO>> getAvailableParkingSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAllAvailableParkingSpaces());
    }

    @GetMapping("/available/location/{location}")
    public ResponseEntity<List<ParkingSpaceDTO>> getAvailableParkingSpacesByLocation(
            @PathVariable("location") String location,
            @RequestParam(value = "startTime", required = false) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) LocalDateTime endTime) {

        if (startTime != null && endTime != null) {
            return ResponseEntity.ok(
                    parkingSpaceService.getAvailableParkingSpacesByLocationForPeriod(location, startTime, endTime));
        }

        return ResponseEntity.ok(parkingSpaceService.getAvailableParkingSpacesByLocation(location));
    }

}
