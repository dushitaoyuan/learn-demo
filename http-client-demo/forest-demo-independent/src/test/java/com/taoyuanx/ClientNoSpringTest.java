package com.taoyuanx;


import com.alibaba.fastjson.JSON;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.ssl.SSLUtils;
import com.taoyuanx.client.MyClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientNoSpringTest {
    MyClient myClient = null;

    @Before
    public void init() {
        ForestConfiguration configuration = ForestConfiguration.configuration();
        configuration.setMaxConnections(123);
        configuration.setMaxRouteConnections(222);
        configuration.setTimeout(3000);
        configuration.setConnectTimeout(2000);
        configuration.setRetryCount(1);
        configuration.setSslProtocol(SSLUtils.SSLv3);
        configuration.setLogEnabled(true);
        configuration.setBackendName("okhttp3");
        configuration.setCacheEnabled(true);
        myClient = configuration.createInstance(MyClient.class);
        System.out.println(configuration.createInstance(MyClient.class));

    }

    @Test
    public void testClient() throws Exception {
        System.out.println(myClient.hello("23131231"));
        Map map = new HashMap<>();
        map.put("test", "test");
        System.out.println(JSON.toJSONString(myClient.json(map)));

    }
}
