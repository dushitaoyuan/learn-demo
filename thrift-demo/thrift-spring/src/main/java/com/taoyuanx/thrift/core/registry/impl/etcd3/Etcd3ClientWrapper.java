package com.taoyuanx.thrift.core.registry.impl.etcd3;

import com.google.protobuf.ByteString;
import com.ibm.etcd.api.PutRequest;
import com.ibm.etcd.api.PutRequestOrBuilder;
import com.ibm.etcd.api.RangeRequest;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.EtcdClient;
import com.ibm.etcd.client.kv.KvClient;
import com.ibm.etcd.client.kv.WatchUpdate;
import com.ibm.etcd.client.lease.PersistentLease;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import io.grpc.stub.StreamObserver;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc etcd client 包装
 * @date 2021/5/6 05-06
 */
@Slf4j
public class Etcd3ClientWrapper {


    private EtcdClient etcdClient;
    private PersistentLease lease;

    public Etcd3ClientWrapper(String url) {
        this(url, false);
    }

    public Etcd3ClientWrapper(String url, boolean withLease) {
        etcdClient = EtcdClient.forEndpoints(url).withPlainText().build();
        if (withLease) {
            withLease();
        }
    }

    private void withLease() {
        if (Objects.isNull(lease)) {
            synchronized (this) {
                if (Objects.isNull(lease)) {
                    StreamObserver<PersistentLease.LeaseState> observer = new StreamObserver<PersistentLease.LeaseState>() {
                        @Override
                        public void onNext(PersistentLease.LeaseState value) {

                        }

                        @Override
                        public void onError(Throwable t) {
                            log.error("keepAlive error", t);
                        }

                        @Override
                        public void onCompleted() {
                        }
                    };
                    lease = etcdClient.getLeaseClient().maintain().start(observer);
                }
            }

        }
    }


    public boolean putEphemeral(String key, String value) {
        try {
            etcdClient.getKvClient().put(PutRequest.newBuilder().setKey(ByteString.copyFromUtf8(key))
                    .setLease(lease.getLeaseId()).setValue(ByteString.copyFromUtf8(value)).build()).get(EtcdHelper.TIMEOUT_SECOND, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    public boolean putEphemeral(Map<String, String> keyValue) {
        try {
            KvClient.FluentTxnOps<?> batch = etcdClient.getKvClient().batch();
            keyValue.entrySet().stream().forEach(kv -> {
                batch.put(PutRequest.newBuilder().setKey(ByteString.copyFromUtf8(kv.getKey()))
                        .setLease(lease.getLeaseId()).setValue(ByteString.copyFromUtf8(kv.getValue())).build());

            });
            batch.async().get(EtcdHelper.TIMEOUT_SECOND * 2, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    public boolean delete(String key) {
        try {
            etcdClient.getKvClient().delete(ByteString.copyFromUtf8(key)).async().get(EtcdHelper.TIMEOUT_SECOND, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    public List<String> listByKeyPrefix(String keyPrefix) {
        try {
            RangeResponse rangeResponse = etcdClient.getKvClient().get(ByteString.copyFromUtf8(keyPrefix)).asPrefix().async().get(EtcdHelper.TIMEOUT_SECOND, TimeUnit.SECONDS);
            return rangeResponse.getKvsList().stream().map(kv -> {
                return kv.getValue().toStringUtf8();
            }).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    public void watchKeyPrefix(String key, StreamObserver<WatchUpdate> observer) {
        try {
            etcdClient.getKvClient().watch(ByteString.copyFromUtf8(key)).asPrefix().start(observer);
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }


    public void close() {
        try {
            Optional.ofNullable(etcdClient).ifPresent(EtcdClient::close);
        } catch (Exception e) {
        }

    }
}
