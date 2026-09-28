package com.parkspot.parking_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkspot.parking_management.model.ParkingSlot;
import com.parkspot.parking_management.service.ParkingSlotService;

@RestController
@RequestMapping("/api/slots")
public class ParkingSlotController {

    private final ParkingSlotService service;

    public ParkingSlotController(ParkingSlotService service) {
        this.service = service;
    }

    @PostMapping
    public ParkingSlot addSlot(@RequestBody ParkingSlot slot) {
        return service.addSlot(slot);
    }

    @GetMapping
    public List<ParkingSlot> getAllSlots() {
        return service.getAllSlots();
    }

    @GetMapping("/available")
    public List<ParkingSlot> getAvailableSlots() {
        return service.getAvailableSlots();
    }

    @GetMapping("/occupied")
    public List<ParkingSlot> getOccupiedSlots() {
        return service.getOccupiedSlots();
    }

    @GetMapping("/{id}")
    public ParkingSlot getSlotById(@PathVariable Long id) {
        return service.getSlotById(id);
    }
}