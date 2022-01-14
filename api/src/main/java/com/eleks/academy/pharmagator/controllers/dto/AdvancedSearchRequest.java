package com.eleks.academy.pharmagator.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedSearchRequest {

    private List<String> pharmacies;
    private String medicine;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

}
