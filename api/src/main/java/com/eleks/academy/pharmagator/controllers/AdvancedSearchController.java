package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.AdvancedSearchRequest;
import com.eleks.academy.pharmagator.entities.AdvancedSearchView;
import com.eleks.academy.pharmagator.services.AdvancedSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class AdvancedSearchController {

    private final AdvancedSearchService advancedSearchService;

    @GetMapping
    public Page<AdvancedSearchView> search(@ModelAttribute AdvancedSearchRequest request,
                                           @PageableDefault(size = 20, sort = "medicine") Pageable pageable) {
        return advancedSearchService.search(request, pageable);
    }

}
