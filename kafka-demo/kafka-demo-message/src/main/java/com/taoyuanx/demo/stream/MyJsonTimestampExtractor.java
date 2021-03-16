package com.taoyuanx.demo.stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoyuanx.demo.stream.data.MyPageView;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.util.Objects;

public class MyJsonTimestampExtractor implements TimestampExtractor {
    String TIMESTAMP_KEY = "timestamp";

    @Override
    public long extract(final ConsumerRecord<Object, Object> record, final long partitionTime) {
        Headers headers = record.headers();
        if (Objects.nonNull(headers)) {
            Iterable<Header> time = headers.headers(TIMESTAMP_KEY);
            if (Objects.nonNull(time) && time.iterator().hasNext()) {
                Header next = time.iterator().next();
                return Long.parseLong(new String(next.value()));
            }
        }
        Object value = record.value();
        if (value instanceof MyPageView) {
            return ((MyPageView) value).getAccessTime();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject) record.value()).getLong(TIMESTAMP_KEY);
        }
        if (value instanceof String) {
            return JSON.parseObject(value.toString()).getLong(TIMESTAMP_KEY);
        }
        throw new IllegalArgumentException("MyJsonTimestampExtractor cannot recognize the record value " + record.value());
    }
}