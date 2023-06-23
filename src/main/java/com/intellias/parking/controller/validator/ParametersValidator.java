package com.intellias.parking.controller.validator;

import com.intellias.parking.service.exception.InvalidQueryParametersException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParametersValidator {
    private final static String ERROR_MESSAGE = "Record ID should be a positive integer.";

    public static void validateRecordId(long id) {
        if (id <= 0) {
            log.error(ERROR_MESSAGE);
            throw new InvalidQueryParametersException(ERROR_MESSAGE);
        }
    }

}
