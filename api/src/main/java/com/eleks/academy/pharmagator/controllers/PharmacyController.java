package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {
    private final PharmacyRepository pharmacyRepository;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getById(@PathVariable("id") Long id) {

        return this.pharmacyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pharmacy> create(@Valid @RequestBody PharmacyDto pharmacyDto) {
        Pharmacy pharmacy = modelMapper.map(pharmacyDto,Pharmacy.class);

        return ResponseEntity.ok(pharmacyRepository.save(pharmacy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> update(
            @Valid @RequestBody PharmacyDto pharmacyDto,
            @PathVariable("id") Long id) {
        return pharmacyRepository.findById(id)
                .map(source -> {
                    source = modelMapper.map(pharmacyDto,Pharmacy.class);
                    return ResponseEntity.ok(pharmacyRepository.save(source));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        pharmacyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
