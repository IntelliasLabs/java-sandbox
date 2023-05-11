package com.intellias.basicsandbox.controller.exception;

import com.intellias.basicsandbox.controller.exception.dto.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Promotes a unified exception handling throughout a whole application.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than jakarta.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the custom ErrorDTO object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(NOT_FOUND);
        return buildResponseEntity(errorDTO);
    }

    private ResponseEntity<ErrorDTO> buildResponseEntity(ErrorDTO error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
