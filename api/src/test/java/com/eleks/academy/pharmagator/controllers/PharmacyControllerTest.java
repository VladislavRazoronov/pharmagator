package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.config.ModelMapperConfig;
import com.eleks.academy.pharmagator.dataproviders.dto.input.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.PharmacyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PharmacyController.class)
@Import({ModelMapperConfig.class})
class PharmacyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URI = "/pharmacies";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PharmacyServiceImpl pharmacyService;

    private static List<Pharmacy> pharmacyList;

    private static Pharmacy pharmacy;

    @BeforeAll
    public static void setUp() {
        pharmacyList = List.of(
                new Pharmacy(101L, "tolya's hub", "https://linktemplate.com/"),
                new Pharmacy(202L, "medcad", "https://linktemplate.com/")
        );

        pharmacy = new Pharmacy(12L, "midi", "https://linktemplate.com/");
    }

    @Test
    void getAll_ok() throws Exception {
        doReturn(pharmacyList).when(pharmacyService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void getById_ok() throws Exception {
        doReturn(Optional.of(pharmacy)).when(pharmacyService).findById(12L);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/" + pharmacy.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.medicineLinkTemplate").exists())
                .andExpect(jsonPath("$.name").value(pharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(pharmacy.getMedicineLinkTemplate()))
                .andDo(print());
    }

    @Test
    void getById_invalidId_404Code() throws Exception {
        doReturn(Optional.empty()).when(pharmacyService).findById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/112").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void create_ok() throws Exception {
        doReturn(pharmacy).when(pharmacyService).save(modelMapper.map(pharmacy, PharmacyDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(pharmacy, PharmacyDto.class)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.medicineLinkTemplate").exists())
                .andExpect(jsonPath("$.name").value(pharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(pharmacy.getMedicineLinkTemplate()))
                .andDo(print());
    }

    @Test
    void create_invalidRequestBody_400Code() throws Exception {
        doReturn(pharmacy).when(pharmacyService).save(modelMapper.map(pharmacy, PharmacyDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ne": "string",
                                  "234": 124
                                }
                                """)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void update_ok() throws Exception {
        doReturn(Optional.of(pharmacy)).when(pharmacyService).update(pharmacy.getId(), modelMapper.map(pharmacy, PharmacyDto.class));

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/" + pharmacy.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(pharmacy, PharmacyDto.class)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.medicineLinkTemplate").exists())
                .andExpect(jsonPath("$.name").value(pharmacy.getName()))
                .andExpect(jsonPath("$.medicineLinkTemplate").value(pharmacy.getMedicineLinkTemplate()))
                .andDo(print());
    }

    @Test
    void update_invalidRequestBody_400Code() throws Exception {
        doReturn(Optional.of(pharmacy)).when(pharmacyService).update(pharmacy.getId(), modelMapper.map(pharmacy, PharmacyDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ne": "string",
                                  "234": 124
                                }
                                """)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(pharmacyService).deleteById(pharmacy.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/" + pharmacy.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void delete_invalidId_200Code() throws Exception {
        doNothing().when(pharmacyService).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/1123768"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}
