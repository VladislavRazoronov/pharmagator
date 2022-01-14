package com.eleks.academy.pharmagator.dataproviders.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDto {

    @NotBlank
    private String name;

    private String medicineLinkTemplate;

}
