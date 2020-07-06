
package com.taoyuanx.demo.config;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.business.service.TransferMoneyService;
import com.taoyuanx.demo.transaction.TransferMoneyMsg;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 转账消息监听
 */
@Component
public class TransferMoneyTransactionListenerImpl implements TransactionListener {
    @Autowired
    TransferMoneyService transferMoneyService;

    private ConcurrentHashMap<String, LocalTransactionState> localTrans = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        byte[] body = msg.getBody();
        TransferMoneyMsg transferMoneyMsg = JSON.parseObject(body, TransferMoneyMsg.class);
        if (transferMoneyMsg.getTarget().equals("fromA")) {
            transferMoneyService.updateMoneyForB(transferMoneyMsg.getAccount(), transferMoneyMsg.getMoney());
        } else if (transferMoneyMsg.getTarget().equals("fromB")) {
            transferMoneyService.updateMoneyForB(transferMoneyMsg.getAccount(), transferMoneyMsg.getMoney());

        }
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        if (localTrans.contains(msg.getTransactionId())) {
            return LocalTransactionState.UNKNOW;
        }
        return localTrans.get(msg.getTransactionId());
    }
}
