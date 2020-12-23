package com.taoyuanx.demo.client.entryhandler;

import com.alibaba.otter.canal.protocol.Message;

public class CanalMessagerLogger implements CanalMessageHandler {
    private static CanalClientPrint canalClientPrint = new CanalClientPrint();


    @Override
    public void handleMessage(Message message) {
        long batchId = message.getId();
        int size = message.getEntries().size();
        canalClientPrint.printSummary(message, batchId, size);
        canalClientPrint.printEntry(message.getEntries());

    }
}
