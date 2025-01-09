package com.s3.s3DataTaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class S3DataTaxiApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3DataTaxiApplication.class, args);
	}

}