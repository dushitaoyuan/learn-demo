package com.taoyuanx.demo.stream;

import com.taoyuanx.demo.KafkaProperties;
import com.taoyuanx.demo.stream.data.MyPageView;
import com.taoyuanx.demo.stream.data.producer.PageViewDataProducer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class MyPageViewCountStream {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "pageViewStream");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.servers);
        prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 2000);
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, MyJsonTimestampExtractor.class);
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, PageViewDataProducer.FastJsonSerDer.class);
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, PageViewDataProducer.FastJsonSerDer.class);
        StreamsBuilder builder = new StreamsBuilder();
        /**
         * 按照应用id和 页面id统计访问次数 每隔15分钟输出到指定topic
         */
        KStream<String, MyPageView> pageViewStream = builder.stream("pageView", Consumed.with(Serdes.String(), new PageViewDataProducer.FastJsonSerDer<>()));
        pageViewStream
                .map((k, v) -> {
                    return new KeyValue<String, String>(((MyPageView) v).getAppId(), "1");
                })
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .windowedBy(SessionWindows.with(Duration.ofMinutes(15)))
                .count().toStream().map((k, v) -> {
            if (Objects.isNull(v)) {
                return new KeyValue<String, Long>(k.toString(), 0L);
            }
            return new KeyValue<String, Long>(k.toString(), v);
        }).to("appCount-output");

        pageViewStream
                .map((k, v) -> {
                    return new KeyValue<String, String>(((MyPageView) v).getPageId(), "1");
                })
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .windowedBy(SessionWindows.with(Duration.ofMinutes(15)))
                .count()
                .toStream().map((k, v) -> {
            if (Objects.isNull(v)) {
                return new KeyValue<String, Long>(k.toString(), 0L);
            }
            return new KeyValue<String, Long>(k.toString(), v);

        }).to("pageCount-output");


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
