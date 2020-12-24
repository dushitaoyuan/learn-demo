package com.taoyuanx;


import com.alibaba.fastjson.JSON;
import com.taoyuanx.client.MyClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
public class ClientSpringTest {
    @Autowired
    MyClient myClient;

    @Test
    public void testClient() throws Exception {
        Thread.sleep(30000L);
        System.out.println(myClient.hello("23131231"));
        Map map=new HashMap<>();
        map.put("test","test");
        System.out.println(JSON.toJSONString(myClient.json(map)));

    }
}
