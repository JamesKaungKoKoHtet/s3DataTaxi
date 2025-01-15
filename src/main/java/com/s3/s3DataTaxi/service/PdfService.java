package com.s3.s3DataTaxi.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.springframework.stereotype.Service;

import com.s3.s3DataTaxi.controller.ShopInvoice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] generatePdf(List<ShopInvoice> objects) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);


            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 700);

         // Load the font from the resources folder
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/Helvetica.ttf");
            if (fontStream == null) {
                throw new FileNotFoundException("Font file not found in resources");
            }

            // Create a PDTrueTypeFont using the InputStream
            PDTrueTypeFont font = PDTrueTypeFont.load(document, fontStream, WinAnsiEncoding.INSTANCE);

            // Set the font for the content stream
            contentStream.setFont(font, 15);

            
            for (ShopInvoice obj : objects) {
                contentStream.showText("ID: " + obj.getId());
                contentStream.newLine();
                contentStream.showText("Name: " + obj.getName());
                contentStream.newLine();
                contentStream.showText("Sales: " + obj.getSales());
                contentStream.newLine();
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();


            document.save(byteArrayOutputStream);
            document.close();


            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
