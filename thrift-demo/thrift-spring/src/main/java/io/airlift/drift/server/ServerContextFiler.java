package io.airlift.drift.server;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ListenableFuture;
import com.taoyuanx.thrift.core.ThriftConstant;
import com.taoyuanx.thrift.core.util.ThriftContext;
import io.airlift.drift.transport.server.ServerInvokeRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc context过滤器
 * @date 2021/5/9 05-09
 */
public class ServerContextFiler implements MethodInvocationFilter {


    @Override
    public ListenableFuture<Object> invoke(ServerInvokeRequest request, io.airlift.drift.server.ServerMethodInvoker next) {
        Map<String, String> headers = request.getHeaders();
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            String contextValue = headers.get(ThriftConstant.CONTEXT_KEY);
            if (StringUtils.isNotEmpty(contextValue)) {
                Map map = JSON.parseObject(contextValue, Map.class);
                ThriftContext.setContext(map);
            }
        }
        ListenableFuture<Object> result = next.invoke(request);
        ThriftContext.remove();
        return result;
    }
}
