package com.parkspot.parking_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkspot.parking_management.model.Flat;
import com.parkspot.parking_management.model.ParkingSlot;
import com.parkspot.parking_management.model.VisitorVehicle;
import com.parkspot.parking_management.repository.FlatRepository;
import com.parkspot.parking_management.repository.ParkingSlotRepository;
import com.parkspot.parking_management.repository.VisitorVehicleRepository;

@Service
public class VisitorVehicleService {

    private final VisitorVehicleRepository visitorRepository;
    private final FlatRepository flatRepository;
    private final ParkingSlotRepository slotRepository;

    public VisitorVehicleService(
            VisitorVehicleRepository visitorRepository,
            FlatRepository flatRepository,
            ParkingSlotRepository slotRepository) {
        this.visitorRepository = visitorRepository;
        this.flatRepository = flatRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public synchronized VisitorVehicle registerVisitor(
            VisitorVehicle visitor, Long flatId, Long slotId) {

        boolean alreadyParked = visitorRepository.findAll()
                .stream()
                .anyMatch(v ->
                        v.getVehicleNumber().equalsIgnoreCase(
                                visitor.getVehicleNumber())
                        && v.getExitTime() == null
                        && v.getParkingSlot() != null);

        if (alreadyParked) {
            throw new RuntimeException(
                    "Vehicle is already parked");
        }

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() ->
                        new RuntimeException("Flat not found"));

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new RuntimeException("Slot not found"));

        if (slot.isOccupied()) {
            throw new RuntimeException(
                    "Parking slot is already occupied");
        }

        visitor.setFlat(flat);
        visitor.setParkingSlot(slot);
        visitor.setEntryTime(LocalDateTime.now());
        visitor.setExitTime(null);

        slot.setOccupied(true);
        slotRepository.save(slot);

        return visitorRepository.save(visitor);
    }

    public List<VisitorVehicle> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public VisitorVehicle getVisitorById(Long id) {
        return visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Visitor not found"));
    }

    public List<VisitorVehicle> getActiveVisitors() {
        return visitorRepository.findAll()
                .stream()
                .filter(v -> v.getParkingSlot() != null
                        && v.getExitTime() == null)
                .toList();
    }

    @Transactional
    public synchronized VisitorVehicle recordExit(Long id) {
        VisitorVehicle visitor = visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Visitor not found"));

        if (visitor.getExitTime() != null
                || visitor.getParkingSlot() == null) {
            throw new RuntimeException(
                    "Vehicle has already exited");
        }

        ParkingSlot slot = visitor.getParkingSlot();

        visitor.setExitTime(LocalDateTime.now());
        slot.setOccupied(false);

        slotRepository.save(slot);
        return visitorRepository.save(visitor);
    }

    public List<VisitorVehicle> getDailyLog(LocalDate date) {
        return visitorRepository.findAll()
                .stream()
                .filter(v -> v.getEntryTime() != null
                        && v.getEntryTime().toLocalDate()
                                .equals(date))
                .toList();
    }
}