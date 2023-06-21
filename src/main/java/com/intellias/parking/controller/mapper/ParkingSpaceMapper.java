package com.intellias.parking.controller.mapper;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingSpaceMapper {
    ParkingSpaceEntity toEntity(final ParkingSpaceDTO parkingSpaceDTO);

    ParkingSpaceDTO toDTO(final ParkingSpaceEntity parkingSpaceEntity);

}

