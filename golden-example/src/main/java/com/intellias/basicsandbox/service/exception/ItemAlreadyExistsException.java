package com.intellias.basicsandbox.service.exception;

import java.util.UUID;

public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(UUID id) {
        super("An item with ID " + id + " already exists.");
    }

}
