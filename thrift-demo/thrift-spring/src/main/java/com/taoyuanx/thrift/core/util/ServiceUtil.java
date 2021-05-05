package com.taoyuanx.thrift.core.util;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dushitaoyuan
 * @date 2021/5/422:22
 */
public class ServiceUtil {
    private static Map<Class, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

    public static <T> List<T> loadService(Class<T> service) {
        if (INSTANCE_MAP.containsKey(service)) {
            return (List<T>) INSTANCE_MAP.get(service);
        }
        ServiceLoader<T> load = ServiceLoader.load(service);
        Iterator<T> iterator = load.iterator();
        List<T> serviceSPIList = new ArrayList<>();
        while (iterator.hasNext()) {
            serviceSPIList.add(iterator.next());
        }
        INSTANCE_MAP.put(service, serviceSPIList);
        return serviceSPIList;
    }

    public static <T> T loadSingleService(Class<T> service) {
        List<T> loadService = loadService(service);
        return Objects.nonNull(loadService) && loadService.size() > 0 ? loadService.get(0) : null;
    }


}
