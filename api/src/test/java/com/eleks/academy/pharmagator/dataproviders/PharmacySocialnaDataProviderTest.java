package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.socialna.parsers.SocialnaParser;
import com.eleks.academy.pharmagator.exceptions.PharmagatorApiException;
import com.eleks.academy.pharmagator.services.ImportService;
import com.eleks.academy.pharmagator.services.ImportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PharmacySocialnaDataProviderTest {

    private PharmacySocialnaDataProvider subject;

    private SocialnaParser socialnaParserMock;

    @BeforeEach
    void setUp() {
        socialnaParserMock = mock(SocialnaParser.class);

        MedicineDto dummyMedicine = MedicineDto.builder()
                .pharmacyName("socialna")
                .build();

        when(socialnaParserMock.getMedicinesFromPageByUrl(anyString()))
                .thenReturn(Stream.of(dummyMedicine));

        subject = new PharmacySocialnaDataProvider(socialnaParserMock);

        ReflectionTestUtils.setField(subject, "pagesLimit", 1);

        ReflectionTestUtils.setField(subject, "pharmacySocialnaBaseUrl", "https://1sa.com.ua/medikamenty");
    }

    @Test
    void loadData_ok() {
        List<MedicineDto> medicineDtos = subject.loadData().toList();

        assertEquals(1, medicineDtos.size());

        MedicineDto medicineDto = medicineDtos.get(0);

        assertEquals("socialna", medicineDto.getPharmacyName());
    }

    @Test
    void loadData_PharmagatorApiException(){
        when(socialnaParserMock.getMedicinesFromPageByUrl(anyString()))
                .thenThrow(new PharmagatorApiException("error"));

        ImportService importServiceMock = mock(ImportServiceImpl.class);

        Stream<MedicineDto> medicineDtoStream = subject.loadData();

        assertThrows(PharmagatorApiException.class, () -> medicineDtoStream.forEach(importServiceMock::storeToDatabase));
    }

}
