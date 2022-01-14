package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ImportServiceImplTest {

    private ImportServiceImpl importService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MedicineDto medicineDto;


    @BeforeEach
    void setUp() {
        importService = new ImportServiceImpl(priceRepository, pharmacyRepository, medicineRepository, modelMapper);

        medicineDto = new MedicineDto();
        medicineDto.setPharmacyName("pharmacy-anc");
        medicineDto.setTitle("Aspirin");
        medicineDto.setExternalId("ExternalID");
        medicineDto.setPrice(new BigDecimal("25.25"));
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prices", "pharmacies", "medicines");
    }

    @Test
    void storeToDatabase_isOk() {
        importService.storeToDatabase(medicineDto);

        medicineRepository.findByTitle(medicineDto.getTitle())
                .ifPresent(medicine -> assertEquals(medicine.getTitle(), medicineDto.getTitle()));

        pharmacyRepository.findByName(medicineDto.getPharmacyName())
                .ifPresent(pharmacy -> assertEquals(pharmacy.getName(), medicineDto.getPharmacyName()));

        priceRepository.findByPrice(medicineDto.getPrice())
                .ifPresent(price -> {
                    assertEquals(price.getPrice(), medicineDto.getPrice());
                    assertEquals(price.getExternalId(), medicineDto.getExternalId());
                });
    }

}
