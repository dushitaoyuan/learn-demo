package com.taoyuanx.thrift.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lianglei09
 * @desc 上下文获取
 * @date 2021/5/9 05-09
 */
public class ThriftContext {
    private static final ThreadLocal<Map> CONTEXT = new ThreadLocal() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap();
        }
    };

    public static void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    public static String get(String key) {
        Object value = CONTEXT.get().get(key);
        return value.toString();
    }

    public static Map getContext() {
        return CONTEXT.get();
    }

    public static void setContext(Map<String, Object> context) {
        CONTEXT.set(context);
    }

    public static void remove() {
        CONTEXT.remove();
    }
}
