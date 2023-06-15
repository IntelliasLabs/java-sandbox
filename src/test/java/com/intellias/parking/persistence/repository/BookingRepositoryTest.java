package com.intellias.parking.persistence.repository;

import com.intellias.parking.persistence.entity.booking.Booking;
import com.intellias.parking.persistence.entity.space.ParkingSpace;
import com.intellias.parking.persistence.entity.user.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookingRepositoryTest {

    private static final long PARKING_SPACE_ID = 1L;
    private static final long USER_ID = 1L;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSpacesRepository parkingSpacesRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void save_ShouldSaveBooking_WhenAllBookingPropertiesAreValid() {
        ParkingSpace parkingSpace = parkingSpacesRepository.findById(PARKING_SPACE_ID).get();
        User user = userRepository.findById(USER_ID).get();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);

        Booking bookingToSave = new Booking();
        bookingToSave.setParkingSpace(parkingSpace);
        bookingToSave.setUser(user);
        bookingToSave.setStartTime(startTime);
        bookingToSave.setEndTime(endTime);

        Booking savedBooking = bookingRepository.save(bookingToSave);

        assertEquals(bookingToSave.getUser(), savedBooking.getUser());
        assertEquals(bookingToSave.getParkingSpace(), savedBooking.getParkingSpace());
        assertEquals(bookingToSave.getStartTime(), savedBooking.getStartTime());
        assertEquals(bookingToSave.getEndTime(), savedBooking.getEndTime());
    }

}