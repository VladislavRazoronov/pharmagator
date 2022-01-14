package com.eleks.academy.pharmagator.exceptions;

public class InvalidIdentifierException extends RuntimeException {

    public InvalidIdentifierException(Long id) {
        super("Invalid id: " + id);
    }

    public InvalidIdentifierException(Long pharmacyId, Long medicineId) {
        super(String.format("Invalid Price id: medicine_id: %d; pharmacy_id: %d", medicineId, pharmacyId));
    }

    public InvalidIdentifierException() {
        super("Invalid id");
    }

}
