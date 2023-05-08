package com.intellias.basicsandbox.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class ItemEntity {
    UUID id;
    String name;
}
