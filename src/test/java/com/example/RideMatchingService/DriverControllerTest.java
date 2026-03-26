package com.example.RideMatchingService;

import com.example.RideMatchingService.controller.DriverController;
import com.example.RideMatchingService.dto.driver.DriverCreateDTO;
import com.example.RideMatchingService.dto.driver.DriverDTO;
import com.example.RideMatchingService.dto.driver.DriverRequestDTO;
import com.example.RideMatchingService.model.DriverLocationRequest;
import com.example.RideMatchingService.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DriverControllerTest {

    @InjectMocks
    private DriverController driverController;

    @Mock
    private DriverService driverService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterDriver() {
        DriverCreateDTO dto = new DriverCreateDTO();
        dto.setName("John");
        dto.setAvailable(true);
        dto.setLatitude(100.0000000);
        dto.setLongitude(111.0000000);

        DriverDTO savedDriver = new DriverDTO();
        savedDriver.setId(1L);
        savedDriver.setName("John");
        savedDriver.setAvailable(true);
        savedDriver.setLatitude(100.0000000);
        savedDriver.setLongitude(111.0000000);

        when(driverService.register(dto)).thenReturn(savedDriver);

        ResponseEntity<DriverDTO> response = driverController.register(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(savedDriver);
        verify(driverService, times(1)).register(dto);
    }

    @Test
    void shouldReturnAvailableDrivers() {
        DriverDTO d = new DriverDTO();
        d.setId(1L);
        d.setAvailable(true);
        d.setLatitude(100.0000000);
        d.setLongitude(111.0000000);

        when(driverService.getAvailableDrivers()).thenReturn(List.of(d));

        ResponseEntity<List<DriverDTO>> response = driverController.getAvailableDrivers();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(d);
        verify(driverService, times(1)).getAvailableDrivers();
    }

    @Test
    void shouldReturnNearestDrivers() {
        DriverDTO driver1 = new DriverDTO();
        driver1.setId(1L);
        driver1.setAvailable(true);
        driver1.setLatitude(36.033894540487395);
        driver1.setLongitude(14.314302345741163);

        DriverDTO driver2 = new DriverDTO();
        driver2.setId(2L);
        driver2.setAvailable(true);
        driver2.setLatitude(36.035319024327464);
        driver2.setLongitude(14.311347850522173);

        when(driverService.getNearestAvailableDrivers(anyDouble(), anyDouble())).thenReturn(List.of(driver1, driver2));

        DriverLocationRequest dto = new DriverLocationRequest();
        dto.setLatitude(36.035319024327464);
        dto.setLongitude(14.311347850522173);

        ResponseEntity<List<DriverDTO>> response = driverController.getNearestDrivers(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).extracting(DriverDTO::getId).containsExactlyInAnyOrder( 2L, 1L);
        verify(driverService, times(1)).getNearestAvailableDrivers(anyDouble(), anyDouble());
    }

    @Test
    void shouldReturnEmptyListWhenNoDrivers() {
        when(driverService.getAvailableDrivers()).thenReturn(List.of());

        ResponseEntity<List<DriverDTO>> response = driverController.getAvailableDrivers();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
        verify(driverService, times(1)).getAvailableDrivers();
    }

    @Test
    void shouldReturnUpdatedDrivers() {
        DriverRequestDTO dto = new DriverRequestDTO();
        dto.setAvailable(false);
        dto.setLatitude(1.0000000);
        dto.setLongitude(11.0000000);

        DriverDTO savedDriver = new DriverDTO();
        savedDriver.setId(1L);
        savedDriver.setName("John");
        savedDriver.setAvailable(false);
        savedDriver.setLatitude(1.0000000);
        savedDriver.setLongitude(11.0000000);

        when(driverService.update(1L, dto)).thenReturn(savedDriver);

        ResponseEntity<DriverDTO> response = driverController.update(1L, dto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(savedDriver);
        verify(driverService, times(1)).update(1L, dto);
    }
}