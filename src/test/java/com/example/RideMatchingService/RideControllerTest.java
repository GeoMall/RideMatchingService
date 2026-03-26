package com.example.RideMatchingService;

import com.example.RideMatchingService.controller.RideController;
import com.example.RideMatchingService.dto.ride.RideRequestDTO;
import com.example.RideMatchingService.dto.ride.RideResponseDTO;
import com.example.RideMatchingService.model.Ride;
import com.example.RideMatchingService.model.RideStatus;
import com.example.RideMatchingService.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RideControllerTest {

    @InjectMocks
    private RideController rideController;

    @Mock
    private RideService rideService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRequestRideSuccessfully() {
        RideRequestDTO dto = new RideRequestDTO();
        dto.setPickupLatitude(36.03447684451744);
        dto.setPickupLongitude(14.314014930066286);
        dto.setRiderName("riderName");

        RideResponseDTO responseDTO = new RideResponseDTO();
        responseDTO.setRideId(1L);
        responseDTO.setDriverLatitude(36.03447684451744);
        responseDTO.setDriverLongitude(14.314014930066286);
        responseDTO.setDriverId(1L);
        responseDTO.setDriverName("driverName");
        responseDTO.setPickupLatitude(36.03447684451744);
        responseDTO.setPickupLongitude(14.314014930066286);
        responseDTO.setRiderName("riderName");
        responseDTO.setStatus(RideStatus.ASSIGNED);

        when(rideService.requestRide(dto)).thenReturn(responseDTO);

        ResponseEntity<RideResponseDTO> response = rideController.requestRide(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(rideService, times(1)).requestRide(dto);
    }

    @Test
    void shouldReturnAllRides() {
        RideResponseDTO ride = new RideResponseDTO();
        ride.setRideId(1L);

        when(rideService.getAllRides()).thenReturn(List.of(ride));

        ResponseEntity<List<RideResponseDTO>> response = rideController.getAllRides();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(ride);
        verify(rideService, times(1)).getAllRides();
    }

    @Test
    void shouldCompleteRideSuccessfully() {

        RideResponseDTO responseDTO = new RideResponseDTO();
        responseDTO.setRideId(1L);
        responseDTO.setDriverLatitude(36.03447684451744);
        responseDTO.setDriverLongitude(14.314014930066286);
        responseDTO.setDriverId(1L);
        responseDTO.setDriverName("driverName");
        responseDTO.setPickupLatitude(36.03447684451744);
        responseDTO.setPickupLongitude(14.314014930066286);
        responseDTO.setRiderName("riderName");
        responseDTO.setStatus(RideStatus.COMPLETED);

        when(rideService.completeRide(1L)).thenReturn(responseDTO);

        ResponseEntity<RideResponseDTO> response = rideController.completeRide(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(rideService, times(1)).completeRide(1L);
    }

    @Test
    void shouldThrowWhenRideSaveFails() {
        RideRequestDTO dto = new RideRequestDTO();

        when(rideService.requestRide(dto))
                .thenThrow(new RuntimeException("DB failure"));

        try {
            rideController.requestRide(dto);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).contains("DB failure");
        }

        verify(rideService, times(1)).requestRide(dto);
    }
}