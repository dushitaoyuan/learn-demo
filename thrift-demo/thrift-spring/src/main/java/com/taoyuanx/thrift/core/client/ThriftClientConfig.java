package com.taoyuanx.thrift.core.client;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import com.taoyuanx.thrift.core.ThriftConstant;
import io.airlift.drift.transport.netty.codec.Protocol;
import io.airlift.drift.transport.netty.codec.Transport;

import java.util.List;

import static io.airlift.drift.transport.netty.codec.Protocol.BINARY;
import static io.airlift.drift.transport.netty.codec.Transport.FRAMED;

/**
 * @author dushitaoyuan
 * @date 2021/4/1822:22
 */
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
    private List<String> ciphers = ImmutableList.of();

    private String trustCertificate;
    private String key;
    private String keyPassword;
    private long sessionCacheSize;
    private Integer sessionTimeout;

    private String serviceInterfaceName;

    public String[] getServerList() {
        return serverList;
    }

    public void setServerList(String[] serverList) {
        this.serverList = serverList;
    }

    private Class serviceInterfaceClass;

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterfaceName) {
        this.serviceInterfaceName = serviceInterfaceName;
    }

    public Class getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(Class serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Integer getMaxFrameSize() {
        return maxFrameSize;
    }

    public void setMaxFrameSize(Integer maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public HostAndPort getSocksProxy() {
        return socksProxy;
    }

    public void setSocksProxy(HostAndPort socksProxy) {
        this.socksProxy = socksProxy;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public List<String> getCiphers() {
        return ciphers;
    }

    public void setCiphers(List<String> ciphers) {
        this.ciphers = ciphers;
    }

    public String getTrustCertificate() {
        return trustCertificate;
    }

    public void setTrustCertificate(String trustCertificate) {
        this.trustCertificate = trustCertificate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public long getSessionCacheSize() {
        return sessionCacheSize;
    }

    public void setSessionCacheSize(long sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
