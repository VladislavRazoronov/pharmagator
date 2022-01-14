package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.exceptions.ExportExceptions;
import com.eleks.academy.pharmagator.projections.MedicinePrice;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.eleks.academy.pharmagator.services.projections.MedicinePriceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PDFExportServiceTest {

    @InjectMocks
    private PDFExportService pdfExportService;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PriceRepository priceRepository;

    private List<Pharmacy> pharmacies;
    private List<MedicinePrice> prices;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        pharmacies = Arrays.asList(
                new Pharmacy(1L, "ANC", "Link"),
                new Pharmacy(2L, "3I", "Link"),
                new Pharmacy(3L, "Liki24", "Link")
        );

        prices = Arrays.asList(
                new MedicinePriceImpl("Aspirin", 1L, BigDecimal.valueOf(25.25)),
                new MedicinePriceImpl("Aspirin", 2L, BigDecimal.valueOf(35.25)),
                new MedicinePriceImpl("Aspirin", 3L, BigDecimal.valueOf(45.25)),
                new MedicinePriceImpl("Fervex", 1L, BigDecimal.valueOf(22.25)),
                new MedicinePriceImpl("Fervex", 2L, BigDecimal.valueOf(32.25)),
                new MedicinePriceImpl("Fervex", 3L, BigDecimal.valueOf(42.25))
        );
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prices", "pharmacies", "medicines");
    }

    @Test
    @SneakyThrows
    void exportToPdf_isOk() {
        String filepath = "src/test/resources/export/export.pdf";
        File file = new File(filepath);

        when(pharmacyRepository.findAll()).thenReturn(pharmacies);
        when(priceRepository.findAllMedicinesPrices(null)).thenReturn(prices);

        FieldUtils.writeField(pdfExportService, "pathToFont", "src/main/resources/fonts/FreeSerif.ttf", true);

        byte[] bytes = pdfExportService.export();
        FileOutputStream fileInputStream = new FileOutputStream(file.getAbsolutePath());
        fileInputStream.write(bytes);

        PdfReader pdfReader = new PdfReader(file.getAbsolutePath());
        String textFromPage = PdfTextExtractor.getTextFromPage(pdfReader, 1);

        pharmacies.stream()
                .map(Pharmacy::getName)
                .forEach(name -> assertTrue(textFromPage.contains(name)));

        prices.forEach(medicinePrice -> {
            assertTrue(textFromPage.contains(medicinePrice.getTitle()));
            assertTrue(textFromPage.contains(String.valueOf(medicinePrice.getPrice())));
        });
    }

    @Test
    void getPdfWriter_throwsDocumentException() {
        try (MockedStatic<PdfWriter> pdfWriter = Mockito.mockStatic(PdfWriter.class)) {
            pdfWriter.when(() -> PdfWriter.getInstance(any(Document.class), any(ByteArrayOutputStream.class)))
                    .thenThrow(new DocumentException());

            ExportExceptions exception = assertThrows(ExportExceptions.class, () -> pdfExportService.export());

            assertEquals(ExportExceptions.Error.GET_PDF_WRITER_IS_BAD, exception.getError());
        }
    }

    @Test
    void createBaseFont_throwsDocumentException() {
        when(pharmacyRepository.findAll()).thenReturn(pharmacies);
        when(priceRepository.findAllMedicinesPrices(null)).thenReturn(prices);

        try (MockedStatic<BaseFont> baseFont = Mockito.mockStatic(BaseFont.class)) {
            baseFont.when(() -> BaseFont.createFont(anyString(), anyString(), anyBoolean()))
                    .thenThrow(new DocumentException());

            ExportExceptions exception = assertThrows(ExportExceptions.class, () -> pdfExportService.export());

            assertEquals(ExportExceptions.Error.INVALID_FONT, exception.getError());
        }
    }

    @Test
    void createBaseFont_throwsIOException() {
        when(pharmacyRepository.findAll()).thenReturn(pharmacies);
        when(priceRepository.findAllMedicinesPrices(null)).thenReturn(prices);

        try (MockedStatic<BaseFont> baseFont = Mockito.mockStatic(BaseFont.class)) {
            baseFont.when(() -> BaseFont.createFont(anyString(), anyString(), anyBoolean()))
                    .thenThrow(new IOException());

            ExportExceptions exception = assertThrows(ExportExceptions.class, () -> pdfExportService.export());

            assertEquals(ExportExceptions.Error.INVALID_FONT, exception.getError());
        }
    }

}
