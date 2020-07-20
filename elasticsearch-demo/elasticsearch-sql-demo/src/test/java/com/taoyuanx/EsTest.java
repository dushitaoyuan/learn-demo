package com.taoyuanx;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.taoyuanx.demo.DemoBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
@Slf4j
public class EsTest {
    String indexName = "plume_log_run_20200720";
    String esJdbcUrl = "jdbc:elasticsearch://127.0.0.1:9200/" + indexName;

    @Test
    public void testEsSql() throws Exception {
        Properties properties = new Properties();
        properties.put(com.alibaba.druid.pool.DruidDataSourceFactory.PROP_URL, esJdbcUrl);
        properties.put(com.alibaba.druid.pool.DruidDataSourceFactory.PROP_CONNECTIONPROPERTIES, "client.transport.ignore_cluster_name=true");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
        String sql = "select * from "+indexName +"  where dtTime  >1595230221378 and content like '%测试%' order by dtTime";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            log.debug("appName:{},content :{},dtTime:{}",resultSet.getString("appName"),resultSet.getString("appName"),resultSet.getLong("dtTime"));
        }
        ps.close();
        connection.close();
        dds.close();


    }


}
