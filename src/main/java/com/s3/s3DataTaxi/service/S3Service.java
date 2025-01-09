package com.s3.s3DataTaxi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class S3Service {


    private final S3Client s3Client;
    private final String bucketName;

    // Inject AWS credentials, region, and bucket name from application.properties
    public S3Service(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretAccessKey}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${aws.s3.bucketName}") String bucketName) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.bucketName = bucketName;
    }

    // Method to download and zip all files in a specific folder (prefix) from S3
    public byte[] downloadAndZipFilesFromFolder(String folderPrefix) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            // List all files in the folder (prefix)
            List<S3Object> objects = listFilesInFolder(folderPrefix);

            // For each file in the folder, download and add it to the zip
            for (S3Object object : objects) {
                InputStream fileStream = downloadFileFromS3(object.key());
                if (fileStream != null) {
                    // Add each file to the zip
                    zos.putNextEntry(new ZipEntry(object.key().substring(object.key().lastIndexOf('/') + 1)));

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fileStream.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }

                    zos.closeEntry();
                    fileStream.close();
                }
            }

            zos.finish();
            return baos.toByteArray();  // Return the zipped files as byte array
        } catch (IOException e) {
            throw new RuntimeException("Error while zipping files", e);
        }
    }

    // Helper method to list files in the folder (prefix)
    private List<S3Object> listFilesInFolder(String folderPrefix) {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderPrefix)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);
        return response.contents();  // Return the list of S3 objects
    }

    // Helper method to download a single file from S3
    private InputStream downloadFileFromS3(String keyName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return null;
        }
    }
}