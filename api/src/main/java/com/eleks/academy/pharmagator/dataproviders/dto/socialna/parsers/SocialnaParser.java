package com.eleks.academy.pharmagator.dataproviders.dto.socialna.parsers;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.exceptions.PharmagatorApiException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Qualifier(value = "socialnaParser")
public class SocialnaParser {

    @Value("${pharmagator.data-providers.apteka-socialna.pharmacy-name}")
    private String pharmacyName;

    public Stream<MedicineDto> getMedicinesFromPageByUrl(String url) {
        try {
            return Jsoup.connect(url).get().select(".product-item-details").stream()
                    .filter(e -> !e.children().eachText().contains("Відсутній на складі"))
                    .map(this::mapElementToMedicineDto);
        } catch (IOException e) {
            throw new PharmagatorApiException("An error occurred " +
                    "in SocialnaParser during getMedicinesFromPageByUrl().\n" +
                    e.getMessage());
        }
    }

    private MedicineDto mapElementToMedicineDto(Element element) {
        String title = getElementByCssQuery(element, "span.gproductname");

        String price = getElementByCssQuery(element, "span.gproductprice");

        String externalId = getElementByCssQuery(element, "span.gproductid");

        return MedicineDto.builder()
                .title(title)
                .price(new BigDecimal(price))
                .externalId(externalId)
                .pharmacyName(pharmacyName)
                .build();
    }

    private String getElementByCssQuery(Element parentElement, String cssQuerySelector) {
        return Optional.ofNullable(parentElement.select(cssQuerySelector).first())
                .map(Element::ownText)
                .orElseThrow(() -> new PharmagatorApiException("Failed to map element to MedicineDto in SocialnaParser"));
    }

}
