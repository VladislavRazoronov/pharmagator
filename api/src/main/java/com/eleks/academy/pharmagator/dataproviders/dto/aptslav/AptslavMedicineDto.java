package com.eleks.academy.pharmagator.dataproviders.dto.aptslav;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AptslavMedicineDto {

    private long id;

    private long externalId;

    private String name;

    private AptslavPriceDto price;

    private Instant created;

}
