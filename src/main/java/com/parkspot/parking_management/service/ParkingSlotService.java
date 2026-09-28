package com.parkspot.parking_management.service;

import com.parkspot.parking_management.model.ParkingSlot;
import com.parkspot.parking_management.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkingSlotService {

    private final ParkingSlotRepository repository;

    public ParkingSlotService(ParkingSlotRepository repository) {
        this.repository = repository;
    }

    public ParkingSlot addSlot(ParkingSlot slot) {
        slot.setOccupied(false);
        return repository.save(slot);
    }

    public List<ParkingSlot> getAllSlots() {
        return repository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots() {
        return repository.findByOccupiedFalse();
    }

    public List<ParkingSlot> getOccupiedSlots() {
        return repository.findByOccupiedTrue();
    }

    public ParkingSlot getSlotById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Slot not found"));
    }
}