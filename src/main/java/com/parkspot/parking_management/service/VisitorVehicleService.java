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

    // =========================================================
    // REGISTER VISITOR
    // =========================================================

    @Transactional
    public VisitorVehicle registerVisitor(
            VisitorVehicle visitor,
            Long flatId,
            Long slotId) {

        // Validate request
        if (visitor == null) {
            throw new RuntimeException("Visitor details are required");
        }

        if (visitor.getVisitorName() == null
                || visitor.getVisitorName().trim().isEmpty()) {

            throw new RuntimeException("Visitor name is required");
        }

        if (visitor.getVehicleNumber() == null
                || visitor.getVehicleNumber().trim().isEmpty()) {

            throw new RuntimeException("Vehicle number is required");
        }

        if (flatId == null) {
            throw new RuntimeException("Flat ID is required");
        }

        if (slotId == null) {
            throw new RuntimeException("Parking slot ID is required");
        }

        // Clean input
        visitor.setVisitorName(
                visitor.getVisitorName().trim());

        visitor.setVehicleNumber(
                visitor.getVehicleNumber().trim().toUpperCase());

        // ---------------------------------------------------------
        // Check whether this vehicle is already inside
        // ---------------------------------------------------------

        boolean alreadyParked = visitorRepository.findAll()
                .stream()
                .anyMatch(existingVisitor ->

                        existingVisitor.getVehicleNumber() != null
                        && existingVisitor.getVehicleNumber()
                                .equalsIgnoreCase(
                                        visitor.getVehicleNumber())

                        && existingVisitor.getExitTime() == null

                        && existingVisitor.getParkingSlot() != null
                );

        if (alreadyParked) {

            throw new RuntimeException(
                    "Vehicle " + visitor.getVehicleNumber()
                    + " is already parked");
        }

        // ---------------------------------------------------------
        // Find Flat
        // ---------------------------------------------------------

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Flat not found with ID: " + flatId));

        // ---------------------------------------------------------
        // Find Parking Slot
        // ---------------------------------------------------------

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Parking slot not found with ID: "
                                + slotId));

        // ---------------------------------------------------------
        // Check whether slot is occupied
        // ---------------------------------------------------------

        if (slot.isOccupied()) {

            throw new RuntimeException(
                    "Parking slot "
                    + slot.getSlotNumber()
                    + " is already occupied");
        }

        // ---------------------------------------------------------
        // Set visitor details
        // ---------------------------------------------------------

        visitor.setFlat(flat);

        visitor.setParkingSlot(slot);

        visitor.setEntryTime(
                LocalDateTime.now());

        visitor.setExitTime(null);

        // ---------------------------------------------------------
        // Mark parking slot as occupied
        // ---------------------------------------------------------

        slot.setOccupied(true);

        slotRepository.save(slot);

        // ---------------------------------------------------------
        // Save visitor
        // ---------------------------------------------------------

        return visitorRepository.save(visitor);
    }


    // =========================================================
    // GET ALL VISITORS
    // =========================================================

    public List<VisitorVehicle> getAllVisitors() {

        return visitorRepository.findAll();
    }


    // =========================================================
    // GET VISITOR BY ID
    // =========================================================

    public VisitorVehicle getVisitorById(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Visitor ID is required");
        }

        return visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Visitor not found with ID: " + id));
    }


    // =========================================================
    // GET ACTIVE VISITORS
    // =========================================================

    public List<VisitorVehicle> getActiveVisitors() {

        return visitorRepository.findAll()
                .stream()
                .filter(visitor ->
                        visitor.getExitTime() == null
                        && visitor.getParkingSlot() != null)
                .toList();
    }


    // =========================================================
    // RECORD VISITOR EXIT
    // =========================================================

    @Transactional
    public VisitorVehicle recordExit(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Visitor ID is required");
        }

        // Find visitor
        VisitorVehicle visitor =
                visitorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Visitor not found with ID: " + id));

        // Check whether visitor has already exited
        if (visitor.getExitTime() != null) {

            throw new RuntimeException(
                    "Vehicle has already exited");
        }

        // Get parking slot
        ParkingSlot slot =
                visitor.getParkingSlot();

        if (slot == null) {

            throw new RuntimeException(
                    "No parking slot is assigned to this visitor");
        }

        // ---------------------------------------------------------
        // Set exit time
        // ---------------------------------------------------------

        visitor.setExitTime(
                LocalDateTime.now());

        // ---------------------------------------------------------
        // Free parking slot
        // ---------------------------------------------------------

        slot.setOccupied(false);

        slotRepository.save(slot);

        // ---------------------------------------------------------
        // Save visitor
        // ---------------------------------------------------------

        return visitorRepository.save(visitor);
    }


    // =========================================================
    // DAILY LOG
    // =========================================================

    public List<VisitorVehicle> getDailyLog(
            LocalDate date) {

        if (date == null) {

            throw new RuntimeException(
                    "Date is required");
        }

        return visitorRepository.findAll()
                .stream()
                .filter(visitor ->
                        visitor.getEntryTime() != null
                        && visitor.getEntryTime()
                                .toLocalDate()
                                .equals(date))
                .toList();
    }
}