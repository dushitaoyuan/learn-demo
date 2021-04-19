package com.taoyuanx.thrift.core.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:17
 * @desc: 处理bean注解
 */
public class ThriftClientConfig implements BeanPostProcessor {
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
                /*applicationContext.getBeansOfType(field.getType());
                field.setAccessible(true);
                if (candidates.size() == 1) {
                    field.set(bean, candidates.values().iterator().next());
                } else if (candidates.size() == 2) {
                    ThriftClient thriftClient = field.getAnnotation(ThriftClient.class);
                    Object proxy = null;
                    field.set(bean, proxy);
                } else {
                    throw new IllegalArgumentException("Find more than 2 beans for type: " + type);
                }*/

            }
        }
        return bean;
    }




}
