package com.taoyuanx.thrift.core.loadbalance;

import io.airlift.drift.client.address.AddressSelector;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.Invocation;

import java.util.List;
import java.util.Optional;

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
        int weight;
        weight = thriftServer.getWeight();
        if (weight > 0) {
            long timestamp = thriftServer.getUpTimestamp();
            if (timestamp > 0L) {
                long uptime = System.currentTimeMillis() - timestamp;
                if (uptime < 0) {
                    return 1;
                }
                int warmup = thriftServer.getWarmupTime();
                if (uptime > 0 && uptime < warmup) {
                    weight = calculateWarmupWeight((int) uptime, warmup, weight);
                }
            }
        }
        return Math.max(weight, 0);
    }

    static int calculateWarmupWeight(int uptime, int warmup, int weight) {
        int ww = (int) (uptime / ((float) warmup / weight));
        return ww < 1 ? 1 : (Math.min(ww, weight));
    }


}
