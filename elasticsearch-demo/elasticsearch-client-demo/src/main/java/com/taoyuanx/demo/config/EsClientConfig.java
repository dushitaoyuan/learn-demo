package com.taoyuanx.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc es client
 * @date 2020/7/16
 */
public class EsClientConfig {
    @Bean(destroyMethod ="close" )
    public RestHighLevelClient esClient(EsProperties esProperties) {
        List<HttpHost> httpHostList = Arrays.stream(esProperties.getServers().split(",")).map(server -> {
            String[] split = server.split(":");
            return new HttpHost(split[0], Integer.parseInt(split[1]));
        }).collect(Collectors.toList());
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(httpHostList.toArray(new HttpHost[httpHostList.size()])));
        return client;
    }
}
