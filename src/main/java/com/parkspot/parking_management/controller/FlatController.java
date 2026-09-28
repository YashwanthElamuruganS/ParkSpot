package com.parkspot.parking_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkspot.parking_management.model.Flat;
import com.parkspot.parking_management.service.FlatService;

@RestController
@RequestMapping("/api/flats")
public class FlatController {

    private final FlatService service;

    public FlatController(FlatService service) {
        this.service = service;
    }

    @PostMapping
    public Flat addFlat(@RequestBody Flat flat) {
        return service.addFlat(flat);
    }

    @GetMapping
    public List<Flat> getAllFlats() {
        return service.getAllFlats();
    }

    @GetMapping("/{id}")
    public Flat getFlatById(@PathVariable Long id) {
        return service.getFlatById(id);
    }
}