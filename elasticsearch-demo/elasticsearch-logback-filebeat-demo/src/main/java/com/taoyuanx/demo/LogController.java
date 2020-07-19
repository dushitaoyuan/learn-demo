package com.taoyuanx.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dushitaoyuan
 * @date 2020/7/1913:56
 */
@RestController
@Slf4j
public class LogController {
    @GetMapping("logdemo")
    public String logTest() throws Exception {
        log.info("{} 删除了 {}", "管理员用户", "测试用户");
        log.warn("测试告警");
        log.debug("xxxx执行 debug");
        log.error("xxx异常", new RuntimeException("运行异常"));
        return "log success";
    }

}
