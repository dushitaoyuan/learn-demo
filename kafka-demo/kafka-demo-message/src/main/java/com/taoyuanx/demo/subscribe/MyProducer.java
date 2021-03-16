package com.taoyuanx.demo.subscribe;

import com.taoyuanx.demo.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class MyProducer {
    /**
     * 创建 topic
     * kafka-topics.bat --create --zookeeper localhost:2181/kafka --partitions 1 --replication-factor 1 --topic demo
     */
    public static void main(String[] args) {

        Properties properties = new Properties();

        properties.put("bootstrap.servers", KafkaProperties.servers);
        // ack
        properties.put("acks", "all");
        // 重试次数
        properties.put("retries", 1);
        // 批次大小
        properties.put("batch.size", 16384);
        // 等待时间
        properties.put("linger.ms", 1);
        // 缓冲区大小
        properties.put("buffer.memory", 33554432);
        // 设置数据key和value的序列化处理类
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", StringSerializer.class);

        KafkaProducer<String, String> produce = new KafkaProducer<>(properties);

        for (int i=0; i < 1000; i++) {
            produce.send(new ProducerRecord<>(KafkaProperties.demoTopic,"key-"+i,"value--" + i));
        }

        produce.close();



    }
}