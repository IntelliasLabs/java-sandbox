package com.intellias.parking.service.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String type, long id) {
        super(String.format("%s with ID %d not found.", type, id));
    }

}
