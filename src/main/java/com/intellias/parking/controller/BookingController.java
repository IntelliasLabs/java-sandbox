package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import com.intellias.parking.controller.dto.booking.RequestBookingSummaryDTO;
import com.intellias.parking.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BookingController.API_VERSION + BookingController.PATH)
@RequiredArgsConstructor
public class BookingController {

    public final static String PATH = "bookings";
    public final static String API_VERSION = "/api/v1/";
    private final BookingService bookingService;

    @PostMapping
    public RequestBookingSummaryDTO requestBooking(@RequestBody @Valid RequestBookingDTO requestBookingDTO) {
        return bookingService.requestBooking(requestBookingDTO);
    }
}
