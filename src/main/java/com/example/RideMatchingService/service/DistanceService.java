package com.example.RideMatchingService.service;

import com.example.RideMatchingService.model.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistanceService
{
    public double calculateDistance(double pickupLat, double pickupLon, double driverLat, double driverLon) {
        double dLat = driverLat - pickupLat;
        double dLon = driverLon - pickupLon;
        return Math.sqrt((dLat * dLat) + (dLon * dLon));
    }
}
