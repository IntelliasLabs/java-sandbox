package com.intellias.basicsandbox.persistence.entity;

import com.intellias.basicsandbox.persistence.converter.EncryptedStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "currency_code")
    private String currencyCode;
}
