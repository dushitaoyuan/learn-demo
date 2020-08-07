package com.taoyuanx.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dushitaoyuan
 * @date 2020/8/7
 */
@Configuration
@ConfigurationProperties("minio")
@Data
public class MinioProperties {
    private String bucketName;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String region;

    private Integer connectTimeout=3;
    private Integer maxIdleConnections=150;
    private Integer keepAliveDuration=30;
}
