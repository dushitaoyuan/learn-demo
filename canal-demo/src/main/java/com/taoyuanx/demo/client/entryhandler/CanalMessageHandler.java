package com.taoyuanx.demo.client.entryhandler;


import com.alibaba.otter.canal.protocol.Message;

/**
 * canal 订阅消息处理
 */
public interface CanalMessageHandler {
    void handleMessage(Message message);
}
