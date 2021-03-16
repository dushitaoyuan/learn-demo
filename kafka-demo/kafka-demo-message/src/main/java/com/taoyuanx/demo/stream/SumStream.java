package com.taoyuanx.demo.stream;

import com.taoyuanx.demo.KafkaProperties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class SumStream {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "sumstream");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.servers);
        prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 2000);
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<Object, Object> source = builder.stream("suminput");
        KTable<String, String> sum1 = source.filter((k, v) -> {
            String value = v.toString();
            return StringUtils.hasText(value) && value.matches("\\d+");
        }).map((key, value) ->
                new KeyValue<String, String>("sum", value.toString())
        ).groupByKey().reduce((x, y) -> {
            System.out.println("x: " + x + "    " + "y: " + y);
            Integer sum = Integer.valueOf(x) + Integer.valueOf(y);
            System.out.println("sum: " + sum);
            return sum.toString();
        });
        sum1.toStream().to("sumout");

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
