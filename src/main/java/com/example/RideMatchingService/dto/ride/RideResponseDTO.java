package com.example.RideMatchingService.dto.ride;

import com.example.RideMatchingService.model.RideStatus;
import lombok.Data;

@Data
public class RideResponseDTO {

    private Long rideId;
    private String riderName;
    private double pickupLatitude;
    private double pickupLongitude;

    private Long driverId;
    private String driverName;
    private double driverLatitude;
    private double driverLongitude;

    private RideStatus status;
    private String message;
}
