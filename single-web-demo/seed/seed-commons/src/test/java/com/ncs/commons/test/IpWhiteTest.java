package com.ncs.commons.test;

import com.ncs.commons.utils.IpWhiteCheckUtil;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @desc 白名单测试
 * @date 2019/12/20
 */
public class IpWhiteTest {
    @Test
    public void whiteTest() {
        //以英文分号;间隔
        String ipWhilte = "192.168.1.1;" +                 //设置单个IP的白名单
                "192.168.4.*;" +                 //设置ip通配符,对一个ip段进行匹配
                "192.168.3.17-192.168.3.38;" +     //设置一个IP范围
                "192.168.5.0/24";                    //设置一个网段
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("192.168.1.1", ipWhilte));  //true
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("192.168.1.2", ipWhilte));  //false
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("192.168.4.16", ipWhilte));  //true
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("192.168.3.17", ipWhilte));  //true
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("192.168.5.1", ipWhilte));//true
        String wh = null;
        System.out.println(IpWhiteCheckUtil.checkWhiteIP("127.0.0.1", wh));
    }

}
