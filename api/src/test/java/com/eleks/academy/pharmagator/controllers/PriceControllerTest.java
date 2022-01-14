package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.input.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.services.PriceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceController priceController;

    @Mock
    private final ModelMapper modelMapper = new ModelMapper();

    private final String CONTENT = """ 
            { "price": 11 , "externalId": "11" }
            """;

    private static Price price1;
    private static Price price2;
    private static PriceDto priceDto1;
    private static ArrayList<Price> priceList;

    private static final Long MEDICINE_ID = 1L;
    private static final Long PHARMACY_ID = 1L;
    private static final String EXTERNAL_ID = "11";
    private static final double PRICE = 11;

    private final Long WRONG_PHARMACY_ID = 23L;
    private final Long WRONG_MEDICINE_ID = 45L;

    private final String URI = "/prices";
    private final String URI_PRICE = "/pharmacyId/" + PHARMACY_ID + "/medicineId/" + MEDICINE_ID;
    private final String URI_NON_EXISTENT_PRICE = String.format("/pharmacyId/%d/medicineId/%d", WRONG_PHARMACY_ID, WRONG_MEDICINE_ID);

    @BeforeAll
    static void setUpComponents() {
        price1 = new Price();
        price1.setPrice(BigDecimal.valueOf(PRICE));
        price1.setPharmacyId(PHARMACY_ID);
        price1.setMedicineId(MEDICINE_ID);
        price1.setExternalId(EXTERNAL_ID);

        price2 = new Price();
        price2.setPrice(BigDecimal.valueOf(PRICE));
        price2.setPharmacyId(2L);
        price2.setMedicineId(MEDICINE_ID);
        price2.setExternalId(EXTERNAL_ID);

        priceDto1 = new PriceDto();
        priceDto1.setPrice(BigDecimal.valueOf(11));
        priceDto1.setExternalId(EXTERNAL_ID);

        priceList = new ArrayList<>();
        priceList.add(price1);
        priceList.add(price2);
    }

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(priceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAllPrices_isOk() throws Exception {
        when(priceService.findAll()).thenReturn(priceList);
        when(modelMapper.map(price1, PriceDto.class)).thenReturn(priceDto1);
        when(modelMapper.map(price2, PriceDto.class)).thenReturn(priceDto1);

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].price", Matchers.hasItems(11)));

        verify(priceService).findAll();
    }

    @Test
    void getById_isOk() throws Exception {
        when(modelMapper.map(price1, PriceDto.class)).thenReturn(priceDto1);
        when(priceService.findById(PHARMACY_ID, MEDICINE_ID)).thenReturn(Optional.of(price1));

        mockMvc.perform(get(URI + URI_PRICE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.price").value(priceDto1.getPrice()));

        verify(priceService).findById(PHARMACY_ID, MEDICINE_ID);
    }

    @Test
    void getById_noExistsId_NotFound() {
        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get(URI + URI_NON_EXISTENT_PRICE))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        });

        String expectedMessage = String.format("Invalid Price id: medicine_id: %d; pharmacy_id: %d", WRONG_MEDICINE_ID, WRONG_PHARMACY_ID);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updatePriceById_isOk() throws Exception {
        when(priceService.update(PHARMACY_ID, MEDICINE_ID, priceDto1)).thenReturn(Optional.of(price1));
        when(modelMapper.map(price1, PriceDto.class)).thenReturn(priceDto1);

        mockMvc.perform(put(URI + URI_PRICE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(priceDto1.getPrice()));

        verify(priceService).update(PHARMACY_ID, MEDICINE_ID, priceDto1);
    }

    @Test
    void updatePriceById_noExistsId_NotFound() throws Exception {
        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(put(URI + URI_NON_EXISTENT_PRICE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CONTENT))
                    .andExpect(status().isNotFound());
        });

        String expectedMessage = String.format("Invalid Price id: medicine_id: %d; pharmacy_id: %d", WRONG_MEDICINE_ID, WRONG_PHARMACY_ID);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete_isOk() throws Exception {
        mockMvc.perform(delete(URI + URI_PRICE)).andExpect(status().isNoContent());
        verify(priceService).deleteById(PHARMACY_ID, MEDICINE_ID);
    }

}

