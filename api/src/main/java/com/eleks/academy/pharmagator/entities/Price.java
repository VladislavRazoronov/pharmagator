package com.eleks.academy.pharmagator.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prices")
@IdClass(PriceId.class)
public class Price {
    @Id
    private long pharmacyId;
    @Id
    private long medicineId;
    private BigDecimal price;
    private String externalId;
    @Column(insertable = false, updatable = false)
    private Instant updatedAt;
}
