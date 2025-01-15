package com.s3.s3DataTaxi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.s3.s3DataTaxi.service.PdfService;
import com.s3.s3DataTaxi.service.S3Service;

@RestController
public class S3Controller {
	
	@Autowired
    private S3Service s3Service;
	
	@Autowired
	private PdfService pdfService;

    @GetMapping("/download/{year}/{month}")
    public ResponseEntity<ByteArrayResource> downloadZip(@PathVariable String year, @PathVariable String month) {

        byte[] zipFile = s3Service.downloadAndZipFilesFromFolder(year+"/"+month);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(zipFile));
    }
    
    @GetMapping("/upload/test")
    public String uploadPdf() {
    	
    	List<ShopInvoice> shopInvoice = new ArrayList<ShopInvoice>();
    	ShopInvoice shop = new ShopInvoice("shop1", "001", 10000);
    	shopInvoice.add(shop);
    	
    	byte[] pdfFile = pdfService.generatePdf(shopInvoice);
    	
    	if (pdfFile != null) {
            try {
				s3Service.uploadToS3(pdfFile, "2025/12/generated-file.pdf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "PDF generated and uploaded to S3 successfully!";
        } else {
            return "Failed to generate PDF";
        }
    	
    }
    
    

   
}
