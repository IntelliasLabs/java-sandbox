package com.intellias.parking.service.booking;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.dto.booking.BookingStatus;
import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import com.intellias.parking.controller.dto.booking.RequestBookingSummaryDTO;
import com.intellias.parking.controller.mapper.BookingMapper;
import com.intellias.parking.controller.mapper.ParkingSpaceMapper;
import com.intellias.parking.controller.mapper.UserMapper;
import com.intellias.parking.persistence.entity.booking.Booking;
import com.intellias.parking.persistence.entity.space.ParkingSpace;
import com.intellias.parking.persistence.entity.user.User;
import com.intellias.parking.persistence.repository.BookingRepository;
import com.intellias.parking.service.exception.ParkingSpaceNotAvailableException;
import com.intellias.parking.service.space.ParkingSpaceService;
import com.intellias.parking.service.user.UserService;
import jakarta.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ParkingSpaceService parkingSpaceService;
    private final ParkingSpaceMapper parkingSpaceMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public RequestBookingSummaryDTO requestBooking(RequestBookingDTO requestBookingDTO) {
        LocalDateTime now = LocalDateTime.now();
        if (requestBookingDTO.getStartTime().isBefore(now)) {
            throw new ValidationException("startTime cannot be in the past.");
        }

        if (requestBookingDTO.getStartTime().isAfter(requestBookingDTO.getEndTime())) {
            throw new ValidationException("startTime cannot be later that endTime.");
        }

        final ParkingSpace parkingSpace = parkingSpaceMapper.toEntity(parkingSpaceService.getParkingSpaceById(requestBookingDTO.getParkingSpaceId()));

        if (!checkParkingSpaceAvailability(parkingSpace, requestBookingDTO)) {
            throw new ParkingSpaceNotAvailableException(parkingSpace.getId(), parkingSpace.getLocation(),
                    requestBookingDTO.getStartTime().toString(), requestBookingDTO.getEndTime().toString());
        }

        final User user = userMapper.toEntity(userService.getUserById(requestBookingDTO.getUserId()));
        final Booking booking = bookingMapper.toEntity(requestBookingDTO, parkingSpace, user);
        final Booking savedBooking = bookingRepository.save(booking);

        final RequestBookingSummaryDTO requestBookingSummary = bookingMapper.toBookingSummaryDTO(savedBooking);
        requestBookingSummary.setBookingStatus(BookingStatus.SUCCESSFUL);

        log.info("Successfully booked parking space. Booking summary: {}", requestBookingSummary);
        return requestBookingSummary;
    }

    private boolean checkParkingSpaceAvailability(ParkingSpace parkingSpace, RequestBookingDTO requestBookingDTO) {
        List<ParkingSpaceDTO> availableParkingSpaces = parkingSpaceService
                .getAvailableParkingSpacesByLocationForPeriod(parkingSpace.getLocation(), requestBookingDTO.getStartTime(), requestBookingDTO.getEndTime());

        return availableParkingSpaces.stream().anyMatch(parkingSpaceDTO -> parkingSpaceDTO.getId().equals(parkingSpace.getId()));
    }

}
