package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.exceptions.ExportExceptions;
import com.eleks.academy.pharmagator.projections.MedicinePrice;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class PDFExportService {

    private final PharmacyRepository pharmacyRepository;
    private final PriceRepository priceRepository;

    private List<Pharmacy> pharmacies;
    private Map<String, Map<Long, BigDecimal>> prices;
    private PdfPTable table;

    private String pathToFont = "api/src/main/resources/fonts/FreeSerif.ttf";

    public byte[] export() {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            findAllMedicinesPrices();
            createTableWithHeader();
            createRows();
            document.add(this.table);
            document.close();
        } catch (DocumentException e) {
            throw new ExportExceptions(ExportExceptions.Error.GET_PDF_WRITER_IS_BAD);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void findAllMedicinesPrices() {
        this.prices = this.priceRepository.findAllMedicinesPrices(null).stream()
                .collect(groupingBy(MedicinePrice::getTitle,
                        toMap(MedicinePrice::getPharmacyId, MedicinePrice::getPrice)));
    }

    private void createTableWithHeader() {
        this.pharmacies = this.pharmacyRepository.findAll();
        this.table = new PdfPTable(pharmacies.size());
        addTableHeader();
    }

    private void addTableHeader() {
        this.pharmacies.forEach(pharmacy -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.ORANGE);
            header.setBorderWidth(2);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPhrase(new Phrase(pharmacy.getName()));
            table.addCell(header);
        });
    }

    private void createRows() {
        this.prices.forEach((medicine, pharmacyPrices) -> addRows(medicine));
    }

    private void addRows(String medicineName) {
        addMedicineCell(medicineName);
        Map<Long, BigDecimal> pharmaciesPrices = this.prices.get(medicineName);
        getPriceCells(pharmaciesPrices).forEach(cell -> this.table.addCell(cell));
    }

    private void addMedicineCell(String medicineName) {
        try {
            BaseFont baseFont = BaseFont.createFont(pathToFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);

            PdfPCell medicineNameColumn = new PdfPCell(new Paragraph(medicineName, font));
            medicineNameColumn.setColspan(3);
            medicineNameColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
            medicineNameColumn.setBackgroundColor(BaseColor.GRAY);

            this.table.addCell(medicineNameColumn);
        } catch (DocumentException | IOException e) {
            throw new ExportExceptions(ExportExceptions.Error.INVALID_FONT);
        }
    }

    private Stream<PdfPCell> getPriceCells(Map<Long, BigDecimal> pharmaciesPrices) {
        return this.pharmacies.stream()
                .map(Pharmacy::getId)
                .map(pharmaciesPrice ->
                        Optional.ofNullable(pharmaciesPrices.get(pharmaciesPrice))
                                .map(Objects::toString)
                                .orElseGet(String::new))
                .map(price -> {
                    PdfPCell priceColumn = new PdfPCell(new Paragraph(price));
                    priceColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
                    priceColumn.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    return priceColumn;
                });
    }

}
