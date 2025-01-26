package com.s3.s3DataTaxi.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.s3.s3DataTaxi.controller.HtmlToPdfConverter;
import com.s3.s3DataTaxi.entity.Product;
import com.s3.s3DataTaxi.entity.ShopInvoice;
import com.s3.s3DataTaxi.entity.ShopSaleItems;
import com.s3.s3DataTaxi.entity.Shops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

	public static List<byte[]> htmltoPdfGenerate(List<Shops> shops) throws IOException {

		// TEST Data Preparation
		List<Product> products = new ArrayList<Product>();
		Product p = new Product();
		p.setProductCode("100001100001");
		p.setProductName("asdf");
		p.setPrice(1500);
		p.setQuantity(1500);
		p.setTotal(22500);
		int i = 0;
		do {
			products.add(p);
			i++;
		} while (i < 170);

		HtmlToPdfConverter converter = new HtmlToPdfConverter();
		ClassPathResource resource = new ClassPathResource("pdfTemplate/invoice.html");
		String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));

		String htmlContent = htmlTemplate
				// 日付
				.replace("{{date}}", "2025/01/22").replace("{{day}}", "水曜日")
				// Detail
				.replace("{{companyName}}", "ネックスピュア株式会社様").replace("{{name}}", "name")
				.replace("{{postalCode}}", "000-0000").replace("{{address1}}", "address1")
				.replace("{{address2}}", "address2").replace("{{phone}}", "000-0000-0000")
				.replace("{{mail}}", "mail@gmail.com").replace("{{persionInCharge}}", "persinInCharge")
				.replace("{{month}}", "10").replace("{{netTotal}}", "195,779")
				// Products
				.replace("{{products}}", formatProductItems(products))
				// 小計
				.replace("{{totalProductCount}}", "100").replace("{{subTotal}}", "195,779")
				// 送料（税別）
				.replace("{{shippingFee}}", "195,779").replace("{{shippingFeeTotal}}", "195,779")
				// 消費税等１０％対象
				.replace("{{tax10percent}}", "195,779").replace("{{tax10percentTotal}}", "195,779")
				//
				.replace("{{tax8percent}}", "195,779").replace("{{tax8percentTotal}}", "195,779")
				//
				.replace("{{grossTotal}}", "195,779");

		List<byte[]> pdfBytes = converter.convertHtmlToPdf(htmlContent);

		return pdfBytes;

	}

	private static String formatProductItems(List<Product> products) {
		StringBuilder itemRows = new StringBuilder();
		int countCheck = 0;
		int pageCount = 0;
		for (Product product : products) {
			if ((pageCount == 0 && countCheck > 45) || (pageCount > 0 && countCheck > 55)) {
				itemRows.append("<div class=\"page-break\" ></div>");
			    itemRows.append("<tr class=\"detail_titles\" >");
			    itemRows.append("<th>商品コード</th>");
			    itemRows.append("<th>商品</th>");
			    itemRows.append("<th>下代</th>");
			    itemRows.append("<th>個数</th>");
			    itemRows.append("<th>合計</th></tr>");
			    pageCount++;
			    countCheck = 0;
			}

			itemRows.append("<tr>").append("<td>").append(product.getProductCode()).append("</td>").append("<td>")
					.append(product.getProductName()).append("</td>").append("<td>¥").append(product.getPrice())
					.append("</td>").append("<td>").append(product.getQuantity()).append("</td>").append("<td>¥")
					.append(product.getTotal()).append("</td>").append("</tr>");
			countCheck++;
		}
		return itemRows.toString();
	}
}
