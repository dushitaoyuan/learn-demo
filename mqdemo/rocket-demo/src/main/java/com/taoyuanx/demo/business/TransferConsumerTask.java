package com.taoyuanx.demo.business;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author dushitaoyuan
 * @desc 转账消费任务
 * @date 2020/7/7
 */
public abstract class TransferConsumerTask implements Runnable {
    private volatile boolean running = true;
    private LitePullConsumer litePullConsumer;

    public TransferConsumerTask(LitePullConsumer litePullConsumer) {
        this.litePullConsumer = litePullConsumer;
    }

    @Override
    public void run() {
        while (running) {
            List<MessageExt> messageExts = litePullConsumer.poll(300);
            if (CollectionUtil.isNotEmpty(messageExts)) {
                messageExts.stream().forEach(messageExt -> {
                    handle(messageExt);
                });
                litePullConsumer.commitSync();
            }
        }
    }

    public abstract void handle(MessageExt messageExt);

    public void stop() {
        running = false;
    }
}
