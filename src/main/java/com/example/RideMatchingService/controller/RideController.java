package com.example.RideMatchingService.controller;

import com.example.RideMatchingService.dto.ride.RideRequestDTO;
import com.example.RideMatchingService.dto.ride.RideResponseDTO;
import com.example.RideMatchingService.model.Ride;
import com.example.RideMatchingService.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController
{
    @Autowired
    private RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<RideResponseDTO> requestRide(@Valid @RequestBody RideRequestDTO dto) {
        RideResponseDTO response = rideService.requestRide(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<RideResponseDTO> completeRide(@PathVariable Long id) {
        RideResponseDTO response = rideService.completeRide(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }
}
