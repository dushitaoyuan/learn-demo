package com.taoyuanx.thrift.core.registry.impl;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import com.taoyuanx.thrift.core.registry.IServiceDiscovery;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.etcd.jetcd.watch.WatchEvent.EventType.*;

/**
 * @author dushitaoyuan
 * @date 2021/5/411:33
 */

@Slf4j
@AutoService(IServiceDiscovery.class)
public class Etcd3ServiceDiscovery implements IServiceDiscovery {
    private String ROOT_PATH = "services/";
    private Client client;

    private static final String SPLIT = "/";
    private static final int TIMEOUT_SECOND = 3;

    private static volatile boolean inited = false;


    private static Map<String, Object> services = new ConcurrentHashMap<>();

    private static Object empty = new Object();

    @Override
    public boolean registerService(ServiceInfo serviceInfo) {
        try {
            CompletableFuture<PutResponse> completableFuture = client.getKVClient().put(toServiceInstanceKey(serviceInfo), toValue(serviceInfo));
            completableFuture.get(TIMEOUT_SECOND, TimeUnit.SECONDS);
            log.info("register {} success", serviceInfo);
            return true;
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    @Override
    public List<ServiceInfo> discoveryService(String serviceName) {
        try {
            ByteSequence servicePrefix = toByte(toServiceKey(serviceName));
            GetResponse response = client.getKVClient().get(servicePrefix,
                    GetOption.newBuilder().withPrefix(servicePrefix).build())
                    .get(TIMEOUT_SECOND, TimeUnit.SECONDS);
            return response.getKvs().stream().map(kv -> {
                return (ServiceInfo) JSON.parseObject(kv.getValue().getBytes(), ServiceInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }


    @Override
    public boolean unRegister(ServiceInfo serviceInfo) {
        try {
            return client.getKVClient().delete(toServiceInstanceKey(serviceInfo)).get(TIMEOUT_SECOND, TimeUnit.SECONDS).getDeleted() > 0;
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }


    private void watch(ServiceInfo serviceInfo) {
        Watch watch = client.getWatchClient();
        Watch.Listener listener = Watch.listener(response -> {
            for (WatchEvent event : response.getEvents()) {
                log.info("type={}, key={}, value={}", event.getEventType(),
                        Optional.ofNullable(event.getKeyValue().getKey()).map(bs -> bs.toString(StandardCharsets.UTF_8)).orElse(""),
                        Optional.ofNullable(event.getKeyValue().getValue()).map(bs -> bs.toString(StandardCharsets.UTF_8))
                                .orElse(""));
                switch (event.getEventType()) {
                    case DELETE:
                        ByteSequence deleteKey = event.getPrevKV().getKey();
                        break;
                    case PUT:
                        KeyValue prevKV = event.getPrevKV();
                        KeyValue keyValue = event.getKeyValue();

                        break;

                    case UNRECOGNIZED:
                        break;
                    default:
                        break;

                }
            }
        }, e -> {
            log.error("service {} etcd   watch error", serviceInfo, e);
        });
        watch.watch(toByte(toServiceKey(serviceInfo.getServiceName())), listener);
    }

    @Override
    public void close() {
        if (Objects.nonNull(client)) {
            client.close();

        }
    }

    @Override
    public void init(String url) {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    this.client = Client.builder().endpoints(url.split(",")).build();
                }
            }

        }
    }

    private ByteSequence toByte(String str) {
        return ByteSequence.from(str, Charset.forName("UTF-8"));
    }


    private String toServiceKey(String serviceName) {
        return ROOT_PATH + serviceName + SPLIT;
    }

    private ByteSequence toServiceInstanceKey(ServiceInfo serviceInfo) {
        return toByte(toServiceKey(serviceInfo.getServiceName()) + serviceInfo.getIp() + ":" + serviceInfo.getPort());
    }

    private ByteSequence toValue(ServiceInfo serviceInfo) {
        return ByteSequence.from(JSON.toJSONBytes(serviceInfo));
    }

    private ByteSequence toString(String str) {
        return ByteSequence.from(str, Charset.forName("UTF-8"));
    }
}
