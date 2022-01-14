package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.config.ModelMapperConfig;
import com.eleks.academy.pharmagator.dataproviders.dto.input.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineServiceImpl;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = MedicineController.class)
@Import({MedicineController.class, ModelMapperConfig.class})
class MedicineControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineServiceImpl medicineService;

    @Autowired
    private ModelMapper modelMapper;

    private final String URI = "/medicines";
    private static Medicine medicine;
    private static Medicine medicine2;
    private static List<Medicine> medicineList;

    @BeforeAll
    public static void setup() {
        medicine = new Medicine(8L, "Vitamin C");
        medicine2 = new Medicine(9L, "Vitamin B");
        medicineList = Arrays.asList(medicine, medicine2);
    }

    @Test
    void postMappingOfMedicine_medicineIsCreated() throws Exception {
        when(medicineService.save(any(MedicineDto.class))).thenReturn(medicine);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(medicine, MedicineDto.class))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(medicine.getTitle()))
                .andDo(MockMvcResultHandlers.print());

        verify(medicineService, times(1)).save(any(MedicineDto.class));
    }

    @Test
    void getAllProducts_() throws Exception {
        when(medicineService.findAll()).thenReturn(medicineList);

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        verify(medicineService, times(1)).findAll();
    }

    @Test
    void DeleteById_ShouldDeleteMedicine() throws Exception {

        doNothing().when(medicineService).deleteById(medicine.getId());

        mockMvc.perform(delete(URI + "/" + medicine.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        verify(medicineService, times(1)).deleteById(anyLong());
    }

    @Test
    void GetMappingOfMedicine_ShouldReturnRespectiveMedicine() throws Exception {
        when(medicineService.findById(medicine.getId())).thenReturn(Optional.ofNullable(medicine));

        mockMvc.perform(get(URI + "/" + medicine.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(medicine.getTitle())));
    }

    @Test
    void putMappingOfMedicine_medicineIsUpdated() throws Exception {
        when(medicineService.update(anyLong(), any(MedicineDto.class)))
                .thenReturn(Optional.ofNullable(medicine));

        mockMvc.perform(put(URI + "/" + medicine.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(medicine, MedicineDto.class))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(medicine.getTitle()))
                .andDo(MockMvcResultHandlers.print());

        verify(medicineService, times(1)).update(anyLong(), any(MedicineDto.class));
    }

    @Test
    void findById_testResourceNotFoundException() throws Exception {
        when(medicineService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/{id}", 1000L))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update_testResourceNotFoundException() throws Exception {
        when(medicineService.update(anyLong(), any(MedicineDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put(URI + "/{id}", 1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicine)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
