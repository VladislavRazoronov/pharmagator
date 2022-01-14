package com.eleks.academy.pharmagator.dataproviders.dto.aptslav.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.AptslavMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.AptslavPriceDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ApiMedicineDtoConverterTest {

    private final ApiMedicineDtoConverter subject = new ApiMedicineDtoConverter();

    @Test
    void toMedicineDto_ok() {
        final long expectedId = 1L;

        final String expectedTitle = "title";

        AptslavPriceDto expectedPriceDto = AptslavPriceDto.builder()
                .min(BigDecimal.valueOf(10))
                .max(BigDecimal.valueOf(20))
                .build();

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(expectedId)
                .id(expectedId)
                .created(Instant.now())
                .name(expectedTitle)
                .price(expectedPriceDto)
                .build();

        MedicineDto medicineDto = subject.toMedicineDto(aptslavMedicineDto);

        assertEquals(expectedPriceDto.getMin(), medicineDto.getPrice());

        assertEquals(String.valueOf(expectedId), medicineDto.getExternalId());

        assertEquals(expectedTitle, medicineDto.getTitle());

        assertNull(medicineDto.getPharmacyName());
    }

    @Test
    void toMedicineDto_passedNullArg_NPE() {
        assertThrows(NullPointerException.class, () -> subject.toMedicineDto(null));
    }

    @Test
    void toMedicineDto_apiDtoHasNullPrice_IAE() {
        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(1L)
                .id(1L)
                .created(Instant.now())
                .name("title")
                .price(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

    @Test
    void toMedicineDto_apiDtoHasBlankTitle_IAE() {
        AptslavPriceDto price = new AptslavPriceDto();

        price.setMin(BigDecimal.valueOf(10));

        price.setMax(BigDecimal.valueOf(25));

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(1L)
                .id(1L)
                .created(Instant.now())
                .name(" ")
                .price(price)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

    @Test
    void toMedicineDto_apiDtoHasNullTitle_IAE() {
        AptslavPriceDto price = new AptslavPriceDto();

        price.setMin(BigDecimal.valueOf(10));

        price.setMax(BigDecimal.valueOf(25));

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(1L)
                .id(1L)
                .created(Instant.now())
                .name(null)
                .price(price)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

    @Test
    void toMedicineDto_apiDtoHasNegativeMinPrice_IAE() {
        AptslavPriceDto price = new AptslavPriceDto();

        price.setMin(BigDecimal.valueOf(-1));

        price.setMax(BigDecimal.valueOf(10));

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(1L)
                .id(1L)
                .created(Instant.now())
                .name("title")
                .price(price)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

    @Test
    void toMedicineDto_apiDtoHasNegativeMaxPrice_IAE() {
        AptslavPriceDto price = new AptslavPriceDto();

        price.setMin(BigDecimal.valueOf(10));

        price.setMax(BigDecimal.valueOf(-1));

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(1L)
                .id(1L)
                .created(Instant.now())
                .name("title")
                .price(price)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

    @Test
    void toMedicineDto_apiDtoHasNegativeExtId_IAE() {
        AptslavPriceDto price = new AptslavPriceDto();

        price.setMin(BigDecimal.valueOf(10));

        price.setMax(BigDecimal.valueOf(25));

        AptslavMedicineDto aptslavMedicineDto = AptslavMedicineDto.builder()
                .externalId(-100L)
                .id(1L)
                .created(Instant.now())
                .name("title")
                .price(price)
                .build();

        assertThrows(IllegalArgumentException.class, () -> subject.toMedicineDto(aptslavMedicineDto));
    }

}
