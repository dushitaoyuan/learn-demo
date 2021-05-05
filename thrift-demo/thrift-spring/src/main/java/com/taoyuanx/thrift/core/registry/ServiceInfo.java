package com.taoyuanx.thrift.core.registry;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @date 2021/5/411:06
 */
@Data
public class ServiceInfo implements Serializable {
    private String serviceName;
    private String ip;
    private Integer port;
    private Integer weight;
    private Long timestamp;
    private Map<String, String> mataInfo;
}
