package com.taoyuanx.thrift.core.client;

import com.taoyuanx.thrift.core.ThriftConstant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2021/5/517:49
 */
public class ThriftServiceBean implements FactoryBean<Object>, ApplicationContextAware, InitializingBean, DisposableBean {
    private ThriftClientConfig thriftClientConfig;
    private static ClientProxyFactory clientProxyFactory = ClientProxyFactory.getInstance();
    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        return clientProxyFactory.getServiceProxy(thriftClientConfig);
    }

    @Override
    public Class<?> getObjectType() {
        return thriftClientConfig.getServiceInterfaceClass();
    }

    @Override
    public void destroy() throws Exception {
        clientProxyFactory.close(thriftClientConfig.getServiceInterfaceClass());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        clientProxyFactory.init(applicationContext.getEnvironment().getProperty(ThriftConstant.SERVICE_DISCOVERY_URL));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
