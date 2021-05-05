package com.taoyuanx.thrift.core.loadbalance;

import io.airlift.drift.client.address.AddressSelector;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.Invocation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dushitaoyuan
 * @date 2021/4/2422:51
 */
public abstract class AbstractSelector implements AddressSelector<ThriftServer> {
    @Getter
    @Setter
    private volatile List<ThriftServer> serverList;

    @Override
    public Optional<ThriftServer> selectAddress(Optional<String> addressSelectionContext) {
        return Optional.ofNullable(doSelect(serverList));
    }


    public abstract ThriftServer doSelect(List<ThriftServer> serverList);

    int getWeight(ThriftServer thriftServer) {
 /*       int weight;
        weight = thriftServer.getWeight();
        if (weight > 0) {
            long timestamp = invoker.getUrl().getParameter(TIMESTAMP_KEY, 0L);
            if (timestamp > 0L) {
                long uptime = System.currentTimeMillis() - timestamp;
                if (uptime < 0) {
                    return 1;
                }
                int warmup = invoker.getUrl().getParameter(WARMUP_KEY, DEFAULT_WARMUP);
                if (uptime > 0 && uptime < warmup) {
                    weight = calculateWarmupWeight((int) uptime, warmup, weight);
                }
            }
        }
        return Math.max(weight, 0);*/
        return Math.max(thriftServer.getWeight(), 0);
    }
}
