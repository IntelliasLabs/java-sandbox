package com.intellias.parking.service;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.mapper.ParkingSpaceMapper;
import com.intellias.parking.persistence.ParkingSpaceRepository;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import com.intellias.parking.service.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceMapper mapper;

    @Transactional(readOnly = true)
    public List<ParkingSpaceDTO> findAll() {
        return parkingSpaceRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParkingSpaceDTO getById(long id) {
        ParkingSpaceEntity parkingSpaceEntity = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Parking space with ID {} not found.", id);
                    return new RecordNotFoundException("Parking space", id);
                });

        return mapper.toDTO(parkingSpaceEntity);
    }

}
