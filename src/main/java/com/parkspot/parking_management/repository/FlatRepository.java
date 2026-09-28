
package com.parkspot.parking_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkspot.parking_management.model.Flat;

public interface FlatRepository extends JpaRepository<Flat, Long> {
}