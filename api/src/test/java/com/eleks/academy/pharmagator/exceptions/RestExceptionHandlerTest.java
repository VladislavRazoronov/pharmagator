package com.eleks.academy.pharmagator.exceptions;

import com.eleks.academy.pharmagator.controllers.PharmacyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PharmacyController.class)
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacyController pharmacyController;

    private final String BASE_URI = "/pharmacies";

    @Test
    void handleResponseStatusException_ok() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid id")).when(pharmacyController).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/1123768"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Invalid id"));
    }

    @Test
    void handleInvalidIdentifierException_ok() throws Exception {
        doThrow(new InvalidIdentifierException()).when(pharmacyController).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/1123768"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Invalid id"));
    }

}
