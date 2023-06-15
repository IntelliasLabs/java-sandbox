package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.space.ParkingSpace;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParkingSpacesRepository extends JpaRepository<ParkingSpace, Long> {

    List<ParkingSpace> findAllByLocation(String location);

    @Query("select new ParkingSpace(p.id, p.name, p.location) " +
            "from  ParkingSpace p " +
            "where p.location = :location " +
            "and p.id not in " +
            "(select b.parkingSpace.id " +
            "from Booking b " +
            "where p.location = :location and :startTime <= b.endTime and :endTime >= b.startTime)"
    )
    List<ParkingSpace> findAllAvailableParkingSpacesByLocationForPeriod(@Param("location") String location,
                                                                        @Param("startTime") LocalDateTime startTime,
                                                                        @Param("endTime") LocalDateTime endTime);

    @Query("select new ParkingSpace(p.id, p.name, p.location) " +
            "from  ParkingSpace p " +
            "where p.id not in " +
            "(select b.parkingSpace.id " +
            "from Booking b " +
            "where :startTime <= b.endTime and :endTime >= b.startTime)"
    )
    List<ParkingSpace> findAllAvailableParkingSpaces(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);
}
