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

    private HostAndPort hostAndPort;

    private Integer weight;


}
