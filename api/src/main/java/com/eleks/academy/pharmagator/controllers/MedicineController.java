package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/medicines")
public class MedicineController {
    private final MedicineRepository medicineRepository;

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }
    
    public ResponseEntity<Medicine> getById(long id){
        return ResponseEntity.ok(medicineRepository.getById(id));
    }

    public void create(String title){
        Medicine new_instance = new Medicine();
        new_instance.setTitle(title);
        medicineRepository.save(new_instance);
    }

    public void update(long id, String new_title){
        Medicine temp = medicineRepository.getById(id);
        temp.setTitle(new_title);
        medicineRepository.deleteById(id);
        medicineRepository.save(temp);
    }

    public void deleteById(long id){
        medicineRepository.deleteById(id);
    }
}
