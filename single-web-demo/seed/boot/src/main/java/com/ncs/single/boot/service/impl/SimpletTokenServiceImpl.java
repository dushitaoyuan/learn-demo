package com.ncs.single.boot.service.impl;

import com.ncs.commons.exception.TokenException;
import com.ncs.commons.token.SimpleTokenManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc 默认token实现
 * @date 2019/12/20
 */
public class SimpletTokenServiceImpl {


    public String create(String paramHash, String openSecret, Map<String, Object> toekenData, Long expire, TimeUnit timeUnit) {
        if (toekenData == null) {
            toekenData = new HashMap<>(1);
        }
        toekenData.put("hash", paramHash);
        return SimpleTokenManager.createToken(openSecret, toekenData, expire, timeUnit);
    }

    public Map<String, Object> vary(String token, String hash, String openSecret) {
        Map<String, Object> vafy = SimpleTokenManager.vafy(openSecret, token);
        if (vafy.get("hash").equals(hash)) {
            return vafy;
        }
        throw new TokenException("请求hash不匹配");
    }
}
