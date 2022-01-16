package com.ncs.springbase.core.aop;

import com.ncs.springbase.core.aop.anno.Auth;

/**
 * @author dushitaoyuan
 * @desc aop 测试类
 * @date 2019/12/27
 */
public class HelloWorldService {
    public String hello(String name) {
        String prefix="hello World \t";
        String result=prefix+name;
        System.out.printf(result);
        return result;
    }
    private void privateHello() {
        System.out.println(" private hello");
    }


    @Auth(needRole = "admin")
    public void helloAuth(String role) {
        System.out.println("auth hello \t"+role);
    }

    public static void main(String[] args) {
        HelloWorldService helloWorldService=new HelloWorldService();
        helloWorldService.privateHello();
    }
}
