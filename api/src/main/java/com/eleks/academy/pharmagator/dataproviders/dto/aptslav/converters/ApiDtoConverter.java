package com.eleks.academy.pharmagator.dataproviders.dto.aptslav.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface ApiDtoConverter<T> {

    MedicineDto toMedicineDto(T apiDto);

}
