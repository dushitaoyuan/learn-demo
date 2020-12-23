package com.taoyuanx.demo.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.taoyuanx.demo.client.entryhandler.CanalMessageHandler;
import com.taoyuanx.demo.config.CanalProperties;
import com.taoyuanx.demo.client.entryhandler.CanalClientPrint;
import com.taoyuanx.demo.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleCanalClient extends CanalClient {

    private final static int BATCH_SIZE = 1000;
    protected CanalProperties cannalConfig;
    protected CanalMessageHandler canalMessageHandler;

    public SimpleCanalClient(CanalProperties cannalConfig, CanalMessageHandler canalMessageHandler) {
        this.cannalConfig = cannalConfig;
        this.canalMessageHandler = canalMessageHandler;
    }


    @Override
    protected void doStart() {
        log.info("canal 启动");
        new Thread(() -> {
            // 创建链接
            CanalConnector connector = null;
            try {
                connector = CanalConnectors.newSingleConnector(new InetSocketAddress(cannalConfig.getServer(), cannalConfig.getServerPort()), "example", "", "");
                //打开连接
                connector.connect();
                //订阅数据库表,全部表
                connector.subscribe(".*\\..*");
                //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (runing) {
                    Message message = connector.getWithoutAck(BATCH_SIZE);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        ThreadUtil.sleep(2, TimeUnit.SECONDS);
                    } else {
                        canalMessageHandler.handleMessage(message);
                    }
                    if (batchId != -1) {
                        connector.ack(batchId); // 提交确认
                    }
                }

            } catch (Exception e) {
                log.error("订阅异常,程序停止", e);
                connector.rollback();
                runing = false;
            } finally {
                if (Objects.nonNull(connector)) {
                    connector.disconnect();
                    log.info("canal connector 连接断开");
                }
            }
        }).start();
    }

    @Override
    protected void doStop() {
        log.info("canal 停止");
    }


}
