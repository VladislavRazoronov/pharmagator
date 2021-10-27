package com.eleks.academy.pharmagator.controllers.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class PriceDto {
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;
    @NotBlank(message = "External id cannot be null or empty")
    private String externalId;
}
