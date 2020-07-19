package com.taoyuanx.demo.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc es client
 * @date 2020/7/16
 */
@Configuration
public class EsClientConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
         ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("192.168.30.210:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}
