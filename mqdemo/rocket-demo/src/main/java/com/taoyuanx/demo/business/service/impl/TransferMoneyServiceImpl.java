package com.taoyuanx.demo.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.business.dto.TransferDTO;
import com.taoyuanx.demo.business.service.TransferMoneyService;
import com.taoyuanx.demo.config.DemoConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Service
@Slf4j
public class TransferMoneyServiceImpl implements TransferMoneyService {

    @Autowired
    MQProducer moneyTransferMqProducer;


    @Override
    public void transferMoney(TransferDTO transferDTO) throws Exception {
        Message transferMoneyMsg = null;
        log.info("..................{} 转账开始.............", transferDTO.getTransferType());
        if (transferDTO.getTransferType().equals(DemoConfig.TRANSFER_TYPE_A_B)) {
            transferMoneyMsg = new Message(DemoConfig.MONEY_TRANSFER_TOPIC_B, JSON.toJSONBytes(transferDTO));
        } else if (transferDTO.getTransferType().equals(DemoConfig.TRANSFER_TYPE_B_A)) {
            transferMoneyMsg = new Message(DemoConfig.MONEY_TRANSFER_TOPIC_A, JSON.toJSONBytes(transferDTO));
        }
        transferMoneyMsg.setKeys(transferDTO.getSerialNo());
        transferMoneyMsg.setTags(transferDTO.getTransferType());
        moneyTransferMqProducer.sendMessageInTransaction(transferMoneyMsg, null);


    }
}
