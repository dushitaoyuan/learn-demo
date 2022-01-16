package com.ncs.sprinbase.core.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @date 2019/12/17
 */
public class MapResultBuilder {
    private Map<String, Object> mapResult;

    private MapResultBuilder() {
        mapResult = new HashMap<>();
    }

    private MapResultBuilder(int size) {
        mapResult = new HashMap<>(size);
    }

    public Map<String, Object> build() {
        return mapResult;
    }

    public static MapResultBuilder newBuilder() {
        return new MapResultBuilder();
    }

    public static MapResultBuilder newBuilder(int size) {
        return new MapResultBuilder(size);
    }

    public MapResultBuilder put(String key, Object value) {
        mapResult.put(key, value);

        return this;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
