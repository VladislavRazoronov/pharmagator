package com.eleks.academy.pharmagator.dataproviders.dto.socialna.parsers;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.exceptions.PharmagatorApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SocialnaParserTestIT {

    private static MockWebServer mockWebServer;

    private SocialnaParser subject;

    private static final String PHARMACY_NAME = "apteka-911-socialna";

    private static String URL;

    @Value("classpath:html/socialna-test-page.html")
    private Resource htmlTestPageResource;

    @Value("classpath:html/socialna-test-malformed-page.html")
    private Resource htmlMalformedTestPageResource;

    @BeforeAll
    static void beforeAll() {
        mockWebServer = new MockWebServer();

        mockWebServer.url("/");

        URL = "http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort() + "/";
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        subject = new SocialnaParser();

        ReflectionTestUtils.setField(subject, "pharmacyName", PHARMACY_NAME);
    }

    private String getHtmlPageAsString(Resource htmlPageResource) throws IOException {
        return IOUtils.toString(htmlPageResource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    void getMedicinesFromPageByUrl_ok() throws InterruptedException, IOException {
        MedicineDto expectedResult = MedicineDto.builder()
                .pharmacyName(PHARMACY_NAME)
                .title("Nailner (Нейлнер) 2в1 протигрибковий лак")
                .price(new BigDecimal("347.50"))
                .externalId("48080")
                .build();

        String htmlPage = getHtmlPageAsString(htmlTestPageResource);

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                .setBody(htmlPage)
        );

        Stream<MedicineDto> resultStream = subject.getMedicinesFromPageByUrl(URL);

        mockWebServer.takeRequest();

        List<MedicineDto> resultDataSet = resultStream.toList();

        assertEquals(1, resultDataSet.size());

        MedicineDto resultSetElement = resultDataSet.get(0);

        assertEquals(expectedResult, resultSetElement);
    }

    @Test
    void getMedicinesFromPageByUrl_malformedUrl_IAE() {
        assertThrows(IllegalArgumentException.class, () -> subject.getMedicinesFromPageByUrl("qwerty"));
    }

    @Test
    void getMedicinesFromPageByUrl_malformedHtmlPage_PharmagatorApiException() throws IOException, InterruptedException {
        String htmlPage = getHtmlPageAsString(htmlMalformedTestPageResource);

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                .setBody(htmlPage)
        );

        try {
            subject.getMedicinesFromPageByUrl(URL);
        } catch (PharmagatorApiException e) {
            assertNotNull(e);

            assertEquals("Failed to map element to MedicineDto in SocialnaParser", e.getMessage());
        }

        mockWebServer.takeRequest();
    }

    @Test
    void getMedicinesFromPageByUrl_badRequest_PharmagatorApiException() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
        );

        PharmagatorApiException exception = assertThrows(PharmagatorApiException.class, () -> subject.getMedicinesFromPageByUrl(URL));

        String errorMessage = exception.getMessage();

        assertTrue(errorMessage.startsWith("An error occurred in SocialnaParser during getMedicinesFromPageByUrl()"));

        mockWebServer.takeRequest();
    }

}
