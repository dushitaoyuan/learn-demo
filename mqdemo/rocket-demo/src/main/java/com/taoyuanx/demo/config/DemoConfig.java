package com.taoyuanx.demo.config;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.business.TransferConsumerTask;
import com.taoyuanx.demo.business.dto.TransferDTO;
import com.taoyuanx.demo.business.service.TransferHelperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Configuration
@Slf4j
public class DemoConfig {
    public static final String MONEY_TRANSFER_TOPIC_A = "money_topic_A";
    public static final String MONEY_TRANSFER_TOPIC_B = "money_topic_B";
    public static final String TRANSFER_TYPE_A_B = "A->B";
    public static final String TRANSFER_TYPE_B_A = "B->A";
    @Value("${demo.rocketmq.name-server-address}")
    private String nameServerAddress;

    @Bean(name = "moneyTransferMqProducer", destroyMethod = "shutdown")
    public MQProducer moneyTransferMqProducer(TransferHelperService transferHelperService) {
        try {
            TransactionListener transactionListener = new TransferMoneyTransactionListenerImpl(transferHelperService);
            TransactionMQProducer producer = new TransactionMQProducer("moneyTransferProducer");
            producer.setNamesrvAddr(nameServerAddress);
            ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                }
            });
            producer.setExecutorService(executorService);
            producer.setTransactionListener(transactionListener);
            producer.start();
            return producer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A方转账消息消费者
     */
    @Bean(name = "moneyTransferMqConsumerA", destroyMethod = "shutdown")
    public LitePullConsumer moneyTransferMqConsumerA() throws MQClientException {
        DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("consumer-transfer-moneyA");
        litePullConsumer.setNamesrvAddr(nameServerAddress);
        litePullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        litePullConsumer.subscribe(MONEY_TRANSFER_TOPIC_A, "*");
        litePullConsumer.setAutoCommit(false);
        litePullConsumer.start();
        return litePullConsumer;
    }

    /**
     * B方转账消息消费者
     */
    @Bean(name = "moneyTransferMqConsumerB", destroyMethod = "shutdown")
    public LitePullConsumer moneyTransferMqConsumerB() throws MQClientException {
        DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("consumer-transfer-moneyB");
        litePullConsumer.setNamesrvAddr(nameServerAddress);
        litePullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        litePullConsumer.subscribe(MONEY_TRANSFER_TOPIC_B, "*");
        litePullConsumer.setAutoCommit(false);
        litePullConsumer.start();
        return litePullConsumer;
    }

    /**
     * 阶段二 必须成功,
     * 如果本地不成功,则重试,超过一定次数后,则人工干预,人工干预失败后,再通知付款方回滚
     */
    @Bean(name = "transferConsumerTaskB", destroyMethod = "stop")
    public TransferConsumerTask transferConsumerTaskB(TransferHelperService transferHelperService) throws MQClientException {
        TransferConsumerTask transferConsumerTask = new TransferConsumerTask(moneyTransferMqConsumerB()) {
            @Override
            public void handle(MessageExt messageExt) {
                TransferDTO msg = JSON.parseObject(messageExt.getBody(), TransferDTO.class);
                transferHelperService.updateMoneyForB(msg.getTo(), msg.getMoney());
                log.info("..................{} 阶段二 远端 B方操作成功,转账流程结束.............", msg.getTransferType());

            }
        };
        Thread thread = new Thread(transferConsumerTask);
        thread.setName("moneyTransferMqConsumerB-thread");
        thread.start();
        log.info("............B 转账队列消费线程启动");
        return transferConsumerTask;
    }

    @Bean(name = "transferConsumerTaskA", destroyMethod = "stop")
    public TransferConsumerTask transferConsumerTaskA(TransferHelperService transferHelperService) throws MQClientException {
        TransferConsumerTask transferConsumerTask = new TransferConsumerTask(moneyTransferMqConsumerA()) {
            @Override
            public void handle(MessageExt messageExt) {
                TransferDTO msg = JSON.parseObject(messageExt.getBody(), TransferDTO.class);
                transferHelperService.updateMoneyForA(msg.getTo(), msg.getMoney());
                log.info("..................{}  阶段二 远端 A方操作成功,转账流程结束.............", msg.getTransferType());
            }
        };
        Thread thread = new Thread(transferConsumerTask);
        thread.setName("transferConsumerTaskA-thread");
        thread.start();
        log.info("............A 转账队列消费线程启动");

        return transferConsumerTask;
    }
}
