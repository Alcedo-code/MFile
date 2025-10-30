package com.angel.minio.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yangbo
 * @Date 2023/7/4 14:27
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(MinioProperty.class)
public class MinioConfig {
    @Autowired
    private MinioProperty minioProperty;

    /**
     * @return {@code MinioClient}
     * @throws Exception
     */
    @Bean
    public MinioClient minioClient() throws Exception {
        return MinioClient.builder().endpoint(minioProperty.getUrl())
                .credentials(minioProperty.getAccessKey(), minioProperty.getSecretKey()).build();
    }
}
