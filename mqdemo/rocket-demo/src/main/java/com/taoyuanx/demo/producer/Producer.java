
package com.taoyuanx.demo.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Random;

public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {

        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        int size = 1000;
        String[] tagArray = new String[]{"taga", "tagb", "tagc", "tagd"};
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            try {
                {
                    String key = i + "";
                    String tag = tagArray[random.nextInt(tagArray.length)];
                    Message msg = new Message("demotopic", tag, key, ("Hello world" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    SendResult sendResult = producer.send(msg);
                    System.out.printf("%s%n", sendResult);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        producer.shutdown();
    }
}
