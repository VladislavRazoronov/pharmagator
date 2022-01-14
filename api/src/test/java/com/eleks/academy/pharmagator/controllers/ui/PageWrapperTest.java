package com.eleks.academy.pharmagator.controllers.ui;

import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PageWrapperTest {

    private static List<Medicine> meds;

    private static Page<Medicine> page;

    private final String URI = "/ui/medicine";
    private final String SORT_DIRECTION = "ASC";

    @BeforeAll
    public static void setup() {
        meds = List.of(
                new Medicine(11L, "meds11"),
                new Medicine(22L, "meds22"),
                new Medicine(33L, "meds33"),
                new Medicine(14L, "meds14"),
                new Medicine(25L, "meds25"),
                new Medicine(36L, "meds36"),
                new Medicine(17L, "meds17"),
                new Medicine(28L, "meds28"),
                new Medicine(111L, "meds111"),
                new Medicine(222L, "meds222"),
                new Medicine(333L, "meds333"),
                new Medicine(144L, "meds144"),
                new Medicine(255L, "meds255"),
                new Medicine(366L, "meds366"),
                new Medicine(177L, "meds177"),
                new Medicine(288L, "meds288"),
                new Medicine(39L, "meds39")
        );

        PageRequest pageRequest = PageRequest.of(1, meds.size());

        page = new PageImpl<>(meds, pageRequest, meds.size());
    }

    @Test
    void PageWrapper_ok() {
        PageWrapper<Medicine> medicinePageWrapper1 = new PageWrapper<>(page, URI, SORT_DIRECTION);
        PageWrapper<Medicine> medicinePageWrapper2 = new PageWrapper<>(page, null, SORT_DIRECTION);

        assertEquals(medicinePageWrapper1.getNumber(), medicinePageWrapper2.getNumber());
        assertEquals(medicinePageWrapper1.getItems().size(), medicinePageWrapper2.getItems().size());
        assertNotEquals(medicinePageWrapper1.getUrl(), medicinePageWrapper2.getUrl());

        List<Pharmacy> pharmacies = List.of(new Pharmacy(1L, "pharm1", null), new Pharmacy(2L, "pharm2", null));

        PageWrapper<Pharmacy> medicinePageWrapper3 = new PageWrapper<>(new PageImpl<>(pharmacies), URI, SORT_DIRECTION);
        PageWrapper<Pharmacy> medicinePageWrapper4 = new PageWrapper<>(new PageImpl<>(pharmacies), null, SORT_DIRECTION);

        assertEquals(medicinePageWrapper3.getNumber(), medicinePageWrapper4.getNumber());
        assertEquals(medicinePageWrapper3.getItems().size(), medicinePageWrapper4.getItems().size());
        assertNotEquals(medicinePageWrapper3.getUrl(), medicinePageWrapper4.getUrl());

        PageRequest pageRequest1 = PageRequest.of(5, 2, Sort.Direction.valueOf(SORT_DIRECTION), "title");

        PageWrapper<Medicine> medicinePageWrapper5 = new PageWrapper<>(new PageImpl<>(meds, pageRequest1, meds.size()), URI, SORT_DIRECTION);
        PageWrapper<Medicine> medicinePageWrapper6 = new PageWrapper<>(new PageImpl<>(meds, pageRequest1, meds.size()), null, SORT_DIRECTION);

        assertEquals(medicinePageWrapper5.getNumber(), medicinePageWrapper6.getNumber());
        assertEquals(medicinePageWrapper5.getItems().size(), medicinePageWrapper6.getItems().size());
        assertNotEquals(medicinePageWrapper5.getUrl(), medicinePageWrapper6.getUrl());
    }

    @Test
    void getUrl_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        String expectedUrl = URI;
        String actualUrl = medicinePageWrapper.getUrl();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void setUrl_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, null, SORT_DIRECTION);

        medicinePageWrapper.setUrl(URI);

        String expectedUrl = URI;
        String actualUrl = medicinePageWrapper.getUrl();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void getSortDirection_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        String expectedSortDirection = SORT_DIRECTION;
        String actualSortDirection = medicinePageWrapper.getSortDirection();

        assertEquals(expectedSortDirection, actualSortDirection);
    }

    @Test
    void setSortDirection_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, null);

        medicinePageWrapper.setSortDirection(SORT_DIRECTION);

        String expectedSortDirection = SORT_DIRECTION;
        String actualSortDirection = medicinePageWrapper.getSortDirection();

        assertEquals(expectedSortDirection, actualSortDirection);
    }

    @Test
    void getNumber_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        int expectedCurrentPage = 1;
        int actualCurrentPage = medicinePageWrapper.getNumber();

        assertEquals(expectedCurrentPage, actualCurrentPage);
    }

    @Test
    void getContent_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        List<Medicine> expectedContent = meds;
        List<Medicine> actualContent = medicinePageWrapper.getContent();

        assertEquals(expectedContent, actualContent);
    }

    @Test
    void getSize_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        int expectedSize = meds.size();
        int actualSize = medicinePageWrapper.getSize();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void getTotalPages_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        int expectedTotalPages = 2;
        int actualTotalPages = medicinePageWrapper.getTotalPages();

        assertEquals(expectedTotalPages, actualTotalPages);
    }

    @Test
    void getTotalElements_ok() {
        PageWrapper<Medicine> medicinePageWrapper = new PageWrapper<>(page, URI, SORT_DIRECTION);

        long expectedTotalElements = meds.size() * 2L;
        long actualTotalElements = medicinePageWrapper.getTotalElements();

        assertEquals(expectedTotalElements, actualTotalElements);
    }

}
