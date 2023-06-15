package com.intellias.parking.controller.mapper;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.persistence.entity.space.ParkingSpace;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingSpaceMapper {

    ParkingSpaceDTO toDTO(final ParkingSpace parkingSpace);

    List<ParkingSpaceDTO> listToDTO(final List<ParkingSpace> parkingSpaces);

    ParkingSpace toEntity(final ParkingSpaceDTO parkingSpaceDTO);
}
