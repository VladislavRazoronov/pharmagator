package com.eleks.academy.pharmagator.services.projections;

import com.eleks.academy.pharmagator.projections.MedicinePrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePriceImpl implements MedicinePrice {

    private String title;

    private Long pharmacyId;

    private BigDecimal price;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public long getPharmacyId() {
        return pharmacyId;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

}
