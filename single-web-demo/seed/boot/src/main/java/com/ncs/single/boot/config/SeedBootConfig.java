package com.ncs.single.boot.config;

import com.ncs.commons.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author dushitaoyuan
 * @date 2019/9/2623:27
 * @desc: 系统配置
 */
@Configuration
public class SeedBootConfig {

    @Autowired
    Environment environment;

    @Bean
    public SpringContextUtil afterPropertiesSet() throws Exception {
        SpringContextUtil springContextUtil = new SpringContextUtil();
        springContextUtil.setDataCenterId(Integer.parseInt(environment.getProperty("mybatis-plus.global-config.datacenter-id")));
        springContextUtil.setWorkNodeId(Integer.parseInt(environment.getProperty("mybatis-plus.global-config.worker-id")));
        return springContextUtil;
    }

}
