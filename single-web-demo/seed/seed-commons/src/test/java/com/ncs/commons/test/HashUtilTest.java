package com.ncs.commons.test;

import com.ncs.commons.token.SimpleTokenManager;
import com.ncs.commons.utils.HashUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2019/12/17
 */
public class HashUtilTest {
    @Test
    public  void simpleTokenManagerTest() throws Exception {
        System.out.println(HashUtil.hash("1", HashUtil.MD5, HashUtil.HEX));
    }
}
