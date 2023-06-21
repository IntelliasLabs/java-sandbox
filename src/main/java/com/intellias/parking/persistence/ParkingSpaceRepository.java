package com.intellias.parking.persistence;

import com.intellias.parking.persistence.entity.ParkingSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Long> {
}
