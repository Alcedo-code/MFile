package com.angel.minio.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * minio配置实体
 * @Author yang
 * @Date 2023/1/3 14:00
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperty {
    private String url;
    private String accessKey;
    private String secretKey;
    /**
    private long callTimeOut = 60000;
    private long readTimeOut = 300000;
     **/
}
