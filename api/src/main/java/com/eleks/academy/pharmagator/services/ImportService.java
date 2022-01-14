package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface ImportService {

    void storeToDatabase(MedicineDto dto);

}
