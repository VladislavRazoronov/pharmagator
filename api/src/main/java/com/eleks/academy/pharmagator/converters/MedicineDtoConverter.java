package com.eleks.academy.pharmagator.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import lombok.Getter;

import java.time.Instant;

@Getter
public class MedicineDtoConverter {
    public Medicine medicine;
    public Price price;
    public MedicineDtoConverter(MedicineDto data){
        medicine = new Medicine();
        price = new Price();
        medicine.setTitle(data.getTitle());
        price.setMedicineId(medicine.getId());
        price.setPrice(data.getPrice());
        price.setExternalId(data.getExternalId());
        price.setUpdatedAt(Instant.now());
    }
}
