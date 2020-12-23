package com.taoyuanx.demo.config;

import com.taoyuanx.demo.client.CanalClient;
import com.taoyuanx.demo.client.SimpleCanalClient;
import com.taoyuanx.demo.client.entryhandler.CanalClientPrint;
import com.taoyuanx.demo.client.entryhandler.CanalMessageHandler;
import com.taoyuanx.demo.client.entryhandler.CanalMessagerLogger;
import com.taoyuanx.demo.util.SpringContextUtil;
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
@ConfigurationProperties(prefix = "canal-client")
@Data
public class CanalProperties {
    private String type;
    private String server;

    private Integer serverPort;

    @Bean(destroyMethod = "stop")
    @ConditionalOnProperty(prefix = "canal-client", name = "type", havingValue = "client")
    public CanalClient cannalClient(CanalMessageHandler canalMessageHandler) {
        CanalClient cannalClient = new SimpleCanalClient(this, canalMessageHandler);
        cannalClient.start();
        return cannalClient;
    }

    @Bean
    @ConditionalOnProperty(prefix = "canal-client", name = "messageHandlerType", havingValue = "logger")
    public CanalMessageHandler canalMessageHandler() {
        return new CanalMessagerLogger();
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }


}
