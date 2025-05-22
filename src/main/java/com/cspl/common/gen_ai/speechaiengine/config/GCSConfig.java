package com.cspl.common.gen_ai.speechaiengine.config;

import com.google.api.services.storage.StorageScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class GCSConfig {
    @Bean
    public Storage storage(AppProperties.GCPStorageProperties gcpStorageProperties) throws IOException {

        byte[] serviceKeyByteArray = Base64.getDecoder().decode(gcpStorageProperties.getKeyFilePath());
        InputStream credentialsStream = new ByteArrayInputStream(serviceKeyByteArray);

        GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(credentialsStream)
                .createScoped(StorageScopes.all());


        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(gcpStorageProperties.getProjectId())
                .build()
                .getService();
    }
}
