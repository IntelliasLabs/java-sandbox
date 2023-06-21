package com.intellias.parking.service;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.mapper.ParkingSpaceMapper;
import com.intellias.parking.persistence.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
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

}
