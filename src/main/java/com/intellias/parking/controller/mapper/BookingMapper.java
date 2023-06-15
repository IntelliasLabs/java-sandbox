package com.intellias.parking.controller.mapper;

import com.intellias.parking.controller.dto.booking.RequestBookingDTO;
import com.intellias.parking.controller.dto.booking.RequestBookingSummaryDTO;
import com.intellias.parking.persistence.entity.booking.Booking;
import com.intellias.parking.persistence.entity.space.ParkingSpace;
import com.intellias.parking.persistence.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "parkingSpace", source = "parkingSpace")
    @Mapping(target = "id", ignore = true)
    Booking toEntity(final RequestBookingDTO bookingDTO, final ParkingSpace parkingSpace, final User user);

    @Mapping(target = "bookingStatus", ignore = true)
    RequestBookingSummaryDTO toBookingSummaryDTO(final Booking booking);

}
