package com.taoyuanx.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author dushitaoyuan
 * @date 2019/9/1111:12
 * @desc: spring容器工具
 */
public class SpringContextUtil implements ApplicationContextAware, InitializingBean {
    private String configLocatioin;


    private static ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化配置
        if (StringUtils.isNotEmpty(configLocatioin)) {
            PropertiesUtil.initSystemConfig(PropertiesUtil.loadProperties(configLocatioin));
        }
        //初始化环境变量
        PropertiesUtil.initEnvironment(SpringContextUtil.getApplicationContext().getEnvironment());

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }





}
