package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
