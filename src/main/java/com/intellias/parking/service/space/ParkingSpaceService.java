package com.intellias.parking.service.space;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.mapper.ParkingSpaceMapper;
import com.intellias.parking.persistence.repository.ParkingSpacesRepository;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import com.intellias.parking.service.exception.RecordNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpacesRepository parkingSpacesRepository;
    private final ParkingSpaceMapper parkingSpaceMapper;

    public List<ParkingSpaceDTO> getAllParkingSpaces() {
        return parkingSpaceMapper.listToDTO(parkingSpacesRepository.findAll());
    }

    public List<ParkingSpaceDTO> getAllParkingSpacesByLocation(String location) {
        return parkingSpaceMapper.listToDTO(parkingSpacesRepository.findAllByLocation(location));
    }

    public List<ParkingSpaceDTO> getAllAvailableParkingSpaces() {
        final LocalDateTime now = LocalDateTime.now();
        return parkingSpaceMapper.listToDTO(parkingSpacesRepository.findAllAvailableParkingSpaces(now, now));
    }

    public List<ParkingSpaceDTO> getAvailableParkingSpacesByLocation(String location) {
        final LocalDateTime now = LocalDateTime.now();

        return getAvailableParkingSpacesByLocationForPeriod(location, now, now);
    }

    public List<ParkingSpaceDTO> getAvailableParkingSpacesByLocationForPeriod(String location, LocalDateTime startTime,
                                                                              LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidQueryParametersException("startTime cannot be later that endTime.");
        }

        return parkingSpaceMapper.listToDTO(parkingSpacesRepository.findAllAvailableParkingSpacesByLocationForPeriod(location, startTime, endTime));
    }

    public ParkingSpaceDTO getParkingSpaceById(Long parkingSpaceId) {
        return parkingSpaceMapper.toDTO(parkingSpacesRepository.findById(parkingSpaceId)
                .orElseThrow(() -> new RecordNotFoundException("parking_space", parkingSpaceId)));
    }
}
