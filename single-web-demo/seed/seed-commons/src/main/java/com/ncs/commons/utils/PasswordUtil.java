package com.ncs.commons.utils;


import org.springframework.util.StringUtils;

/**
 * @author dushitaoyuan
 * @date 2019/9/1115:52
 * @desc: 密码工具类 BCrypt 实现
 */
public class PasswordUtil {
    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    public static String passwordEncode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 密码是否匹配
     *
     * @param encodePassword 已加密的密码
     * @param passwordPlain
     * @return
     */
    public static boolean passwordEqual(String encodePassword, String passwordPlain) {
        if (StringUtils.isEmpty(encodePassword) || StringUtils.isEmpty(passwordPlain)) {
            return false;
        }
        return BCrypt.checkpw(passwordPlain, encodePassword);
    }

}
