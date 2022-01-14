package com.eleks.academy.pharmagator.dataproviders.dto.aptslav;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AptslavPriceDto {

    private BigDecimal max;

    private BigDecimal min;

}
