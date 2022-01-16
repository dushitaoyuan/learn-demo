package com.ncs.commons.test;

import com.ncs.commons.utils.SystemCodeUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @desc SystemCode测试
 * @date 2019/12/17
 */
public class SystemCodeTest {
    @Test
    public  void systemCodeTest(){
        SystemCodeUtil systemCodeUtil=new SystemCodeUtil("NCS",16);
        String systemcode = systemCodeUtil.genSystemcode(null, 100L);
        System.out.println(systemcode);
        System.out.println(systemCodeUtil.isSystemCode(systemcode));
        System.out.println(systemCodeUtil.getSystemCodePattern());
        System.out.println(systemCodeUtil.getSystemCodeLength());
        Assert.assertTrue(systemCodeUtil.getSystemCodeLength().equals(systemcode.length()));
    }
}
