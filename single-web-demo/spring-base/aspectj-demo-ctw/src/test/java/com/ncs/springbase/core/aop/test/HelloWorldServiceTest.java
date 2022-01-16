package com.ncs.springbase.core.aop.test;

import com.ncs.springbase.core.aop.HelloWorldService;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @desc aop 测试类(CTW 编译时织入)
 * @date 2019/12/27
 */

public class HelloWorldServiceTest {
    @Test
    public void helloTest() {
        String name="dushitaoyuan";
        HelloWorldService helloWorldService=new HelloWorldService();
        System.out.println(helloWorldService.hello(name));
        String spilt="\n -----------------------------------------------------\n";
        System.out.println(spilt);
        HelloWorldService.main(null);
        System.out.println(spilt);
        helloWorldService.helloAuth("admin");
        System.out.println(spilt);
        helloWorldService.helloAuth("public");

    }
}
