package com.intellias.parking.service.exception;

public class ParkingSpaceNotAvailableException extends RuntimeException {

    public ParkingSpaceNotAvailableException(long id, String location, String startTime, String endTime) {
        super(String.format("Parking space with ID %d on location %s not available from %s until %s.", id, location, startTime, endTime));
    }

}
