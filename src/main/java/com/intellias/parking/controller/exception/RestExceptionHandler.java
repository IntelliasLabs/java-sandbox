package com.intellias.parking.controller.exception;

import com.intellias.parking.controller.dto.ErrorDTO;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import com.intellias.parking.service.exception.ItemNotFoundException;
import com.intellias.parking.service.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ItemNotFoundException.class, RecordNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleRecordNotFound(Exception exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(NOT_FOUND);

        return errorDTO;
    }

    @ExceptionHandler(InvalidQueryParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleQueryParametersMismatchException(Exception exception) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(BAD_REQUEST);

        return errorDTO;
    }

}
