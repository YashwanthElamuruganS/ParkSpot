
package com.parkspot.parking_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkspot.parking_management.model.ParkingSlot;

public interface ParkingSlotRepository
        extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByOccupiedFalse();

    List<ParkingSlot> findByOccupiedTrue();
}