package com.taoyuanx.demo.stream.data;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class JsonTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(final ConsumerRecord<Object, Object> record, final long partitionTime) {
        if (record.value() instanceof PageViewTypedDemo.PageView) {
            return ((PageViewTypedDemo.PageView) record.value()).timestamp;
        }

        if (record.value() instanceof PageViewTypedDemo.UserProfile) {
            return ((PageViewTypedDemo.UserProfile) record.value()).timestamp;
        }

        if (record.value() instanceof JsonNode) {
            return ((JsonNode) record.value()).get("timestamp").longValue();
        }

        throw new IllegalArgumentException("JsonTimestampExtractor cannot recognize the record value " + record.value());
    }
}