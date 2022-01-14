package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.exceptions.GlobalExceptionHandler;
import com.eleks.academy.pharmagator.exceptions.UploadExceptions;
import com.eleks.academy.pharmagator.services.CsvService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UploadControllerIT {

    @InjectMocks
    private UploadController uploadController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CsvService csvService;

    private final String URI = "/ui/upload";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(uploadController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prices", "pharmacies", "medicines");
    }

    @Test
    void getImportPage_isOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andExpect(status().isOk())
                .andExpect(view().name("importData"))
                .andDo(print());
    }

    @Test
    void importCsv_isOk() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("import.csv");
        MockMultipartFile file = new MockMultipartFile("file",
                "import.csv", "text/csv", inputStream);

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(URI+"/medicines").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("importResult"))
                .andDo(print());
    }

    @Test
    void importCsv_isInvalidFileFormat() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("import.csv");
        MockMultipartFile file = new MockMultipartFile("file", inputStream);

        when(csvService.parseAndSave(file)).thenThrow(new UploadExceptions(UploadExceptions.Error.INVALID_FILE_FORMAT));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(URI+"/medicines").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        then(this.csvService).should().parseAndSave(file);
    }

    @Test
    void importCsv_saveWasNotSuccessful() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("import.csv");
        MockMultipartFile file = new MockMultipartFile("file", inputStream);

        when(csvService.parseAndSave(file)).thenThrow(new UploadExceptions(UploadExceptions.Error.SAVE_WAS_NOT_SUCCESSFUL));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(URI+"/medicines").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        then(this.csvService).should().parseAndSave(file);
    }

}
