package com.eleks.academy.pharmagator.dataproviders.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    @Min(value = 0)
    @NotNull
    private BigDecimal price;

    @NotBlank
    private String externalId;

}
