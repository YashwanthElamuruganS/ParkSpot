package com.parkspot.parking_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parkspot.parking_management.model.ParkingSlot;
import com.parkspot.parking_management.model.VisitorVehicle;
import com.parkspot.parking_management.repository.FlatRepository;
import com.parkspot.parking_management.repository.VisitorVehicleRepository;
import com.parkspot.parking_management.service.ParkingSlotService;

@Controller
public class WebController {

    private final FlatRepository flatRepository;
    private final ParkingSlotService parkingSlotService;
    private final VisitorVehicleRepository visitorVehicleRepository;

    public WebController(
            FlatRepository flatRepository,
            ParkingSlotService parkingSlotService,
            VisitorVehicleRepository visitorVehicleRepository) {

        this.flatRepository = flatRepository;
        this.parkingSlotService = parkingSlotService;
        this.visitorVehicleRepository = visitorVehicleRepository;
    }

    // =========================
    // DASHBOARD
    // =========================

    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) Long slotId,
            @RequestParam(required = false) String date,
            Model model) {

        // Get all parking slots
        List<ParkingSlot> allSlots =
                parkingSlotService.getAllSlots();

        // Total slots
        int slotCount = allSlots.size();

        // Available slots
        int availableCount =
                parkingSlotService.getAvailableSlots().size();

        // Occupied slots
        int occupiedCount =
                parkingSlotService.getOccupiedSlots().size();

        // Total flats
        long flatCount =
                flatRepository.count();

        // Get all visitors
        List<VisitorVehicle> visitors =
                visitorVehicleRepository.findAll();

        // =========================
        // DATE FILTER
        // =========================

        if (date != null && !date.isBlank()) {

            LocalDate selectedDate =
                    LocalDate.parse(date);

            visitors = visitors.stream()
                    .filter(v ->
                            v.getEntryTime() != null
                            && v.getEntryTime()
                                    .toLocalDate()
                                    .equals(selectedDate))
                    .toList();
        }

        // =========================
        // SLOT FILTER
        // =========================

        List<ParkingSlot> displayedSlots = allSlots;

        if (slotId != null) {

            displayedSlots = allSlots.stream()
                    .filter(slot ->
                            slot.getId() != null
                            && slot.getId().equals(slotId))
                    .toList();
        }

        // =========================
        // SEND DATA TO DASHBOARD
        // =========================

        model.addAttribute(
                "slotCount",
                slotCount
        );

        model.addAttribute(
                "availableCount",
                availableCount
        );

        model.addAttribute(
                "occupiedCount",
                occupiedCount
        );

        model.addAttribute(
                "flatCount",
                flatCount
        );

        // Total visitors in database
        model.addAttribute(
                "visitorCount",
                visitorVehicleRepository.count()
        );

        // All slots for dropdown
        model.addAttribute(
                "slots",
                allSlots
        );

        // Slots after filtering
        model.addAttribute(
                "displayedSlots",
                displayedSlots
        );

        // Visitors after date filtering
        model.addAttribute(
                "visitors",
                visitors
        );

        // Keep selected filter values
        model.addAttribute(
                "selectedSlotId",
                slotId
        );

        model.addAttribute(
                "selectedDate",
                date
        );

        return "dashboard";
    }


    // =========================
    // VISITORS
    // =========================

    @GetMapping("/visitors")
    public String visitors(Model model) {

        model.addAttribute(
                "visitors",
                visitorVehicleRepository.findAll()
        );

        model.addAttribute(
                "flats",
                flatRepository.findAll()
        );

        return "visitors";
    }


    // =========================
    // FLATS
    // =========================

    @GetMapping("/flats")
    public String flats(Model model) {

        model.addAttribute(
                "flats",
                flatRepository.findAll()
        );

        return "flats";
    }


    // =========================
    // PARKING SLOTS
    // =========================

    @GetMapping("/parking-slots")
    public String parkingSlots(Model model) {

        model.addAttribute(
                "slots",
                parkingSlotService.getAllSlots()
        );

        return "parking-slots";
    }


    // =========================
    // DAILY LOG
    // =========================

    @GetMapping("/daily-log")
    public String dailyLog(Model model) {

        model.addAttribute(
                "visitors",
                visitorVehicleRepository.findAll()
        );

        return "daily-log";
    }
}