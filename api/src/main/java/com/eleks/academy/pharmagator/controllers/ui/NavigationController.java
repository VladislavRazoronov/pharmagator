package com.eleks.academy.pharmagator.controllers.ui;

import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.ExportService;
import com.eleks.academy.pharmagator.services.MedicineService;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.eleks.academy.pharmagator.controllers.ui.PageWrapper.MAX_PAGE_ITEM_DISPLAY;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui")
public class NavigationController {

    private final MedicineService medicineService;
    private final PharmacyService pharmacyService;
    private final ExportService exportService;

    @Value("${pharmagator.ui.items-per-page}")
    private Integer itemsPerPage;

    private String sortByMedicineField = "title";

    @GetMapping({"", "/", "/index", "/index.html", "/home", "/homepage"})
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/medicine")
    public String getMedicines(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page, 2 * itemsPerPage, Sort.Direction.valueOf(sortDirection), sortByMedicineField);
        Page<Medicine> pageOfMedicine = medicineService.findAllPaginated(pageRequest);
        PageWrapper<Medicine> wrappedPage = new PageWrapper<>(pageOfMedicine, "/ui/medicine", sortDirection);

        model.addAttribute("meds", pageOfMedicine.get().toList());
        model.addAttribute("page", wrappedPage);
        return "medicine";
    }

    @GetMapping("/price")
    public String getPrices(@RequestParam(required = false, defaultValue = "1") Integer page,
                            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page, 2 * itemsPerPage, Sort.Direction.valueOf(sortDirection), sortByMedicineField);
        Map<String, Map<Long, BigDecimal>> mappedPrices = exportService.getMapPricesFromDatabase(pageRequest);

        Long totalCountOfPrices = exportService.getRowsCountOfPrices();
        long totalPages;

        totalPages = totalCountOfPrices / (2L * itemsPerPage);

        List<PageWrapper.PageItem> pageItems = new ArrayList<>();
        int start;
        int size;

        if (totalPages <= MAX_PAGE_ITEM_DISPLAY) {
            start = 1;
            size = (int) totalPages;
        } else {
            if (page <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
                start = 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else if (page >= totalPages - MAX_PAGE_ITEM_DISPLAY / 2) {
                start = (int) totalPages - MAX_PAGE_ITEM_DISPLAY + 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else {
                start = page - MAX_PAGE_ITEM_DISPLAY / 2;
                size = MAX_PAGE_ITEM_DISPLAY;
            }
        }

        for (int i = 0; i < size; i++) {
            pageItems.add(new PageWrapper.PageItem(start + i, (start + i) == page));
        }

        List<Pharmacy> pharmacies = pharmacyService.findAll();

        model.addAttribute("pricesMap", mappedPrices);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageItems", pageItems);
        model.addAttribute("url", "/ui/price");
        model.addAttribute("pharmacies", pharmacies);
        return "price";
    }

    @GetMapping("/search")
    public String getMedicinesPricesSearch(@RequestParam(required = false) String keyword, Model model) {
        Map<String, Map<Long, BigDecimal>> mappedPrices = Optional.ofNullable(keyword)
                .map(exportService::searchMedicinesPrices)
                .orElseGet(() -> exportService.getMapPricesFromDatabase(null));

        List<Pharmacy> pharmacies = pharmacyService.findAll();

        model.addAttribute("pricesMap", mappedPrices);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pharmacies", pharmacies);
        return "search";
    }

}
