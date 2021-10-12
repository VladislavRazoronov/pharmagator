package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/prices")
public class PriceController {
    private final PriceRepository priceRepository;

    @GetMapping
    public ResponseEntity<List<Price>> getAll() {
        return ResponseEntity.ok(priceRepository.findAll());
    }

    public ResponseEntity<Price> getById(long id){
        return ResponseEntity.ok(priceRepository.getById(id));
    }

    public void create(int pharm_id,int med_id,BigDecimal price, String external_id){
        Price new_instance = new Price();
        new_instance.setPharmacyId(pharm_id);
        new_instance.setMedicineId(med_id);
        new_instance.setPrice(price);
        new_instance.setExternalId(external_id);
        new_instance.setUpdatedAt(Instant.now());
        priceRepository.save(new_instance);
    }

    public void update(long id, BigDecimal new_price, String new_external_id){
        Price temp = priceRepository.getById(id);
        temp.setPrice(new_price);
        temp.setExternalId(new_external_id);
        temp.setUpdatedAt(Instant.now());
        priceRepository.deleteById(id);
        priceRepository.save(temp);
    }

    public void deleteById(long id){
        priceRepository.deleteById(id);
    }
}
