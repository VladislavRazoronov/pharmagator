package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.services.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/ui/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CsvService csvService;

    @GetMapping
    public String getImportPage() {
        return "importData";
    }

    @PostMapping("/medicines")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        csvService.parseAndSave(file);
        return "importResult";
    }

}

