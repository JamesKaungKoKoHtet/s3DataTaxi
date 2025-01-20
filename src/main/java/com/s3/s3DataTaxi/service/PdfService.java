package com.s3.s3DataTaxi.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.springframework.stereotype.Service;

import com.s3.s3DataTaxi.controller.HtmlToPdfConverter;
import com.s3.s3DataTaxi.controller.ShopInvoice;
import com.s3.s3DataTaxi.controller.ShopSaleItems;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

	public List<byte[]> generatePdf(List<ShopInvoice> invoices) {
    List<byte[]> pdfFiles = new ArrayList<byte[]>();

    for (ShopInvoice invoice : invoices) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 700);

            // Load font
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/Helvetica.ttf");
            PDTrueTypeFont font = PDTrueTypeFont.load(document, fontStream, WinAnsiEncoding.INSTANCE);
            contentStream.setFont(font, 12);

            // Write invoice details
            contentStream.showText("Company Name: " + invoice.getCompany_name());
            contentStream.newLine();
            contentStream.showText("Net Total Price: " + invoice.getNet_total_price());
            contentStream.newLine();
            contentStream.showText("Gross Total Price: " + invoice.getGross_total_price());
            contentStream.newLine();
            contentStream.showText("Shipping Fees: " + invoice.getShipping_fees());
            contentStream.newLine();
            contentStream.showText("Tax: " + invoice.getTax());
            contentStream.newLine();
            contentStream.showText("Subtotal Quantity: " + invoice.getSub_total_quantity());
            contentStream.newLine();
            contentStream.newLine();

            // Iterate over sold items
            contentStream.showText("Sold Items:");
            contentStream.newLine();
            for (ShopSaleItems item : invoice.getSold_items()) {
                contentStream.showText(" - Product Code: " + item.getProduct_code());
                contentStream.newLine();
                contentStream.showText("   Product Name: " + item.getProduct_name());
                contentStream.newLine();
                contentStream.showText("   Retail Price: " + item.getRetale_price());
                contentStream.newLine();
                contentStream.showText("   Quantity: " + item.getQuantity());
                contentStream.newLine();
                contentStream.showText("   Total Price: " + item.getTotal_price());
                contentStream.newLine();
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();

            // Save the PDF to ByteArrayOutputStream and add it to the list
            document.save(byteArrayOutputStream);
            pdfFiles.add(byteArrayOutputStream.toByteArray());

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    return pdfFiles;
}

	public static  List<byte[]> testHtmlToPDF() throws IOException{
		System.out.println("called");
	        HtmlToPdfConverter converter = new HtmlToPdfConverter();
	        
	        String htmlContent = """
	                <html>
	                <body>
	                    <h1>Hello, World!</h1>
	                    <p>This is a sample PDF generated from HTML.</p>
	                </body>
	                </html>
	                """;
	        
	        List<byte[]> pdfBytes = converter.convertHtmlToPdf(htmlContent);
	        
	        return pdfBytes;
	    
	}
}
