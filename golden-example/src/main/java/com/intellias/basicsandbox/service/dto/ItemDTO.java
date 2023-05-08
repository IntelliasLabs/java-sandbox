package com.intellias.basicsandbox.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
@Builder
public class ItemDTO {
    UUID id;

    @Size(min = 1, max = 255)
    @NotNull
    String name;
}
