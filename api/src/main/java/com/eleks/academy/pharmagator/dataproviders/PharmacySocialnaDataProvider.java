package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.socialna.parsers.SocialnaParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("pharmacySocialnaDataProvider")
public class PharmacySocialnaDataProvider implements DataProvider {

    @Value("${pharmagator.data-providers.apteka-socialna.url}")
    private String pharmacySocialnaBaseUrl;

    @Value("${pharmagator.data-providers.apteka-socialna.page-limit}")
    private Integer pagesLimit;

    private final SocialnaParser socialnaParser;

    @Override
    public Stream<MedicineDto> loadData() {
        return generateAllUrls()
                .flatMap(socialnaParser::getMedicinesFromPageByUrl);
    }

    private Stream<String> generateAllUrls() {
        return IntStream.rangeClosed(1, pagesLimit)
                .mapToObj((int i) -> pharmacySocialnaBaseUrl + "?p=" + i);
    }

}
