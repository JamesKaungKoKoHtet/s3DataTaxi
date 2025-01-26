package com.s3.s3DataTaxi.controller;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

public class HtmlToPdfConverter {

	public List<byte[]> convertHtmlToPdf(String htmlContent) {
		
		List<byte[]> pdfBytesList = new ArrayList<>();

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PdfRendererBuilder builder = new PdfRendererBuilder();

			InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSansJP-Regular.ttf");
			builder.useFont(() -> fontStream, "NotoSansJP", 400, PdfRendererBuilder.FontStyle.NORMAL, true);

			builder.withHtmlContent(htmlContent, null);

			builder.toStream(outputStream);

			builder.run();

			byte[] pdfBytes = outputStream.toByteArray();

			pdfBytesList.add(pdfBytes);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
//		try (FileOutputStream outputStream = new FileOutputStream(new File("asdf.pdf"))) {
//
//			PdfRendererBuilder builder = new PdfRendererBuilder();
//
//			InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSansJP-Regular.ttf");
//			builder.useFont(() -> fontStream, "NotoSansJP", 400, PdfRendererBuilder.FontStyle.NORMAL, true);
//
//			builder.withHtmlContent(htmlContent, null);
//
//			builder.toStream(outputStream);
//
//			builder.run();
//
//			System.out.println("PDF saved to asdf.pdf");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return pdfBytesList;
	}
}
