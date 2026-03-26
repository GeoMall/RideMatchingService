package com.example.RideMatchingService.service;

import com.example.RideMatchingService.dto.driver.DriverCreateDTO;
import com.example.RideMatchingService.dto.driver.DriverDTO;
import com.example.RideMatchingService.dto.driver.DriverRequestDTO;
import com.example.RideMatchingService.model.Driver;
import com.example.RideMatchingService.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class DriverService
{
    @Autowired
    private DistanceService distanceService;

    @Autowired
    private DriverRepository repository;

    public DriverDTO register(DriverCreateDTO request) {
        Driver driver = mapToEntity(request);
        return mapToDTO(repository.save(driver));
    }

    public DriverDTO update(Long driverId, DriverRequestDTO request) {
        Driver driver = repository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver with id " + driverId + " not found"));

        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());
        driver.setAvailable(request.isAvailable());

        return mapToDTO(repository.save(driver));
    }

    public List<DriverDTO> getAllDrivers() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public DriverDTO getDriverById(Long id) {
        Driver driver = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));

        return mapToDTO(driver);
    }

    @Transactional
    public List<DriverDTO> getAvailableDrivers() {
        return repository.findByIsAvailable(true)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public List<DriverDTO> getNearestAvailableDrivers(double pickupLat, double pickupLon){
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

    private DriverDTO mapToDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setLatitude(driver.getLatitude());
        dto.setLongitude(driver.getLongitude());
        dto.setAvailable(driver.isAvailable());

        return dto;
    }

    private Driver mapToEntity(DriverCreateDTO dto) {
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());
        driver.setAvailable(dto.isAvailable());

        return driver;
    }
}
