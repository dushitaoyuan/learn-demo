package com.taoyuanx.thrift.core.server;

import com.taoyuanx.thrift.core.ThriftConstant;
import lombok.Data;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2021/4/1822:22
 */
@Data
public class ThriftServer {
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


}
