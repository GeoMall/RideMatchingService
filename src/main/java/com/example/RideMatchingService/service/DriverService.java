package com.example.RideMatchingService.service;

import com.example.RideMatchingService.dto.driver.DriverDTO;
import com.example.RideMatchingService.model.Driver;
import com.example.RideMatchingService.repository.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DriverService
{
    @Autowired
    private DistanceService distanceService;

    @Autowired
    private DriverRepository repository;

    public Driver register(DriverDTO request) {
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());
        driver.setAvailable(request.isAvailable());

        return repository.save(driver);
    }

    public Driver update(Long driverId, DriverDTO request) {
        Driver driver = repository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver with id "+ driverId+ " not found"));

        driver.setName(request.getName());
        driver.setAvailable(request.isAvailable());
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());

        return repository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return repository.findAll();
    }

    public Driver getDriverById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    public List<Driver> getAvailableDrivers() {
        return repository.findByIsAvailable(true);
    }

    public List<Driver> getNearestAvailableDrivers(double pickupLat, double pickupLon){
        var availableDrivers = getAvailableDrivers();

        return availableDrivers.stream()
                .map(driver -> Map.entry(
                        driver,
                        distanceService.calculateDistance(
                                pickupLat, pickupLon,
                                driver.getLatitude(), driver.getLongitude()
                        )
                ))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    public Driver getNearestDriver(
            List<Driver> availableDrivers,
            double pickupLat,
            double pickupLon
    ) {

        return availableDrivers.stream()
                .min(Comparator.comparingDouble(driver ->
                        distanceService.calculateDistance(
                                pickupLat, pickupLon,
                                driver.getLatitude(), driver.getLongitude()
                        )
                ))
                .orElse(null);
    }

}
