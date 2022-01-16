package com.ncs.springbase.core.aop.test;

import com.ncs.springbase.service.HelloService;
import com.ncs.springbase.service.HiService;
import com.ncs.springbase.service.impl.HelloServiceImpl;
import com.ncs.springbase.service.impl.HiServiceImpl;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @date 2019/12/290:07
 * @desc: lib (LTW 加载时时织入) 启动 vm参数指定 javaagent -javaagent:path/aspectjweaver.jar
 */
public class AspectAnnoTest {
    @Test
    public void annoTest() {
        HelloService helloService = new HelloServiceImpl();
        HiService hiService = new HiServiceImpl();
        helloService.sayHello();
        split();
        helloService.time();
        split();
        hiService.sayHi();
        split();

    }

    public void split() {
        System.out.println("\n\n");
    }
}
