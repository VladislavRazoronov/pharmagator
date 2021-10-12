package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {
    private final PharmacyRepository pharmacyRepository;

    @GetMapping
    public ResponseEntity<List<Pharmacy>> getAll() {
        return ResponseEntity.ok(pharmacyRepository.findAll());
    }

    public ResponseEntity<Pharmacy> getById(long id){
        return ResponseEntity.ok(pharmacyRepository.getById(id));
    }

    public void create(String name, String medicine_link_template){
        Pharmacy new_instance = new Pharmacy();
        new_instance.setName(name);
        new_instance.setMedicineLinkTemplate(medicine_link_template);
        pharmacyRepository.save(new_instance);
    }

    public void update(long id, String new_name, String new_link){
        Pharmacy temp = pharmacyRepository.getById(id);
        temp.setName(new_name);
        temp.setMedicineLinkTemplate(new_link);
        pharmacyRepository.deleteById(id);
        pharmacyRepository.save(temp);
    }

    public void deleteById(long id){
        pharmacyRepository.deleteById(id);
    }
}
