package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository repository;

    @Test
    void deleteById_ok() {
        Long wrongPharmacyId = 1000002L;
        Long wrongMedicineId = 1000003L;
        PriceId wrongPriceId = new PriceId(wrongPharmacyId, wrongMedicineId);

        String exceptionMessage = String.format("No class %s entity with id PriceId(pharmacyId=%d, medicineId=%d) exists!", Price.class.getCanonicalName(), wrongPharmacyId, wrongMedicineId);

        doThrow(new EmptyResultDataAccessException(exceptionMessage, 0)).when(repository).deleteById(wrongPriceId);

        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(wrongPriceId);
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exceptionMessage));
    }

}
