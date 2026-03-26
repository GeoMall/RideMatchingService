package com.example.RideMatchingService.controller;

import com.example.RideMatchingService.dto.driver.DriverCreateDTO;
import com.example.RideMatchingService.dto.driver.DriverDTO;
import com.example.RideMatchingService.dto.driver.DriverRequestDTO;
import com.example.RideMatchingService.model.DriverLocationRequest;
import com.example.RideMatchingService.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController
{
    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<DriverDTO> register(@Valid @RequestBody DriverCreateDTO request) {
        DriverDTO driver = driverService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverDTO> update(
            @PathVariable Long driverId,
            @Valid @RequestBody DriverRequestDTO request) {
        DriverDTO updated = driverService.update(driverId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverDTO>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PostMapping("/nearest")
    public ResponseEntity<List<DriverDTO>> getNearestDrivers(@RequestBody DriverLocationRequest request) {
        return ResponseEntity.ok(driverService.getNearestAvailableDrivers(request.getLatitude(), request.getLongitude()));
    }
}
