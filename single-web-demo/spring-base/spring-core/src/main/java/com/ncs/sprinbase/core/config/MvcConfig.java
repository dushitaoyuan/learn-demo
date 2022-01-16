package com.ncs.sprinbase.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ncs.sprinbase.core.commons.MapResultBuilder;
import com.ncs.sprinbase.core.commons.Result;
import com.ncs.sprinbase.core.commons.ResultBuilder;

/**
 * @author dushitaoyuan
 * @date 2019/9/1116:59
 * @desc: mvc配置
 */
@Configuration
public class MvcConfig {

    /**
     * 统一结果处理
     */
    @RestControllerAdvice(basePackages = "com.ncs.sprinbase.core.controller")
    public static class ResponseControllerAdviceHandler implements ResponseBodyAdvice<Object> {
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                      Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      ServerHttpRequest request, ServerHttpResponse response) {
            if (body == null) {
                return ResultBuilder.success();
            }

            if (body instanceof Result) {
                return body;
            }

            if (body instanceof ResponseEntity) {
                return body;
            }

            if (body instanceof MapResultBuilder) {
                body = ((MapResultBuilder) body).build();
            }

            return ResultBuilder.success(body);
        }

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }
    }
}


