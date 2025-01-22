package com.s3.s3DataTaxi.controller;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

public class HtmlToPdfConverter {

	public List<byte[]> convertHtmlToPdf(String htmlContent) {
        List<byte[]> pdfBytesList = new ArrayList<>();

//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            // Build the PDF renderer
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//
//            // Add the custom font
//            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSansJP-Regular.ttf");
//            builder.useFont(() -> fontStream, "NotoSansJP");
//
//            // Other builder configurations
//            builder.useFastMode();
//            builder.withHtmlContent(htmlContent, null); // Pass the HTML content
//            builder.toStream(outputStream); // Write the PDF to the output stream
//
//            // Generate the PDF
//            builder.run();
//
//            // Convert the output stream to byte array
//            byte[] pdfBytes = outputStream.toByteArray();
//
//            // Add the byte array to the List
//            pdfBytesList.add(pdfBytes);
//        } catch (Exception e) {
//            e.printStackTrace(); // Handle the exception
//        }
        int currentLine = 0;
        int linecount = 45;
        StringBuilder modifiedHtmlContent = new StringBuilder();
        String[] lines = htmlContent.split("\n");
        
        for (String line : lines) {
            modifiedHtmlContent.append(line).append("\n");
            currentLine++;

            if (currentLine >= linecount) {
                modifiedHtmlContent.append("<div class=\"page-break\"></div>");  // Insert page break
                currentLine = 0;  // Reset line count for next page
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File("asdf.pdf"))) {

            PdfRendererBuilder builder = new PdfRendererBuilder();

            // Add the custom font
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSansJP-Regular.ttf");
            builder.useFont(() -> fontStream, "NotoSansJP", 400, PdfRendererBuilder.FontStyle.NORMAL, true);

            // Set the HTML content
            builder.withHtmlContent(htmlContent, null); 

            // Write the PDF to the output stream
            builder.toStream(outputStream);

            // Generate the PDF
            builder.run();

            System.out.println("PDF saved to asdf.pdf");


        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return pdfBytesList; 
    }
}
