package com.ncs.sprinbase.ioc.test;

import com.ncs.sprinbase.core.event.RegistEvent;
import com.ncs.sprinbase.core.service.ChildServiceImpl;
import com.ncs.sprinbase.core.service.SimpleUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dushitaoyuan
 * @date 2019/11/1
 */
public class IocContextTest {
    @Test
    public void contextTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SimpleUserService simpleUserService = applicationContext.getBean(SimpleUserService.class);
        Assert.assertNotNull(simpleUserService);
    }

    @Test
    public void childContextTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        ApplicationContext childApplicationContext = new ClassPathXmlApplicationContext(new String[]{"spring/childApplicationContext.xml"},applicationContext);

        SimpleUserService simpleUserService = childApplicationContext.getBean(SimpleUserService.class);
        System.out.println("父容器bean对子容器可见");
       try {
           ChildServiceImpl bean = applicationContext.getBean(ChildServiceImpl.class);
       }catch (Exception e){
           System.out.println("子容器bean对父容器不可见");
       }

    }
    @Test
    public void contextEvent() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        ApplicationContext childApplicationContext = new ClassPathXmlApplicationContext(new String[]{"spring/childApplicationContext.xml"},applicationContext);
        applicationContext.publishEvent(new RegistEvent(1L,1L));


    }

}
