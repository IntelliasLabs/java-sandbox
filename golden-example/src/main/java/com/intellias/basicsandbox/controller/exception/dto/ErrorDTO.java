package com.intellias.basicsandbox.controller.exception.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorDTO {
    private HttpStatus status;
    private String message;
}
