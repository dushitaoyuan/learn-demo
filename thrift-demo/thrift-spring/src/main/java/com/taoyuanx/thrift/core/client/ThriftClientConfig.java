package com.taoyuanx.thrift.core.client;

import com.google.common.net.HostAndPort;
import com.taoyuanx.thrift.core.ThriftConstant;
import io.airlift.drift.transport.netty.codec.Protocol;
import io.airlift.drift.transport.netty.codec.Transport;
import lombok.Data;

import static io.airlift.drift.transport.netty.codec.Protocol.BINARY;
import static io.airlift.drift.transport.netty.codec.Transport.FRAMED;

/**
 * @author dushitaoyuan
 * @date 2021/4/1822:22
 */
@Data
public class ThriftClientConfig {
    private int port = ThriftConstant.PORT;

    private String[] serverList;
    private Transport transport = FRAMED;
    private Protocol protocol = BINARY;
    private Integer maxFrameSize;

    private Integer connectTimeout;
    private Integer requestTimeout;

    private HostAndPort socksProxy;

    private boolean sslEnabled;
    private String ciphers;

    private String trustCertificate;
    private String key;
    private String keyPassword;
    private long sessionCacheSize;
    private Integer sessionTimeout;

    private String serviceInterfaceName;

    private Class serviceInterfaceClass;

}
