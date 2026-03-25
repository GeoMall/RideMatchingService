package com.example.RideMatchingService.dto.ride;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RideRequestDTO
{
    @NotBlank(message = "Rider name is required")
    private String riderName;

    @NotNull(message = "Rider Latitude is required")
    private double pickupLatitude;

    @NotNull(message = "Rider Longitude is required")
    private double pickupLongitude;
}
