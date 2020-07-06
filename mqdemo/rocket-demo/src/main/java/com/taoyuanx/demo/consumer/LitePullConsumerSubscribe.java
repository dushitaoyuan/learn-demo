package com.taoyuanx.demo.consumer;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

public class LitePullConsumerSubscribe {

    public static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("lite_pull_consumer_test");
        litePullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        litePullConsumer.subscribe("demotopic", "*");
        litePullConsumer.start();
        try {
            while (running) {
                List<MessageExt> messageExts = litePullConsumer.poll();
                messageExts.stream().forEach(messageExt -> {
                    System.out.print("msgid:" + messageExt.getMsgId());
                    System.out.print("\t");
                    System.out.print("msgkeys:" + messageExt.getKeys());
                    System.out.print("\t");
                    System.out.println("msg:" + new String(messageExt.getBody()));
                    System.out.print("\t");
                    System.out.println("msg tag:" + messageExt.getTags());
                    System.out.print("\t");
                });
            }
        } finally {
            litePullConsumer.shutdown();
        }
    }
}
