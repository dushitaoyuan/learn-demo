package com.taoyuanx.thrift.core.loadbalance;

import com.google.common.net.HostAndPort;
import io.airlift.drift.transport.client.Address;
import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2021/4/2422:53
 */
@Data
public class ThriftServer implements Address {

    /**
     * ip+port
     */
    private HostAndPort hostAndPort;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 服务上线时间
     */
    private long upTimestamp;

    /**
     * 服务预热时间
     */
    private int warmupTime;


}
