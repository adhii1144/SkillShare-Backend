package com.example.skillshare.Services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.core.exception.HttpResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AzureBlobService {

    // Fetch from application.properties or hardcode for simplicity
    @Value("${azure.storage.endpoint}")
    private String endpoint;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Value("${azure.storage.sas-token}")
    private String sasToken;

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            // Create the BlobServiceClient using the endpoint and SAS token
            String blobServiceUrl = endpoint + "?" + sasToken;
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint(blobServiceUrl)
                    .buildClient();

            // Create BlobContainerClient to interact with the container
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Create a BlobClient for the file
            BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

            // Upload the file
            InputStream inputStream = file.getInputStream();
            blobClient.upload(inputStream, file.getSize(), true); // Overwrite if file exists

            // Return the Blob URL
            return blobClient.getBlobUrl();
        } catch (HttpResponseException e) {
            throw new IOException("Error uploading file: " + e.getMessage(), e);
        }
    }

    public byte[] downloadFile(String blobName) throws IOException {
        try {
            // Create the BlobServiceClient using the endpoint and SAS token
            String blobServiceUrl = endpoint + "?" + sasToken;
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint(blobServiceUrl)
                    .buildClient();

            // Create BlobContainerClient to interact with the container
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Get the BlobClient for the specific blob
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Download the blob content
            return blobClient.downloadContent().toBytes();
        } catch (HttpResponseException e) {
            throw new IOException("Error downloading file: " + e.getMessage(), e);
        }
    }
}
