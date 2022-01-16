package com.ncs.commons.test;

import com.ncs.commons.token.SimpleTokenManager;
import com.ncs.commons.utils.SystemCodeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2019/12/17
 */
public class SimpleTokenManagerTest {
    @Test
    public  void simpleTokenManagerTest(){
        SimpleTokenManager simpleTokenManager=new SimpleTokenManager();
        String hmacKey="11111";
        Map<String,Object> map=new HashMap<>();
        map.put("124","123");
        String token=simpleTokenManager.createToken(hmacKey,map,100L, TimeUnit.HOURS);

        System.out.println(simpleTokenManager.vafy(hmacKey,token));


    }
}
