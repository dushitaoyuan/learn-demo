package com.taoyuanx.thrift.core.client;

import com.taoyuanx.thrift.core.ThriftConstant;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:17
 * @desc: 处理bean注解
 */
public class ThriftClientHandler implements BeanPostProcessor, DisposableBean, InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;


    private static ClientProxyFactory CLIENT_PROXY_FACTORY = ClientProxyFactory.getInstance();


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
                    field.setAccessible(true);
                    ThriftClient thriftClient = field.getAnnotation(ThriftClient.class);
                    Class<?> serviceInterface = field.getType();
                    ThriftClientConfig clientConfig = CLIENT_PROXY_FACTORY.buildThriftClientConfig(thriftClient, serviceInterface);
                    Object proxy = CLIENT_PROXY_FACTORY.getServiceProxy(clientConfig);
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
        Optional.ofNullable(CLIENT_PROXY_FACTORY).ifPresent(ClientProxyFactory::close);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        CLIENT_PROXY_FACTORY.init(applicationContext.getEnvironment().getProperty(ThriftConstant.SERVICE_DISCOVERY_URL));
    }
}
