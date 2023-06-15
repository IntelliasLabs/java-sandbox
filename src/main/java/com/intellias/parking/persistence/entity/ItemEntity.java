package com.intellias.parking.persistence.entity;

import com.intellias.parking.persistence.converter.EncryptedStringConverter;
import jakarta.persistence.*;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "credit_card")
    @Convert(converter = EncryptedStringConverter.class)
    private String creditCard;
}
