package com.parkspot.parking_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parkspot.parking_management.model.VisitorVehicle;
import com.parkspot.parking_management.service.VisitorVehicleService;

@RestController
@RequestMapping("/api/visitors")
public class VisitorVehicleController {

    private final VisitorVehicleService service;

    public VisitorVehicleController(VisitorVehicleService service) {
        this.service = service;
    }

    // Register visitor
    @PostMapping
    public VisitorVehicle registerVisitor(
            @RequestBody VisitorVehicle visitor,
            @RequestParam(name = "flatId") Long flatId,
            @RequestParam(name = "slotId") Long slotId) {

        return service.registerVisitor(
                visitor,
                flatId,
                slotId);
    }

    // Get all visitors
    @GetMapping
    public List<VisitorVehicle> getAllVisitors() {

        return service.getAllVisitors();
    }

    // Get visitor by ID
    @GetMapping("/{id}")
    public VisitorVehicle getVisitorById(
            @PathVariable(name = "id") Long id) {

        return service.getVisitorById(id);
    }

    // Get active visitors
    @GetMapping("/active")
    public List<VisitorVehicle> getActiveVisitors() {

        return service.getActiveVisitors();
    }

    // Record visitor exit
    @PutMapping("/{id}/exit")
    public VisitorVehicle recordExit(
            @PathVariable(name = "id") Long id) {

        return service.recordExit(id);
    }

    // Get daily visitor log
    @GetMapping("/daily-log")
    public List<VisitorVehicle> getDailyLog(
            @RequestParam(name = "date") LocalDate date) {

        return service.getDailyLog(date);
    }
}