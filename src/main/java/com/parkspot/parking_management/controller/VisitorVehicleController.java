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

    public VisitorVehicleController(
            VisitorVehicleService service) {
        this.service = service;
    }

    @PostMapping
    public VisitorVehicle registerVisitor(
            @RequestBody VisitorVehicle visitor,
            @RequestParam Long flatId,
            @RequestParam Long slotId) {
        return service.registerVisitor(visitor, flatId, slotId);
    }

    @GetMapping
    public List<VisitorVehicle> getAllVisitors() {
        return service.getAllVisitors();
    }

    @GetMapping("/{id}")
    public VisitorVehicle getVisitorById(
            @PathVariable Long id) {
        return service.getVisitorById(id);
    }

    @GetMapping("/active")
    public List<VisitorVehicle> getActiveVisitors() {
        return service.getActiveVisitors();
    }

    @PutMapping("/{id}/exit")
    public VisitorVehicle recordExit(@PathVariable Long id) {
        return service.recordExit(id);
    }

    @GetMapping("/daily-log")
    public List<VisitorVehicle> getDailyLog(
            @RequestParam LocalDate date) {
        return service.getDailyLog(date);
    }
}