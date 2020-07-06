package com.taoyuanx.demo.consumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoyuanx.demo.dto.DemoMsg;
import com.taoyuanx.demo.dto.MsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;


/**
 * 网站文本消费者
 */
@Component
@Slf4j
public class DemoConsumer {

    private AtomicLong group1Count = new AtomicLong();
    private AtomicLong group2Count = new AtomicLong();

    @KafkaListener(topics = {"dushitaoyuan"},
            groupId = "group1",
            concurrency = "20")
    public void handleMsgGroup(ConsumerRecord<String, String> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition) {
        String value = record.value();
        try {
            MsgDTO msgDTO = JSON.parseObject(value, MsgDTO.class);
            DemoMsg dataMsg = JSON.parseObject(msgDTO.getData(), DemoMsg.class);
            // Thread.sleep(2000);
            log.warn("group {} topic {}  partition {} msgId {} msgText{} 消费成功 ", "group1", topic, partition, msgDTO.getMsgId(), dataMsg.getMsgText());
        } catch (Exception e) {
            log.error("消费异常", e);
        }
        group1Count.incrementAndGet();
        ack.acknowledge();
        log.warn("线程id {} group1 消费总数 {}", Thread.currentThread().getId(), group1Count.get());

    }

  /*  @KafkaListener(topics = {"dushitaoyuan"},
            groupId = "group2",
            concurrency = "3")
    public void handleMsgGroup2(ConsumerRecord<String, String> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition) {
        String value = record.value();
        try {
            MsgDTO msgDTO = JSON.parseObject(value, MsgDTO.class);
            DemoMsg dataMsg = JSON.parseObject(msgDTO.getData(), DemoMsg.class);
            log.warn("group {}  topic {}  partition {} msgId {} msgText{} 消费成功 ", "group2", topic, partition, msgDTO.getMsgId(), dataMsg.getMsgText());
            //  Thread.sleep(2000);
        } catch (Exception e) {
            log.error("消费异常", e);
        }
        ack.acknowledge();
        group2Count.incrementAndGet();
        log.warn("线程id {} group2 消费总数 {}", Thread.currentThread().getId(), group2Count.get());

    }*/
}
