package com.ncs.test;

import com.taoyuanx.demo.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;


/**
 * @author dushitaoyuan
 * @date 2020/1/823:07
 */
@Slf4j
public class MyTest {
    @Test
    public void testPasswordHashed() {
        String hashedPassword = Hex.encodeHexString(DigestUtils.getMd5Digest().digest("123456".getBytes()));
        System.out.println(hashedPassword);
        String encodePassword = PasswordUtil.passwordEncode(hashedPassword);
        System.out.println(encodePassword);
        System.out.println(PasswordUtil.passwordEqual(encodePassword, hashedPassword));
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testPasswordPlain() {
        String password = "123456";
        String encodePassword = PasswordUtil.passwordEncode(password);
        System.out.println(encodePassword);
        System.out.println(PasswordUtil.passwordEqual(encodePassword, password));
    }



}
