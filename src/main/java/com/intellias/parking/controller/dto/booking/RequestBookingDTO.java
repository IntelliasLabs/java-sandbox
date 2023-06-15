package com.intellias.parking.controller.dto.booking;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookingDTO {

    @NotNull(message = "userId is mandatory for booking")
    private Long userId;
    @NotNull(message = "parkingSpaceId is mandatory for booking")
    private Long parkingSpaceId;
    @NotNull(message = "startTime is mandatory for booking")
    private LocalDateTime startTime;
    @NotNull(message = "endTime is mandatory for booking")
    private LocalDateTime endTime;

}
