package com.eleks.academy.pharmagator.dataproviders.dto.aptslav;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AptslavResponseEntity {

    private long id;

    private String externalId;

    private String name;

    private String manufacturer;

    private String created;

    private AptslavPriceDto price;

}
