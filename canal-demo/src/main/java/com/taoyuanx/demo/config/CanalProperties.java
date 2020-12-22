package com.taoyuanx.demo.config;

import com.taoyuanx.demo.client.CanalClient;
import com.taoyuanx.demo.client.SimpleCanalClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dushitaoyuan
 * @date 2020/12/22
 */
@Configuration
@ConfigurationProperties(prefix = "canal-client-client")
@Data
public class CanalProperties {
    private String type;
    private String server;

    private String serverPort;

    @Bean(destroyMethod = "stop")
    @ConditionalOnProperty(prefix = "canal-client", name = "type", havingValue = "client")
    public CanalClient cannalClient() {
        CanalClient cannalClient = new SimpleCanalClient(this);
        cannalClient.start();
        return cannalClient;
    }

}
