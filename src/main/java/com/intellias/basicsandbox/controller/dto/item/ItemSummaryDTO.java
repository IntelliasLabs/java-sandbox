package com.intellias.basicsandbox.controller.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSummaryDTO {
    UUID id;
    String name;
    private String creditCard;
}
