package com.eleks.academy.pharmagator.controllers.ui;

import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.ExportService;
import com.eleks.academy.pharmagator.services.MedicineService;
import com.eleks.academy.pharmagator.services.PharmacyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(NavigationController.class)
class NavigationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URI = "/ui";

    @MockBean
    private PharmacyService pharmacyService;

    @MockBean
    private MedicineService medicineService;

    @MockBean
    private ExportService exportService;

    private static List<Medicine> meds;
    private static List<Pharmacy> pharmacies;
    private static Map<String, Map<Long, BigDecimal>> prices;

    private static final Integer PAGE = 1;
    private static final String SORT_DIRECTION = "ASC";
    private final String URI = "/ui/price";

    private static final String KEYWORD = "3";

    @BeforeAll
    public static void setUp() {
        meds = List.of(
                new Medicine(201102L, "medicine1"),
                new Medicine(202202L, "medicine2"),
                new Medicine(2011102L, "medicine3"),
                new Medicine(2022102L, "medicine4"),
                new Medicine(2021102L, "medicine6"),
                new Medicine(2023202L, "medicine5"),
                new Medicine(2011302L, "medicine7"),
                new Medicine(1202202L, "medicine8")
        );

        pharmacies = List.of(
                new Pharmacy(103L, "tolya's hub", "https://linktemplate.com/"),
                new Pharmacy(104L, "medcad", "https://linktemplate.com/")
        );

        prices = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies = new HashMap<>();
        priceByPharmacies.put(103L, new BigDecimal("12.23"));
        priceByPharmacies.put(104L, new BigDecimal("24.46"));
        prices.put("medicine13", priceByPharmacies);
        prices.put("randomName2", priceByPharmacies);
    }

    @Test
    void getHomePage_ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());
    }

    @Test
    void getMedicines_ok() throws Exception {
        doReturn(new PageImpl<>(meds)).when(medicineService).findAllPaginated(any(PageRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/medicine"))
                .andExpect(status().isOk())
                .andExpect(view().name("medicine"))
                .andExpect(model().attribute("meds", meds))
                .andDo(print());
    }

    @Test
    void getPrices_ok1() throws Exception {
        doReturn(pharmacies).when(pharmacyService).findAll();
        doReturn(prices).when(exportService).getMapPricesFromDatabase(any(PageRequest.class));
        doReturn((long)prices.size()).when(exportService).getRowsCountOfPrices();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/price"))
                .andExpect(status().isOk())
                .andExpect(view().name("price"))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andExpect(model().attribute("pricesMap", prices))
                .andExpect(model().attribute("url", URI))
                .andExpect(model().attribute("currentPage", PAGE))
                .andExpect(model().attribute("sortDirection", SORT_DIRECTION))
                .andDo(print());
    }

    @Test
    void getPrices_ok2() throws Exception {
        doReturn(pharmacies).when(pharmacyService).findAll();

        Map<Long, BigDecimal> priceByPharmacies1 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies2 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies3 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies4 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies5 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies6 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies7 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies8 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies9 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies10 = new HashMap<>();

        priceByPharmacies1.put(107L, new BigDecimal("12.23"));
        prices.put("medicine331", priceByPharmacies1);
        priceByPharmacies2.put(1066L, new BigDecimal("24.40"));
        prices.put("randomName112", priceByPharmacies2);
        priceByPharmacies3.put(1037L, new BigDecimal("132.23"));
        prices.put("medicine3512", priceByPharmacies3);
        priceByPharmacies4.put(1048L, new BigDecimal("249.46"));
        prices.put("randomName612", priceByPharmacies4);
        priceByPharmacies5.put(1039L, new BigDecimal("1276.23"));
        prices.put("medicine3128", priceByPharmacies5);
        priceByPharmacies6.put(1040L, new BigDecimal("2344.46"));
        prices.put("randomName239", priceByPharmacies6);
        prices.put("medicine30512", priceByPharmacies7);
        priceByPharmacies4.put(1048L, new BigDecimal("2491.46"));
        prices.put("randomName6102", priceByPharmacies8);
        priceByPharmacies5.put(1039L, new BigDecimal("12761.23"));
        prices.put("medicine31208", priceByPharmacies9);
        priceByPharmacies6.put(1040L, new BigDecimal("23442.46"));
        prices.put("randomName2039", priceByPharmacies10);

        doReturn(prices).when(exportService).getMapPricesFromDatabase(any(PageRequest.class));
        doReturn((long)prices.size()).when(exportService).getRowsCountOfPrices();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/price"))
                .andExpect(status().isOk())
                .andExpect(view().name("price"))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andExpect(model().attribute("pricesMap", prices))
                .andExpect(model().attribute("url", URI))
                .andExpect(model().attribute("currentPage", PAGE))
                .andExpect(model().attribute("sortDirection", SORT_DIRECTION))
                .andDo(print());
    }

    @Test
    void getPrices_ok3() throws Exception {
        doReturn(pharmacies).when(pharmacyService).findAll();
        doReturn(prices).when(exportService).getMapPricesFromDatabase(any(PageRequest.class));
        doReturn((long)prices.size()).when(exportService).getRowsCountOfPrices();

        int page5 = 5;

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/price?page=" + page5))
                .andExpect(status().isOk())
                .andExpect(view().name("price"))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andExpect(model().attribute("pricesMap", prices))
                .andExpect(model().attribute("url", URI))
                .andExpect(model().attribute("currentPage", page5))
                .andExpect(model().attribute("sortDirection", SORT_DIRECTION))
                .andDo(print());
    }

    @Test
    void getPrices_ok4() throws Exception {
        doReturn(pharmacies).when(pharmacyService).findAll();

        int page4 = 4;
        Map<Long, BigDecimal> priceByPharmacies1 = new HashMap<>();
        Map<Long, BigDecimal> priceByPharmacies2 = new HashMap<>();

        priceByPharmacies1.put(1087L, new BigDecimal("112.23"));
        prices.put("medicine1331", priceByPharmacies1);
        priceByPharmacies2.put(10566L, new BigDecimal("224.40"));
        prices.put("randomName1122", priceByPharmacies2);

        doReturn(prices).when(exportService).getMapPricesFromDatabase(any(PageRequest.class));
        doReturn((long)prices.size()).when(exportService).getRowsCountOfPrices();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/price?page=" + page4))
                .andExpect(status().isOk())
                .andExpect(view().name("price"))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andExpect(model().attribute("pricesMap", prices))
                .andExpect(model().attribute("url", URI))
                .andExpect(model().attribute("currentPage", page4))
                .andExpect(model().attribute("sortDirection", SORT_DIRECTION))
                .andDo(print());
    }

    @Test
    void getMedicinesPricesSearch_keywordNotNull_ok() throws Exception {
        Map<String, Map<Long, BigDecimal>> subPrices = Stream.of("medicine3").filter(prices::containsKey)
                .collect(Collectors.toMap(Function.identity(), prices::get));
        doReturn(subPrices).when(exportService).searchMedicinesPrices(KEYWORD);
        doReturn(pharmacies).when(pharmacyService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/search?keyword=3"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("keyword", KEYWORD))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andExpect(model().attribute("pricesMap", subPrices))
                .andDo(print());
    }

    @Test
    void getMedicinesPricesSearch_keywordNull_ok() throws Exception {
        doReturn(pharmacies).when(pharmacyService).findAll();
        doReturn(prices).when(exportService).getMapPricesFromDatabase(null);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("pricesMap", prices))
                .andExpect(model().attribute("pharmacies", pharmacies))
                .andDo(print());
    }

}
