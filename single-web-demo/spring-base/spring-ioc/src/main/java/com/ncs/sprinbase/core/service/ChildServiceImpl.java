package com.ncs.sprinbase.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dushitaoyuan
 * @desc 测试子容器
 * @date 2020/1/2
 */
public class ChildServiceImpl {
    @Autowired
    SimpleUserService simpleUserService;
    public  void say(){
        System.out.println("hello I'm  child bean");
    }
}
