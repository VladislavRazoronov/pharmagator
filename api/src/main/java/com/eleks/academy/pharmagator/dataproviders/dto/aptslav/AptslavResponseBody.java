package com.eleks.academy.pharmagator.dataproviders.dto.aptslav;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AptslavResponseBody<T> {

    private List<T> data;

    private long count;

}
