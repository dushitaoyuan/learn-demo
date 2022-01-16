package com.ncs.springbase.service.impl;

import com.ncs.springbase.service.HiService;

/**
 * @author dushitaoyuan
 * @date 2019/12/2823:46
 * @desc: Service 实现
 */
public class HiServiceImpl implements HiService {
    @Override
    public void sayHi() {
        System.out.println("hi");
    }
}
