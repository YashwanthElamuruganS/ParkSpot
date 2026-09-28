package com.parkspot.parking_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkspot.parking_management.model.VisitorVehicle;

public interface VisitorVehicleRepository extends JpaRepository<VisitorVehicle, Long> {
}
