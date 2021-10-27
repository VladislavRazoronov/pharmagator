package com.eleks.academy.pharmagator.entities;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceId implements Serializable {
    private long pharmacyId;
    private long medicineId;
}
