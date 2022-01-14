package com.eleks.academy.pharmagator.controllers.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PharmacyDto {
    @NotBlank(message = "Pharmacy name cannot be null")
    private String name;
    private String medicineLinkTemplate;
}
