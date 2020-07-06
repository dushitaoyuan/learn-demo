package com.taoyuanx.demo.config;

import com.taoyuanx.demo.transaction.TransactionListenerImpl;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.relation.RoleUnresolved;
import java.util.concurrent.*;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Configuration
public class DemoConfig {
    @Bean
    public TransactionMQProducer transferMoneyMq() {
        try {
            TransactionListener transactionListener = new TransactionListenerImpl();
            TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");
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
}
