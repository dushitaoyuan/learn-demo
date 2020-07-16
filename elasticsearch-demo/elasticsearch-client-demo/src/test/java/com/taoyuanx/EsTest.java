package com.taoyuanx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoyuanx.demo.DemoBootApplication;
import com.taoyuanx.demo.es.dto.ContractDTO;
import com.taoyuanx.demo.es.dto.EsDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
@Slf4j
public class EsTest {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    String indexName = "contract";
    String mapping = " \"dynamic\": false,\n" +
            "            \"properties\" : {\n" +
            "                            \"contract_id\":{\n" +
            "                                \"type\" : \"long\",\n" +
            "                                \"index\":true\n" +
            "                            },  \n" +
            "                            \"contract_name\":{\n" +
            "                                \"type\":\"text\",\n" +
            "                                \"index\":true\n" +
            "                            },  \n" +
            "                            \"contract_code\":{\n" +
            "                                \"type\":\"keyword\",\n" +
            "                                \"index\":true\n" +
            "                            },  \n" +
            "                             \"contract_hash\":{\n" +
            "                                \"type\":\"keyword\"\n" +
            "                            },\n" +
            "                             \"plat_id\":{\n" +
            "                               \"type\" : \"long\",\n" +
            "                               \"index\":true\n" +
            "                            },\n" +
            "                             \"contract_figure\":{\n" +
            "                               \"type\" : \"keyword\",\n" +
            "                               \"index\":true\n" +
            "                            },\n" +
            "                            \"serial_number\":{\n" +
            "                               \"type\" : \"keyword\",\n" +
            "                               \"index\":true\n" +
            "                            },\n" +
            "                            \"create_time\":{\n" +
            "                                \"type\": \"date\",\n" +
            "                                \"index\":true\n" +
            "                            }\n" +
            "                        }";

    @Test
    public void testRestHighLevelClient() throws Exception {
        if (!existIndex(indexName)) {
            createIndex(indexName, mapping);
        }
        //保存
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setContract_id(2L);
        contractDTO.setContract_code("testcode");
        contractDTO.setContract_figure("figuretest");
        contractDTO.setContract_hash("md51111");
        contractDTO.setContract_name("测试合同002");
        contractDTO.setCreate_time(new Date());
        contractDTO.setSerial_number("2123456");
        contractDTO.setPlat_id(125L);
        EsDTO insertDTO = new EsDTO();
        insertDTO.setId(String.valueOf(contractDTO.getContract_id()));
        insertDTO.setJsonData(JSON.toJSONString(contractDTO));
        saveOrUpdate(indexName, insertDTO);
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("contract_name", "测试合同");
        SearchSourceBuilder searchSourceBuilder = searchSourceBuilder(matchQuery);
        SortBuilders.fieldSort("create_time").order(SortOrder.DESC);
        List<ContractDTO> search = search(indexName, searchSourceBuilder, ContractDTO.class);
        System.out.println(JSON.toJSONString(search));

    }

    public boolean existIndex(String indexName) throws IOException {
        return restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    }

    public void deleteIndex(String indexName) throws IOException {
        restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
    }

    public void createIndex(String indexName, String indexMapping) throws IOException {
        if (!this.existIndex(indexName)) {
            log.error("indexName {} 已存在", indexName);
            return;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        defaultIndexSetting(request);
        request.mapping(indexMapping, XContentType.JSON);
        CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        if (!res.isAcknowledged()) {
            log.error("创建索引失败");
            throw new RuntimeException("创建索引失败");
        }
    }

    public void saveOrUpdate(String indexName, EsDTO esDTO) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.id(esDTO.getId());
        request.source(esDTO.getJsonData(), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        if (indexResponse.status().equals(RestStatus.OK)) {
            log.debug("新增成功");
        }

    }

    public void insertBatch(String indexName, List<EsDTO> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(indexName).id(item.getId())
                .source(item.getJsonData(), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void deleteBatch(String indexName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(indexName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> search(String indexName, SearchSourceBuilder builder, Class<T> resultClass) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        if (hits != null && hits.length > 0) {
            return Arrays.stream(hits).map(hit -> {
                return JSON.parseObject(hit.getSourceAsString(), resultClass);
            }).collect(Collectors.toList());
        }
        return null;


    }

    public void defaultIndexSetting(CreateIndexRequest request) {
        /*request.settings(Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));*/
    }

    public static SearchSourceBuilder searchSourceBuilder(QueryBuilder queryBuilder, int from, int size, int timeout) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(timeout, TimeUnit.SECONDS));
        return sourceBuilder;
    }

    public static SearchSourceBuilder searchSourceBuilder(QueryBuilder queryBuilder) {
        return searchSourceBuilder(queryBuilder, 0, 10, 60);
    }

}
