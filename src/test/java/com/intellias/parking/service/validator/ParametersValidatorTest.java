package com.intellias.parking.service.validator;

import com.intellias.parking.controller.validator.ParametersValidator;
import com.intellias.parking.service.exception.InvalidQueryParametersException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParametersValidatorTest {

    @Test
    public void validateRecordId_ShouldThrowNoException_WhenParameterIsPositiveInteger() {
        ParametersValidator.validateRecordId(1);
    }

    @Test
    public void validateRecordId_ShouldThrowException_WhenParameterIsNegativeInteger() {
        InvalidQueryParametersException exception = assertThrows(InvalidQueryParametersException.class, () -> ParametersValidator.validateRecordId(-1));
        assertEquals("Record ID should be a positive integer.", exception.getMessage());
    }

    @Test
    public void validateRecordId_ShouldThrowException_WhenParameterIsZero() {
        InvalidQueryParametersException exception = assertThrows(InvalidQueryParametersException.class, () -> ParametersValidator.validateRecordId(0));
        assertEquals("Record ID should be a positive integer.", exception.getMessage());
    }

}