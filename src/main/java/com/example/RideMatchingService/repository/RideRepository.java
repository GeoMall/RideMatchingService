package com.example.RideMatchingService.repository;

import com.example.RideMatchingService.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>
{
    List<Ride> findByRiderName(String riderName);
}

