package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.projections.MedicinePrice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    @Query("""
            SELECT p.price as price, m.title as title, p.pharmacyId as pharmacyId
            FROM Price p
            LEFT JOIN Medicine m ON m.id = p.medicineId
            """)
    List<MedicinePrice> findAllMedicinesPrices(Pageable pageable);

    @Query("""
            SELECT p.price as price, m.title as title, p.pharmacyId as pharmacyId
            FROM Price p
            LEFT JOIN Medicine m ON m.id = p.medicineId
            WHERE title LIKE %?1%
            """)
    List<MedicinePrice> searchMedicinesPrices(String keyword);

    Optional<Price> findByPrice(BigDecimal price);
}
