package com.parkspot.parking_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/")
    public String dashboard(Model model) {

        int slotCount =
                parkingSlotService.getAllSlots().size();

        int availableCount =
                parkingSlotService.getAvailableSlots().size();

        long flatCount =
                flatRepository.count();

        long visitorCount =
                visitorVehicleRepository.count();

        model.addAttribute("slotCount", slotCount);
        model.addAttribute("availableCount", availableCount);
        model.addAttribute("flatCount", flatCount);
        model.addAttribute("visitorCount", visitorCount);

        model.addAttribute(
                "slots",
                parkingSlotService.getAllSlots()
        );

        model.addAttribute(
                "visitors",
                visitorVehicleRepository.findAll()
        );

        return "dashboard";
    }

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

    @GetMapping("/flats")
    public String flats(Model model) {

        model.addAttribute(
                "flats",
                flatRepository.findAll()
        );

        return "flats";
    }

    @GetMapping("/parking-slots")
    public String parkingSlots(Model model) {

        model.addAttribute(
                "slots",
                parkingSlotService.getAllSlots()
        );

        return "parking-slot";
    }

    @GetMapping("/daily-log")
    public String dailyLog(Model model) {

        model.addAttribute(
                "visitors",
                visitorVehicleRepository.findAll()
        );

        return "daily-log";
    }
}