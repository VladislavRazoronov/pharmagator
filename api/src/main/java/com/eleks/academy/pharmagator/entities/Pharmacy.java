package com.eleks.academy.pharmagator.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "pharmacies")
public class Pharmacy {
    @Id
    private long id;
    private String name;
    private String medicineLinkTemplate;
}
