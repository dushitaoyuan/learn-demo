package com.taoyuanx.thrift.core.client;

import com.google.common.net.HostAndPort;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:17
 * @desc: 处理bean注解
 */
public class ThriftClientHandler implements BeanPostProcessor, DisposableBean {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ThriftClient.class)) {
                if (!field.getType().isInterface()) {
                    throw new BeanCreationException("@ThriftClient field must be declared as an interface:" + field.getName()
                            + " @Class " + beanClass.getName());
                }
                try {
                    ThriftClient thriftClient = field.getAnnotation(ThriftClient.class);
                    Class<?> serviceInterface = field.getType();
                    ThriftClientConfig thriftClientConfig = ClientHelper.toThriftClientConfig(thriftClient, serviceInterface);
                    Object proxy = ClientHelper.createProxy(serviceInterface, Arrays.stream(thriftClientConfig.getServerList()).map(ipPort -> {
                        return HostAndPort.fromString(ipPort);
                    }).collect(Collectors.toList()), thriftClientConfig);
                    field.setAccessible(true);
                    field.set(bean, proxy);
                } catch (Exception e) {
                    throw new MyThriftExceptioin("client config exception", e);
                }


            }
        }
        return bean;
    }


    @Override
    public void destroy() throws Exception {
        ClientHelper.close();
    }
}
