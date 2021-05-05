package com.taoyuanx.thrift.core.loadbalance;

import io.airlift.drift.client.address.AddressSelector;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dushitaoyuan
 * @date 2021/4/2422:51
 * @desc: 轮询
 */
public class RandomSelector extends AbstractSelector {




    @Override
    public ThriftServer doSelect(List<ThriftServer> serverList) {
        if (serverList == null || serverList.isEmpty()) {
            return null;
        }
        if (serverList.size() == 1) {
            return serverList.get(0);
        }
        return serverList.get(ThreadLocalRandom.current().nextInt(serverList.size()));
    }
}
