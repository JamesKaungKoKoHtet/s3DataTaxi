package com.s3.s3DataTaxi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.s3.s3DataTaxi.entity.ShopInvoice;
import com.s3.s3DataTaxi.entity.ShopSaleItems;
import com.s3.s3DataTaxi.service.PdfService;
import com.s3.s3DataTaxi.service.S3Service;

@RestController
public class S3Controller {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private PdfService pdfService;

	@GetMapping("/test")
	public String test() throws IOException {

		List<byte[]> pdfFiles = PdfService.htmltoPdfGenerate();

		if (!pdfFiles.isEmpty()) {
			try {
				for (int i = 0; i < pdfFiles.size(); i++) {
					String s3Key = "download/2025/1/generated-file_" + (i + 1) + ".pdf";
					s3Service.uploadToS3(pdfFiles.get(i), s3Key);
				}
				return "PDFs generated and uploaded to S3 successfully!";
			} catch (IOException e) {
				e.printStackTrace();
				return "Failed to upload PDFs to S3";
			}
		} else {
			return "Failed to generate PDFs";
		}
	}

	@GetMapping("/download/{year}/{month}")
	public ResponseEntity<ByteArrayResource> downloadZip(@PathVariable String year, @PathVariable String month) {

		byte[] zipFile = s3Service.downloadAndZipFilesFromFolder(year + "/" + month);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(zipFile));
	}

	@GetMapping("/upload/test")
	public String uploadPdf() {
		
		// Test data
		List<ShopInvoice> shopInvoice = new ArrayList<>();
		ShopInvoice shop = new ShopInvoice();
		shop.setNet_total_price(10000);
		shop.setCompany_name("test company name");
		List<ShopSaleItems> sold_items = new ArrayList<>();
		ShopSaleItems a = new ShopSaleItems();
		a.setProduct_code("111");
		a.setProduct_name("test product name");
		a.setRetale_price(1000);
		a.setQuantity(10);
		a.setTotal_price(1000);
		sold_items.add(a);
		sold_items.add(a);
		sold_items.add(a);
		sold_items.add(a);
		shop.setSold_items(sold_items);
		shop.setSub_total_quantity(10000);
		shop.setGross_total_price(10000);
		shop.setShipping_fees(10000);
		shop.setTax(10);
		shopInvoice.add(shop);
		shopInvoice.add(shop);

		// Generate multiple PDFs
		List<byte[]> pdfFiles = pdfService.generatePdf(shopInvoice);

		if (!pdfFiles.isEmpty()) {
			try {
				for (int i = 0; i < pdfFiles.size(); i++) {
					String s3Key = "testingfolder/2025/test/generated-file_" + (i + 1) + ".pdf";
					s3Service.uploadToS3(pdfFiles.get(i), s3Key);
				}
				return "PDFs generated and uploaded to S3 successfully!";
			} catch (IOException e) {
				e.printStackTrace();
				return "Failed to upload PDFs to S3";
			}
		} else {
			return "Failed to generate PDFs";
		}
	}

}
