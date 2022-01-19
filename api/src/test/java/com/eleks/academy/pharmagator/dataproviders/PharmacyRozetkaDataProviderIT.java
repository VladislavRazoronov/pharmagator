package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaMedicineResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaProductIdsResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.rozetka.RozetkaProductIdsResponseData;
import com.eleks.academy.pharmagator.exceptions.PharmagatorApiException;
import com.eleks.academy.pharmagator.services.ImportService;
import com.eleks.academy.pharmagator.services.ImportServiceImpl;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PharmacyRozetkaDataProviderIT {

    private PharmacyRozetkaDataProvider subject;

    private static MockWebServer mockWebServer;

    private final String BASE_URL = "https://apteka.rozetka.com.ua/ua/lekarstvennye-preparaty/c4628712/";

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        HttpUrl url = mockWebServer.url(BASE_URL);

        WebClient webClient = WebClient.create(url.toString());

        subject = new PharmacyRozetkaDataProvider(webClient);

        ReflectionTestUtils.setField(subject, "pagesLimit", 1);

        ReflectionTestUtils.setField(subject, "pharmacyRozetkaBaseUrl", BASE_URL);
    }

    @Test
    void loadData_ok() {
        List<MedicineDto> medicineDtos = subject.loadData().toList();

        assertEquals(1, medicineDtos.size());

        MedicineDto medicineDto = medicineDtos.get(0);
      
        assertEquals(1, medicineDto.getPrice().compareTo(new BigDecimal(0)));
    }

    @Test
    void loadData_PharmagatorApiException(){

        ImportService importServiceMock = mock(ImportServiceImpl.class);

        Stream<MedicineDto> medicineDtoStream = subject.loadData();

        assertThrows(PharmagatorApiException.class, () -> medicineDtoStream.forEach(importServiceMock::storeToDatabase));
    }

}
