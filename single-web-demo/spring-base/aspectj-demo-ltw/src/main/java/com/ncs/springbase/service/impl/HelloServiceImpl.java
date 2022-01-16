package com.ncs.springbase.service.impl;

import com.ncs.springbase.service.HelloService;

/**
 * @author dushitaoyuan
 * @date 2019/12/2823:46
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }

    @Override
    public void time() {
        System.out.println( "time is "+System.currentTimeMillis());;
    }
}
