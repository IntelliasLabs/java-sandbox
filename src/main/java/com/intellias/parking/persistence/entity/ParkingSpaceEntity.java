package com.intellias.parking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_spaces", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}
