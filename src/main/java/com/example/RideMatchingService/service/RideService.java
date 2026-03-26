package com.example.RideMatchingService.service;

import com.example.RideMatchingService.dto.driver.DriverDTO;
import com.example.RideMatchingService.dto.ride.RideRequestDTO;
import com.example.RideMatchingService.dto.ride.RideResponseDTO;
import com.example.RideMatchingService.model.Driver;
import com.example.RideMatchingService.model.Ride;
import com.example.RideMatchingService.model.RideStatus;
import com.example.RideMatchingService.repository.DriverRepository;
import com.example.RideMatchingService.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService
{
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;
    @Autowired
    private DistanceService distanceService;

    public List<RideResponseDTO> getAllRides() {
       return rideRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public RideResponseDTO requestRide(RideRequestDTO dto) {

        List<Driver> availableDrivers = driverRepository.findByIsAvailable(true);

        Ride ride = new Ride();
        ride.setRiderName(dto.getRiderName());
        ride.setPickupLatitude(dto.getPickupLatitude());
        ride.setPickupLongitude(dto.getPickupLongitude());

        if (availableDrivers.isEmpty()) {
            ride.setStatus(RideStatus.NO_DRIVER_AVAILABLE);
            rideRepository.save(ride);
            return buildResponse(ride, null, "No drivers available at the moment.");
        }

        Driver nearestDriver = driverService.getNearestDriver(
                availableDrivers,
                dto.getPickupLatitude(),
                dto.getPickupLongitude());

        nearestDriver.setAvailable(false);
        driverRepository.save(nearestDriver);

        ride.setDriver(nearestDriver);
        ride.setStatus(RideStatus.ASSIGNED);
        rideRepository.save(ride);

        return buildResponse(ride, nearestDriver, "Driver assigned successfully.");
    }

    @Transactional
    public RideResponseDTO completeRide(Long rideId) {
        var ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new RuntimeException("Ride cannot be completed. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);
        rideRepository.save(ride);

        var driver = ride.getDriver();
        driver.setAvailable(true);
        driverRepository.save(driver);

        return buildResponse(ride, driver, "Ride completed successfully. Driver is now available.");
    }

    public RideResponseDTO getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + id));

        return mapToDTO(ride);
    }

    private RideResponseDTO buildResponse(Ride ride, Driver driver, String message) {
        RideResponseDTO response = new RideResponseDTO();
        response.setRideId(ride.getId());
        response.setRiderName(ride.getRiderName());
        response.setPickupLatitude(ride.getPickupLatitude());
        response.setPickupLongitude(ride.getPickupLongitude());
        response.setStatus(ride.getStatus());
        response.setMessage(message);

        if (driver != null) {
            response.setDriverId(driver.getId());
            response.setDriverName(driver.getName());
            response.setDriverLatitude(driver.getLatitude());
            response.setDriverLongitude(driver.getLongitude());
        }

        return response;
    }

    private RideResponseDTO mapToDTO(Ride ride) {
        RideResponseDTO dto = new RideResponseDTO();
        dto.setRideId(ride.getId());
        dto.setRiderName(ride.getRiderName());
        dto.setPickupLatitude(ride.getPickupLatitude());
        dto.setPickupLongitude(ride.getPickupLongitude());
        dto.setStatus(ride.getStatus());

        var driver = ride.getDriver();
        dto.setDriverId(driver.getId());
        dto.setDriverName(driver.getName());
        dto.setDriverLatitude(driver.getLatitude());
        dto.setDriverLongitude(driver.getLongitude());

        return dto;
    }
}