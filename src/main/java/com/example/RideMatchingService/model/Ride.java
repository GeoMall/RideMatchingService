package com.example.RideMatchingService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String riderName;

    @Column(nullable = false)
    private double pickupLatitude;

    @Column(nullable = false)
    private double pickupLongitude;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @CreationTimestamp
    private LocalDateTime requestedAt;

    @Version
    private Long version;
}