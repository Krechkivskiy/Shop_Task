package com.productshop.productshop.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.BaseColor;
import com.productshop.productshop.dto.ProductDto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CheckWriter {

    public static void writeCheck(List<ProductDto> productList, Long basketId, String path) throws IOException,
            DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));
        double totalPrise = 0l;
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
        for (ProductDto productDto : productList) {
            Chunk chunk = new Chunk(productDto.getName() + " " + productDto.getPrice(), font);
            totalPrise = totalPrise + productDto.getPrice();
            document.add(chunk);
        }
        Chunk chunk = new Chunk("Total price   " + totalPrise,
                FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK));
        document.add(chunk);
        document.close();
    }
}
