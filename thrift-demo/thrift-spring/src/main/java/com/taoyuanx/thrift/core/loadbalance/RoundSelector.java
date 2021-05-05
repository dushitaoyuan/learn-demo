package com.taoyuanx.thrift.core.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dushitaoyuan
 * @date 2021/4/2422:51
 * @desc: 轮询
 */
public class RoundSelector extends AbstractSelector {


    @Override
    public ThriftServer doSelect(List<ThriftServer> serverList) {
        if (serverList.isEmpty()) {
            return null;
        }
        ThriftServer selectServer = serverList.size() == 1 ? serverList.get(round(serverList.size())) : serverList.get(0);
        return selectServer;
    }

    private AtomicInteger pos = new AtomicInteger(0);

    private int round(Integer size) {
        Integer index = pos.getAndIncrement();
        if (index < size) {
            return index;
        } else {
            pos.set(0);
            pos.incrementAndGet();
            return 0;
        }
    }
}
