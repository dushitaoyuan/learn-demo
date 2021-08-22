package com.taoyuanx.thrift.core.client;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ListenableFuture;
import com.taoyuanx.thrift.core.ThriftConstant;
import com.taoyuanx.thrift.core.util.ThriftContext;
import io.airlift.drift.client.MethodInvocationFilter;
import io.airlift.drift.transport.client.InvokeRequest;
import io.airlift.drift.transport.client.MethodInvoker;

import java.util.Map;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc client context处理
 * @date 2021/5/9 05-09
 */
public class ClientContextFilter implements MethodInvocationFilter {
    @Override
    public ListenableFuture<Object> invoke(InvokeRequest request, MethodInvoker next) {
        Map<String, Object> context = ThriftContext.getContext();
        if (Objects.nonNull(context) && !context.isEmpty()) {
            request.getHeaders().put(ThriftConstant.CONTEXT_KEY, JSON.toJSONString(context));
        }
        ListenableFuture<Object> invoke = next.invoke(request);
        ThriftContext.remove();
        return invoke;
    }
}
