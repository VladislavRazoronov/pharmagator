package com.eleks.academy.pharmagator.dataproviders.impl;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class DataProviderImpl implements DataProvider {
    @Override
    public Stream<MedicineDto> loadData() {

        return IntStream.rangeClosed(1, 100).mapToObj(this::buildDto);
    }

    private MedicineDto buildDto(int i) {
        return MedicineDto.builder()
                .externalId(String.valueOf(i))
                .title("title " + i)
                .price(BigDecimal.valueOf(Math.random()))
                .build();
    }
}
