package com.ncs.demo;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.demo.DemoBootApplication;
import com.taoyuanx.demo.dto.DemoMsg;
import com.taoyuanx.demo.dto.MsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author dushitaoyuan
 * @desc 测试生产者
 * @date 2020/6/2
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @Test
    public void produceMsg() {
        String msgText = "demoMsg=====";
        for (int i = 0; i < 100; i++) {
            String msg = JSON.toJSONString(msgWraper(String.valueOf(i
            ), msgText + i));
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("dushitaoyuan", msg);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info(" - 生产者 发送消息失败：" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    log.info(" - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                }
            });
        }

    }

    public MsgDTO msgWraper(String msgId, String msgText) {
        DemoMsg demoMsg = new DemoMsg();
        demoMsg.setMsgId(msgId);
        demoMsg.setMsgText(msgText);
        return new MsgDTO(msgId, JSON.toJSONBytes(demoMsg));
    }


}
