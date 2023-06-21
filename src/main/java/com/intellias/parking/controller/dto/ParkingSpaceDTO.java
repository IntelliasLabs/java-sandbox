package com.intellias.parking.controller.dto;

import com.intellias.parking.persistence.entity.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpaceDTO {

    private long id;

    @NotNull(message = "Constant violation: the 'name' field is null")
    @Size(min = 1, max = 255, message = "Constant violation: the 'name' field length should be in range [1, 255] characters")
    private String name;

    @NotNull(message = "Constant violation: the value of the 'status' field is null")
    private Status status;

}
