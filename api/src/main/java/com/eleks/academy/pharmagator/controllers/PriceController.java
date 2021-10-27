package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceRepository priceRepository;

    private final MedicineRepository medicineRepository;

    private final PharmacyRepository pharmacyRepository;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<Price> getAll() {
        return priceRepository.findAll();
    }

    @GetMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<Price> getById(
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);

        return this.priceRepository.findById(priceId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<Price> update(
            @Valid @RequestBody PriceDto priceDto,
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {
        PriceId priceId = new PriceId(pharmacyId, medicineId);
        
        Function<PriceId, Optional<Price>> create = id ->
                this.pharmacyRepository.findById(id.getPharmacyId())
                        .map(pharmacy -> Price.builder()
                                .pharmacyId(pharmacy.getId())
                        )
                        .flatMap(builder ->
                                this.medicineRepository.findById(id.getMedicineId())
                                        .map(medicine -> builder
                                                .medicineId(medicine.getId())
                                        )
                        )
                        .map(builder -> builder
                                .price(priceDto.getPrice())
                                .externalId(priceDto.getExternalId()))
                        .map(Price.PriceBuilder::build);

        return this.priceRepository.findById(priceId)
                .map(source -> {
                    source.setPrice(priceDto.getPrice());
                    source.setExternalId(priceDto.getExternalId());
                    return source;
                }).or(() -> create.apply(priceId))
                .map(this.priceRepository::save)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<?> delete(
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {
        PriceId priceId = new PriceId(pharmacyId, medicineId);
        priceRepository.deleteById(priceId);
        return ResponseEntity.noContent().build();
    }
}
