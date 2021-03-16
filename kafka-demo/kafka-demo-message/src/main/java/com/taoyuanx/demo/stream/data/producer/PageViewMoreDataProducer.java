package com.taoyuanx.demo.stream.data.producer;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.KafkaProperties;
import com.taoyuanx.demo.stream.data.MyPageView;
import com.taoyuanx.demo.stream.data.PageViewTypedDemo;
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
public class PageViewMoreDataProducer {

    public static void producer() {
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
        properties.put("value.serializer", PageViewTypedDemo.JSONSerde.class);

        KafkaProducer<String, Object> producer = new KafkaProducer<>(properties);
        int pageViewSize = 1000;
        List<PageViewTypedDemo.PageView> pageViewList = randomPageView(pageViewSize);

        for (int i = 0; i < pageViewSize; i++) {
            producer.send(new ProducerRecord<>("streams-pageview-input", pageViewList.get(i).user, pageViewList.get(i)));
        }

        List<PageViewTypedDemo.UserProfile> userProfileList = randomUserProfile(10);

        for (int i = 0; i < userProfileList.size(); i++) {
            String userId = "useId_" + (i % 5);
            producer.send(new ProducerRecord<>("streams-userprofile-input", userId, userProfileList.get(i)));
        }
        System.out.println("批量生产pageView消息成功,条数为:" + pageViewSize);


        producer.close();
    }


    private static List<PageViewTypedDemo.PageView> randomPageView(int dataSize) {
        List<PageViewTypedDemo.PageView> pageViewList = new ArrayList<>(dataSize);
        Long now = System.currentTimeMillis();
        for (int i = 0; i < dataSize; i++) {
            PageViewTypedDemo.PageView pageView = new PageViewTypedDemo.PageView();
            int mod = i % 5;
            pageView.page = "pageId_" + mod;
            pageView.user = "useId_" + mod;
            if (i > 0) {
                pageView.timestamp = now - TimeUnit.DAYS.toMillis(i % 7);
            } else {
                pageView.timestamp = now;
            }
            pageViewList.add(pageView);
        }

        return pageViewList;
    }

    private static List<PageViewTypedDemo.UserProfile> randomUserProfile(int userLen) {
        List<PageViewTypedDemo.UserProfile> us = new ArrayList<>(userLen);
        Long now = System.currentTimeMillis();
        for (int i = 0; i < userLen; i++) {
            PageViewTypedDemo.UserProfile user = new PageViewTypedDemo.UserProfile();
            int mod = i % 10;
            user.region = "region_" + mod;
            if (i > 0) {
                user.timestamp = now - TimeUnit.DAYS.toMillis(mod % userLen);
            } else {
                user.timestamp = now;
            }
            us.add(user);
        }

        return us;
    }

    public static void main(String[] args) {
        producer();
    }
}