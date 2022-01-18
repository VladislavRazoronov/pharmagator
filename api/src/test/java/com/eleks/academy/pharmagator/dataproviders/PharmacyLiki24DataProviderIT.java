package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PharmacyLiki24DataProviderIT {

    private PharmacyLiki24DataProvider subject;

    private static MockWebServer mockWebServer;

    private final String BASE_URL = "https://liki24.com/uk/category/8000007/";

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

        subject = new PharmacyLiki24DataProvider(webClient);

        ReflectionTestUtils.setField(subject, "pagesLimit", 1);

        ReflectionTestUtils.setField(subject, "pharmacyLiki24BaseUrl", BASE_URL);
    }

    @Test
    void loadData_ok() {
        List<MedicineDto> medicineDtos = subject.loadData().toList();

        assertEquals(1, medicineDtos.size());

        MedicineDto medicineDto = medicineDtos.get(0);

        assertEquals("Liki24", medicineDto.getPharmacyName());
    }

    @Test
    void loadData_PharmagatorApiException(){

        ImportService importServiceMock = mock(ImportServiceImpl.class);

        Stream<MedicineDto> medicineDtoStream = subject.loadData();

        assertThrows(PharmagatorApiException.class, () -> medicineDtoStream.forEach(importServiceMock::storeToDatabase));
    }

}
