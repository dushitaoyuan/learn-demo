package com.taoyuanx.demo.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author dushitaoyuan
 * @desc es配置
 * @date 2020/7/16
 */
@ConfigurationProperties(prefix = "es")
@Configuration
@Data
public class EsProperties {
    private String servers;

}
