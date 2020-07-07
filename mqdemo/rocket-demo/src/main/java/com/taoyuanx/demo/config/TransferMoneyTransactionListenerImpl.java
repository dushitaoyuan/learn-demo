
package com.taoyuanx.demo.config;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.business.dto.TransferDTO;
import com.taoyuanx.demo.business.service.TransferHelperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 转账消息监听
 */
@Slf4j
public class TransferMoneyTransactionListenerImpl implements TransactionListener {
    TransferHelperService transferHelperService;
    private ConcurrentHashMap<String, LocalTransactionState> localTrans = new ConcurrentHashMap<>();

    public TransferMoneyTransactionListenerImpl(TransferHelperService transferHelperService) {
        this.transferHelperService = transferHelperService;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            byte[] body = msg.getBody();
            TransferDTO transferMoneyMsg = JSON.parseObject(body, TransferDTO.class);
            if (transferMoneyMsg.getTransferType().equals("A->B")) {
                transferHelperService.updateMoneyForA(transferMoneyMsg.getFrom(), -transferMoneyMsg.getMoney());
                log.info("..................{} 阶段一 本地 A方操作成功.............", transferMoneyMsg.getTransferType());
                localTrans.put(msg.getTransactionId(), LocalTransactionState.COMMIT_MESSAGE);
                return LocalTransactionState.COMMIT_MESSAGE;
            } else if (transferMoneyMsg.getTransferType().equals("B->A")) {
                transferHelperService.updateMoneyForB(transferMoneyMsg.getFrom(), -transferMoneyMsg.getMoney());
                log.info("..................{}  阶段一 本地 B方操作成功.............", transferMoneyMsg.getTransferType());
                localTrans.put(msg.getTransactionId(), LocalTransactionState.COMMIT_MESSAGE);
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        } catch (Exception e) {
            log.error("执行本地事务失败", e);
            localTrans.put(msg.getTransactionId(), LocalTransactionState.ROLLBACK_MESSAGE);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        if (localTrans.contains(msg.getTransactionId())) {
            return localTrans.get(msg.getTransactionId());
        }
        return LocalTransactionState.UNKNOW;
    }
}
