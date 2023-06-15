package com.intellias.parking.persistence.entity.space;

import com.intellias.parking.persistence.entity.booking.Booking;
import jakarta.persistence.*;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "parking_spaces")
@Data
@NoArgsConstructor
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parkingSpace")
    private List<Booking> bookings;

    public ParkingSpace(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}

