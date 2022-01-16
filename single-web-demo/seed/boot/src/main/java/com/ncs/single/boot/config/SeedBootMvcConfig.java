package com.ncs.single.boot.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ncs.commons.api.MapResultBuilder;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.single.boot.exception.SystemExceptionHandler;
import com.ncs.single.boot.interceptor.StaticTokenAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dushitaoyuan
 * @desc mvc相关配置
 * @date 2019/12/23
 */
@Configuration
public class SeedBootMvcConfig implements WebMvcConfigurer {

/*    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        //自定义异常拦截器优先级最高
        resolvers.add(systemExceptionHandler());
    }*/

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SystemExceptionHandler systemExceptionHandler() {
        return new SystemExceptionHandler();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String index = "index";
        registry.addViewController("/").setViewName(index);
        registry.addViewController("/index").setViewName(index);


    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //fastjson 转换
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.TEXT_PLAIN);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastJsonHttpMessageConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new StaticTokenAuthInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/**/*.css", "/**/*.html", "/**/*.js");
    }

    /**
     * 统一结果处理
     */
    @RestControllerAdvice(basePackages = "com.ncs.single.boot.controller")
    public static class ResponseControllerAdviceHandler implements ResponseBodyAdvice<Object> {


        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
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


    }
}
