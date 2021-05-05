package com.taoyuanx.thrift.core;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2021/5/319:33
 * @desc: TODO
 */
public class EtcdTest {
    public static void main(String[] args) throws Exception {
        // create client
        Client client = Client.builder().endpoints("http://192.168.30.154:2380").build();
        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("com.taoyuanx.demo".getBytes());
        kvClient.put(key, ByteSequence.from("com.taoyuanx.demo".getBytes()));
        long id = client.getLeaseClient()
                .grant(3000)
                .get(3000, TimeUnit.MILLISECONDS)
                .getID();
        kvClient.put(ByteSequence.from("com.taoyuanx.demo/service1".getBytes()),
                ByteSequence.from("127.0.0.1:9091".getBytes()), PutOption.newBuilder().withLeaseId(id).build());
        kvClient.put(ByteSequence.from("com.taoyuanx.demo/service2".getBytes()),
                ByteSequence.from("127.0.0.1:9092".getBytes()), PutOption.newBuilder().withLeaseId(id).build());

        CompletableFuture<GetResponse> getFuture = kvClient.get(key, GetOption.newBuilder().withPrefix(key).build());

        GetResponse response = getFuture.get();
        response.getKvs().stream().forEach(keyValue -> {
            System.out.println(new String(keyValue.getKey().getBytes()) + "\t" + new String(keyValue.getValue().getBytes()));
        });


    }
}
