package com.taoyuanx.thrift.core.loadbalance;

import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dushitaoyuan
 * @date 2021/4/2422:51
 * @desc: 加权轮询
 */
public class RoundWeightSelector extends AbstractSelector {
    private static final int RECYCLE_PERIOD = 60000;

    protected static class WeightedRoundRobin {
        private int weight;
        private AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    private ConcurrentMap<String, WeightedRoundRobin> weightMap = new ConcurrentHashMap<String, WeightedRoundRobin>();

    @Override
    public ThriftServer doSelect(List<ThriftServer> serverList) {
        if (serverList.isEmpty()) {
            return null;
        }
        if (serverList.size() == 1) {
            return serverList.get(0);
        }
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        ThriftServer selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;
        for (ThriftServer server : serverList) {
            String key = server.getHostAndPort().toString();
            int weight = getWeight(server);
            WeightedRoundRobin weightedRoundRobin = weightMap.computeIfAbsent(key, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });

            if (weight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInvoker = server;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }
        if (selectedInvoker != null) {
            selectedWRR.sel(totalWeight);
            return selectedInvoker;
        }
        // should not happen here
        return serverList.get(0);
    }


}
