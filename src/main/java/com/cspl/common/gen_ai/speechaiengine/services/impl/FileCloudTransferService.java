package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.services.IFileCloudTransferService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class FileCloudTransferService implements IFileCloudTransferService {

    private final Storage storage;

    private final AppProperties.GCPStorageProperties gcpStorageProperties;

    @Override
    public String transferFileFromUrl(String fileUrl, String newFileName, String encodedAuth) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            if(Strings.isNotBlank(encodedAuth)) {
                connection.setRequestProperty("Authorization", encodedAuth);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to download file, response code: " + responseCode + ", message: " + connection.getResponseMessage());
            }

            log.debug("Downloading file from URL: {}", fileUrl);
            inputStream =  connection.getInputStream();

            BlobInfo blobInfo = BlobInfo.newBuilder(gcpStorageProperties.getBucketName(), newFileName).build();
            storage.create(blobInfo, inputStream);
            log.info("File successfully uploaded: {}" , newFileName);

            return gcpStorageProperties.getBaseUrl() + gcpStorageProperties.getBucketName() + "/" + newFileName;

        } catch (IOException e) {
            log.error("[FileCloudTransferGCSService] : Error downloading and uploading file from URL: {} - {}", fileUrl, e.getMessage(), e);
            throw new RuntimeException("[FileCloudTransferGCSService] : Error downloading and uploading file: " + e.getMessage(), e);

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            catch (Exception ex){
                log.error("[FileCloudTransferGCSService] :  to close input stream and connection for {}",newFileName);
            }
        }
    }

    @Override
    public String getSignedUrl(String fileName,int timeInHours){
        BlobId blobId = BlobId.of(gcpStorageProperties.getBucketName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        long expirationMillis = TimeUnit.HOURS.toMillis(timeInHours);
        String signedUrl = storage.signUrl(blobInfo, expirationMillis, TimeUnit.MILLISECONDS, Storage.SignUrlOption.withV4Signature()).toString();
        log.info("[FileCloudTransferGCSService] generated signed url for {}",fileName);
        return signedUrl;
    }
}
