package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final PriceRepository priceRepository;

    private final PharmacyRepository pharmacyRepository;

    private final MedicineRepository medicineRepository;

    private final ModelMapper modelMapper;

    @Override
    public void storeToDatabase(MedicineDto dto) {
        Medicine medicine = modelMapper.map(dto, Medicine.class);
        Price price = modelMapper.map(dto, Price.class);

        String pharmacyName = dto.getPharmacyName();
        Pharmacy pharmacyFromDb = pharmacyRepository.findByName(pharmacyName)
                .orElseGet(() -> {
                    Pharmacy pharmacy = new Pharmacy();
                    pharmacy.setName(pharmacyName);
                    return pharmacyRepository.save(pharmacy);
                });

        Medicine medicineFromDb = medicineRepository.findByTitle(medicine.getTitle())
                .orElseGet(() -> medicineRepository.save(medicine));

        price.setMedicineId(medicineFromDb.getId());
        price.setPharmacyId(pharmacyFromDb.getId());
        price.setUpdatedAt(Instant.now());
        price.setExternalId(dto.getExternalId());
        priceRepository.save(price);

    }

}
