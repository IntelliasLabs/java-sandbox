package com.intellias.parking.controller.exception;

import com.intellias.parking.controller.dto.ErrorDTO;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import com.intellias.parking.service.exception.ParkingSpaceNotAvailableException;
import com.intellias.parking.service.exception.RecordNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({RecordNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleRecordNotFound(RecordNotFoundException exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(NOT_FOUND);

        return errorDTO;
    }

    @ExceptionHandler(InvalidQueryParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleQueryParametersMismatchException(InvalidQueryParametersException exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(BAD_REQUEST);

        return errorDTO;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationException(ValidationException exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(BAD_REQUEST);

        return errorDTO;
    }

    @ExceptionHandler(ParkingSpaceNotAvailableException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDTO handleParkingSpaceNotAvailableException(ParkingSpaceNotAvailableException exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(CONFLICT);

        return errorDTO;
    }

}
