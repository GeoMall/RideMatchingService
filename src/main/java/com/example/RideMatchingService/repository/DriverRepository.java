package com.example.RideMatchingService.repository;

import com.example.RideMatchingService.model.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>
{
    List<Driver> findByIsAvailable(boolean isAvailable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Driver d WHERE d.available = true")
    List<Driver> findAvailableDriversWithLock();
}
