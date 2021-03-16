package com.taoyuanx.demo.stream.data.producer;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.KafkaProperties;
import com.taoyuanx.demo.stream.data.MyPageView;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc pageview数据构造
 * @date 2021/3/15
 */
public class PageViewDataProducer {

    public static void producer(String topic, int dataSize) {
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
        properties.put("value.serializer", FastJsonSerDer.class);

        KafkaProducer<String, MyPageView> producer = new KafkaProducer<>(properties);
        List<MyPageView> pageViewList = randomPageView(dataSize);

        for (int i = 0; i < dataSize; i++) {
            producer.send(new ProducerRecord<>(topic, "key-" + i, pageViewList.get(i)));
        }
        System.out.println("批量生产pageView消息成功,条数为:" + dataSize);

        producer.close();
    }



    private static List<MyPageView> randomPageView(int dataSize) {
        List<MyPageView> pageViewList = new ArrayList<>(dataSize);
        Long now = System.currentTimeMillis();
        for (int i = 0; i < dataSize; i++) {
            MyPageView pageView = new MyPageView();
            int mod = i % 10;
            pageView.setAppId("app_" + mod);
            pageView.setPageId("pageId_" + mod);
            if (i > 0) {
                pageView.setAccessTime(now - TimeUnit.MINUTES.toMillis(i));
            } else {
                pageView.setAccessTime(now);
            }
            pageViewList.add(pageView);
        }

        return pageViewList;
    }

    public static class FastJsonSerDer<T> implements Serializer<T>, Deserializer<T>, Serde<T> {
        @Override
        public void configure(final Map<String, ?> configs, final boolean isKey) {
            System.out.println("isKey:" + isKey);
            System.out.println(configs);
        }

        @Override
        public T deserialize(final String topic, final byte[] data) {
            if (data == null) {
                return null;
            }
            try {
                return (T) JSON.parseObject(data, MyPageView.class);
            } catch (final Exception e) {
                throw new SerializationException(e);
            }
        }

        @Override
        public byte[] serialize(final String topic, final T data) {
            if (data == null) {
                return null;
            }
            try {
                return JSON.toJSONBytes(data);
            } catch (final Exception e) {
                throw new SerializationException("Error serializing JSON message", e);
            }
        }

        @Override
        public void close() {
        }

        @Override
        public Serializer<T> serializer() {
            return this;
        }

        @Override
        public Deserializer<T> deserializer() {
            return this;
        }
    }

    public static void main(String[] args) {
        producer("pageView", 1000);
    }
}