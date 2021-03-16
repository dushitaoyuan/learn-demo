package com.taoyuanx.demo.stream;

import com.taoyuanx.demo.KafkaProperties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class WordCountStream {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcountstream");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.servers); //zookeeper的地址
        prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 2000);  //提交时间设置为2秒
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        /**
         * 处理流程java8 stream类似
         */
        KTable<String, Long> count = builder.stream("wordcount-input")
                .flatMapValues(
                        (value) -> {
                            return Arrays.asList(value.toString().split(" "));
                        })
                .map((k, v) -> {
                    return new KeyValue<String, String>(v, "1");
                }).groupByKey().count();
        count.toStream().foreach((k, v) -> {
            System.out.println("key:" + k + "   " + "value:" + v);
        });


        count.toStream().map((k, v) -> {
            return new KeyValue<String, String>(k, v.toString());
        }).to("wordcount-output");

        final Topology topo = builder.build();
        final KafkaStreams streams = new KafkaStreams(topo, prop);

        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("stream") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
            streams.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
