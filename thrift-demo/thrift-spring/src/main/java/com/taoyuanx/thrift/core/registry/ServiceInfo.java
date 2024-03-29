package com.taoyuanx.thrift.core.registry;

import com.taoyuanx.thrift.core.ThriftConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @date 2021/5/411:06
 */
@Data
@EqualsAndHashCode(of = {"ip", "port", "serviceName"})
public class ServiceInfo implements Serializable {
    private String serviceName;
    private String ip;
    private Integer port;
    private Integer weight;
    private Long timestamp;

    private Integer warmupTime;
    private Map<String, String> mataInfo;


}
