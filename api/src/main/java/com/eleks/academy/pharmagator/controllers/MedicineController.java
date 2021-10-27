package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineRepository medicineRepository;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<Medicine> getAll() {

        return medicineRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable("id") Long id) {

        return this.medicineRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@Valid @RequestBody MedicineDto medicineDto) {
        Medicine medicine = modelMapper.map(medicineDto, Medicine.class);

        return ResponseEntity.ok(medicineRepository.save(medicine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(
            @Valid @RequestBody MedicineDto medicineDto,
            @PathVariable("id") Long id) {
        return medicineRepository.findById(id)
                .map(source -> {
                    source = modelMapper.map(medicineDto, Medicine.class);
                    return ResponseEntity.ok(medicineRepository.save(source));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        medicineRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
