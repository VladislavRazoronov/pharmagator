package com.eleks.academy.pharmagator.repositories.specifications;

import com.eleks.academy.pharmagator.controllers.dto.AdvancedSearchRequest;
import com.eleks.academy.pharmagator.entities.AdvancedSearchView;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record AdvancedSearchViewSpecification(AdvancedSearchRequest request) implements Specification<AdvancedSearchView> {

    @Override
    public Predicate toPredicate(@NonNull Root<AdvancedSearchView> root,
                                 @NonNull CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Optional.ofNullable(request.getPharmacies())
                .filter(pharmacies -> !pharmacies.isEmpty())
                .map(pharmacies -> root.get("pharmacy").in(pharmacies))
                .ifPresent(predicates::add);

        Optional.ofNullable(request.getMedicine())
                .filter(s -> !s.isBlank())
                .map(medicine -> criteriaBuilder.like(root.get("medicine"), "%" + medicine + "%"))
                .ifPresent(predicates::add);

        Optional.ofNullable(request.getPriceFrom())
                .map(price -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price))
                .ifPresent(predicates::add);

        Optional.ofNullable(request.getPriceTo())
                .map(price -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price))
                .ifPresent(predicates::add);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
