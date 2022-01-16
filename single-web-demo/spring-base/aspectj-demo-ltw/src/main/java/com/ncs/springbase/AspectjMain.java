package com.ncs.springbase;

import com.ncs.springbase.service.impl.HelloServiceImpl;
import com.ncs.springbase.service.impl.HiServiceImpl;

/**
 * @author dushitaoyuan
 * @date 2019/12/2917:19
 */
public class AspectjMain {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        HiServiceImpl hiService = new HiServiceImpl();
        helloService.sayHello();
        split();
        helloService.time();
        split();
        hiService.sayHi();
        split();
    }


    public static void split() {
        System.out.println("\n\n");
    }
}
