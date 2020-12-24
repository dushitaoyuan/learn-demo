package com.taoyuanx.client;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.ssl.SSLUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dushitaoyuan
 * @date 2020/9/3
 */
@Configuration
public class MyConfig {
    @Bean
    public ForestConfiguration forestConfig() {
        ForestConfiguration configuration = ForestConfiguration.configuration();
        configuration.setMaxConnections(123);
        configuration.setMaxRouteConnections(222);
        configuration.setTimeout(3000);
        configuration.setConnectTimeout(2000);
        configuration.setRetryCount(1);
        configuration.setSslProtocol(SSLUtils.SSLv3);
        configuration.setLogEnabled(true);
        configuration.setBackendName("okhttp3");
        return configuration;
    }
    @Bean
    public MyClient myClient(ForestConfiguration configuration){
        return  configuration.createInstance(MyClient.class);
    }


}
