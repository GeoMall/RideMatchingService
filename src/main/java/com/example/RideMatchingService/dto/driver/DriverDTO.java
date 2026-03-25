package com.example.RideMatchingService.dto.driver;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverDTO
{
    @NotBlank(message = "Name is required")
    private String name;

    private boolean isAvailable;

    @Min(-90) @Max(90)
    private double latitude;

    @Min(-180) @Max(180)
    private double longitude;
}
