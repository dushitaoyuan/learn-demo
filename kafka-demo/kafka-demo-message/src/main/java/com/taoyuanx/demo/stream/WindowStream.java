package com.taoyuanx.demo.stream;

import com.taoyuanx.demo.KafkaProperties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class WindowStream {
    /**
     * 每隔2秒钟输出一次过去5秒内topicA里的wordcount
     */
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "WindowStream");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.servers);
        prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 3000);
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("windowWordCountInput");
        KTable<Windowed<String>, Long> countKtable = source.flatMapValues(
                value ->
                        Arrays.asList(value.toString().split(" ")))
                .map((k, v) -> {
                    return new KeyValue<String, String>(v, "1");
                }).groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(5))
                        .advanceBy(Duration.ofSeconds(2)))
                .count();

        //为了方便查看，输出到控制台
        countKtable.toStream().foreach((k, v) -> {
            System.out.println("k: " + k + "  v: " + v);
        });

        countKtable.toStream().map((x, y) -> {
            return new KeyValue<String, String>(x.toString(), y.toString());
        }).to("windowWordCountOutput");

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
