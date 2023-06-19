package com.intellias.basicsandbox.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private HttpStatus status;
    private String message;
}
