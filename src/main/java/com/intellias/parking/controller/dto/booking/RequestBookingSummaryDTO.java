package com.intellias.parking.controller.dto.booking;

import com.intellias.parking.controller.dto.ParkingSpaceDTO;
import com.intellias.parking.controller.dto.UserDTO;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBookingSummaryDTO {

    private ParkingSpaceDTO parkingSpace;
    private UserDTO user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus bookingStatus;

}
