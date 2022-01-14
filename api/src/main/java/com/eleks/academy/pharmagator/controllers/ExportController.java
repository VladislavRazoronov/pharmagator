package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.services.CsvService;
import com.eleks.academy.pharmagator.services.ExportService;
import com.eleks.academy.pharmagator.services.PDFExportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/export")
public class ExportController {

    private final ExportService exportService;
    private final PDFExportService pdfExportService;
    private final CsvService csvService;

    private String defaultHeaderName = "Content-Disposition";

    @GetMapping
    public String getExportPage() {
        return "exportData";
    }

    @SneakyThrows
    @GetMapping("/xlsx")
    public void export(HttpServletResponse response) {
        XSSFWorkbook workbook = exportService.getExportData();
        ServletOutputStream outputStream = response.getOutputStream();

        response.addHeader(defaultHeaderName, "attachment; filename=export.xlsx");

        workbook.write(outputStream);

        workbook.close();
    }

    @SneakyThrows
    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> exportToPDF() {
        byte[] bytes = pdfExportService.export();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(defaultHeaderName, "attachment; filename=export.pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @SneakyThrows
    @GetMapping("/csv")
    public ResponseEntity<ByteArrayResource> exportToCsv() {
        byte[] bytes = csvService.export();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(defaultHeaderName, "attachment; filename=export.csv");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
