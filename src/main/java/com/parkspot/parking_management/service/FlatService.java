package com.parkspot.parking_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.parkspot.parking_management.model.Flat;
import com.parkspot.parking_management.repository.FlatRepository;

@Service
public class FlatService {

    private final FlatRepository repository;

    public FlatService(FlatRepository repository) {
        this.repository = repository;
    }

    public Flat addFlat(Flat flat) {
        return repository.save(flat);
    }

    public List<Flat> getAllFlats() {
        return repository.findAll();
    }

    public Flat getFlatById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Flat not found"));
    }
}