package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.input.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public Page<Medicine> findAllPaginated(Pageable pageable) {
        return medicineRepository.findAll(pageable);
    }

    @Override
    public Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    @Override
    public Medicine save(MedicineDto medicineDto) {
        Medicine medicine = modelMapper.map(medicineDto, Medicine.class);
        return medicineRepository.save(medicine);
    }

    @Override
    public Optional<Medicine> update(Long id, MedicineDto medicineDto) {
        return medicineRepository.findById(id)
                .map(source -> {
                    Medicine medicine = modelMapper.map(medicineDto, Medicine.class);
                    medicine.setId(id);
                    return medicineRepository.save(medicine);
                });
    }

    public void deleteById(Long id) {
        try {
            medicineRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            log.info(exception.getMessage(), exception);
        }
    }

}
