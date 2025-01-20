package com.s3.s3DataTaxi.controller;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HtmlToPdfConverter {

    public List<byte[]> convertHtmlToPdf(String htmlContent) {
        List<byte[]> pdfBytesList = new ArrayList<>();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Build the PDF renderer
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null); // Pass the HTML content
            builder.toStream(outputStream); // Write the PDF to the output stream

            // Generate the PDF
            builder.run();

            // Convert the output stream to byte array
            byte[] pdfBytes = outputStream.toByteArray();

            // Add the byte array to the List
            pdfBytesList.add(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception
        }

        return pdfBytesList; // Return the List<byte[]>
    }
}
