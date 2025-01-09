package com.s3.s3DataTaxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.s3.s3DataTaxi.service.S3Service;

@RestController
public class DownloadController {
	
	@Autowired
    private S3Service s3Service;

    @GetMapping("/download/{year}/{month}")
    public ResponseEntity<ByteArrayResource> downloadZip(@PathVariable String year, @PathVariable String month) {

        // Download and zip all files in the specified folder from S3
        byte[] zipFile = s3Service.downloadAndZipFilesFromFolder(year+"/"+month);

        // Return the zipped file as a downloadable response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(zipFile));
    }

   
}
