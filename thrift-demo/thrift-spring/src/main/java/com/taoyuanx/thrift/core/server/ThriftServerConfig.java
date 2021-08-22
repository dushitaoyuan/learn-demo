package com.taoyuanx.thrift.core.server;

import com.google.common.base.Objects;
import com.taoyuanx.thrift.core.ThriftConstant;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2021/4/1822:22
 */
public class ThriftServerConfig {
    private int port = ThriftConstant.PORT;
    private int ioThreadNum;
    private int workThreadNum;
    private int timeOut;
    private int acceptBacklog;
    private int maxFrameSize;
    private int requestTimeout;
    private int sslContextRefreshTime;
    private boolean allowPlaintext = true;
    private boolean sslEnabled;
    private List<String> ciphers;
    private String trustCertificate;
    private String key;
    private String keyPassword;
    private long sessionCacheSize;
    private int sessionTimeout;
    private boolean assumeClientsSupportOutOfOrderResponses = true;

    private String serviceInterface;

    private Class serviceInterfaceClass;

    private Object serviceImpl;

    private double version;

    private Integer weight = ThriftConstant.WEIGHT;

    private String netPrefix;

    private int warmupTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThriftServerConfig that = (ThriftServerConfig) o;
        return port == that.port && Double.compare(that.version, version) == 0 && Objects.equal(serviceInterface, that.serviceInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(port, serviceInterface, version);
    }

    public String getNetPrefix() {
        return netPrefix;
    }

    public void setNetPrefix(String netPrefix) {
        this.netPrefix = netPrefix;
    }

    public Integer getWeight() {
        return weight;
    }


    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getIoThreadNum() {
        return ioThreadNum;
    }

    public void setIoThreadNum(int ioThreadNum) {
        this.ioThreadNum = ioThreadNum;
    }

    public int getWorkThreadNum() {
        return workThreadNum;
    }

    public void setWorkThreadNum(int workThreadNum) {
        this.workThreadNum = workThreadNum;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getAcceptBacklog() {
        return acceptBacklog;
    }

    public void setAcceptBacklog(int acceptBacklog) {
        this.acceptBacklog = acceptBacklog;
    }

    public int getMaxFrameSize() {
        return maxFrameSize;
    }

    public void setMaxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getSslContextRefreshTime() {
        return sslContextRefreshTime;
    }

    public void setSslContextRefreshTime(int sslContextRefreshTime) {
        this.sslContextRefreshTime = sslContextRefreshTime;
    }

    public boolean isAllowPlaintext() {
        return allowPlaintext;
    }

    public void setAllowPlaintext(boolean allowPlaintext) {
        this.allowPlaintext = allowPlaintext;
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

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public boolean isAssumeClientsSupportOutOfOrderResponses() {
        return assumeClientsSupportOutOfOrderResponses;
    }

    public void setAssumeClientsSupportOutOfOrderResponses(boolean assumeClientsSupportOutOfOrderResponses) {
        this.assumeClientsSupportOutOfOrderResponses = assumeClientsSupportOutOfOrderResponses;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Class getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(Class serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
        if (serviceInterface == null) {
            this.serviceInterface = serviceInterfaceClass.getName();
        }
    }

    public Object getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public int getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(int warmupTime) {
        this.warmupTime = warmupTime;
    }
}
