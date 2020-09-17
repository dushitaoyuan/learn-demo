package com.taoyuanx.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Properties;

/**
 * @author dushitaoyuan
 * @date 2019/9/11 11:11
 * @desc: 系统配置获取
 */
public final class PropertiesUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties SYSTEM_CONFIG = null;
    private static Environment ENV = null;
    private static volatile boolean INIT_CONFIG = false;
    private static volatile boolean INIT_ENV = false;
    private static final String DEFAULT_CONFIG = "classpath:application.properties";

    // 获取非默认配置文件
    public static Properties loadProperties(String configPath) {
        try {
            if (configPath.startsWith("classpath:")) {
                configPath = configPath.substring(configPath.indexOf("classpath:") + 10);
                Resource resource = new ClassPathResource(configPath);
                EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
                return PropertiesLoaderUtils.loadProperties(encodedResource);
            } else {
                EncodedResource encodedResource = new EncodedResource(new FileSystemResource(new File(configPath)),
                        "UTF-8");
                return PropertiesLoaderUtils.loadProperties(encodedResource);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 设置系统配置
    public static void initSystemConfig(Properties systemConfig) {
        if (systemConfig != null) {
            SYSTEM_CONFIG = systemConfig;
            INIT_CONFIG = true;
        }
    }

    public static void initEnvironment(Environment environment) {
        try {
            ENV = environment;
            INIT_ENV = true;
        } catch (Exception e) {
            LOG.warn("spring environment 不存在");
        }
    }


    /*
     * 获取系统配置
     * 配置源优先级:应用配置 > spring environment
     */
    public static String getSystemProperty(String key) {
        String value = null;
        if (INIT_CONFIG) {
            value = SYSTEM_CONFIG.getProperty(key);
        }
        if (!StringUtils.isEmpty(value)) {
            return value;
        }
        if (INIT_ENV) {
            value = ENV.getProperty(key);
        }
        return value;
    }

    static {
        doInitDefault();
    }


    //加载默认配置
    private static void doInitDefault() {
        try {
            SYSTEM_CONFIG = loadProperties(DEFAULT_CONFIG);
            INIT_CONFIG = true;
        } catch (Exception e) {
            LOG.warn("{}默认配置不存在", DEFAULT_CONFIG);
        }
    }


}
