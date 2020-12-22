package com.taoyuanx.demo.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.taoyuanx.demo.config.CanalProperties;
import com.taoyuanx.demo.util.CanalClientPrint;
import com.taoyuanx.demo.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleCanalClient extends CanalClient {

    private final static int BATCH_SIZE = 1000;
    protected CanalProperties cannalConfig;

    public SimpleCanalClient(CanalProperties cannalConfig) {
        this.cannalConfig = cannalConfig;
    }


    @Override
    protected void doStart() {
        log.info("cannal 启动");
        CanalClientPrint canalClientPrint = new CanalClientPrint();
        new Thread(() -> {
            // 创建链接
            CanalConnector connector = null;
            try {
                connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111), "example", "", "");
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
                        canalClientPrint.printSummary(message, batchId, size);
                        canalClientPrint.printEntry(message.getEntries());
                    }
                    if (batchId != -1) {
                        connector.ack(batchId); // 提交确认
                    }
                }
            } catch (Exception e) {
                log.error("订阅异常", e);
                connector.rollback();
                runing = false;
            } finally {
                if (Objects.nonNull(connector)) {
                    connector.disconnect();
                }
            }
        }).start();
    }

    @Override
    protected void doStop() {
        log.info("cannal 停止");
    }


}
