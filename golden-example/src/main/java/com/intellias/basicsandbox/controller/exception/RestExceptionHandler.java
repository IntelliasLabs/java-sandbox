package com.intellias.basicsandbox.controller.exception;

import com.intellias.basicsandbox.controller.exception.dto.ErrorDTO;
import com.intellias.basicsandbox.service.exception.ItemAlreadyExistsException;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

/**
 * Promotes a unified exception handling throughout a whole application.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles ItemNotFoundException. Created to encapsulate errors with more detail.
     *
     * @param ex the ItemNotFoundException
     * @return the custom ErrorDTO object
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleItemNotFound(ItemNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(NOT_FOUND);

        return errorDTO;
    }

    /**
     * Handles ItemAlreadyExistsException. Created to encapsulate errors with more detail.
     *
     * @param ex the ItemAlreadyExistsException
     * @return the custom ErrorDTO object
     */
    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ErrorDTO itemAlreadyExistsHandler(ItemAlreadyExistsException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(UNPROCESSABLE_ENTITY);

        return errorDTO;
    }
}
