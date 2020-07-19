package com.taoyuanx;


import com.taoyuanx.demo.DemoBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
@Slf4j
public class LogTest {

    @Test
    public void logTest() throws Exception {
        log.info("{} 删除了 {}", "管理员用户", "测试用户");
        log.warn("测试告警");
        log.debug("xxxx执行 debug");
        log.error("xxx异常", new RuntimeException("运行异常"));


    }


}
