package com.ncs.sprinbase.core.utils;

import java.util.UUID;

/**
 * @author dushitaoyuan
 * @date 2019/9/1116:40
 * @desc: 主键生成工具
 */
public class IdGenUtil {
    public static Long genLongId() {
        return SpringContextUtil.getSnowflake().nextId();
    }

    public static String genLongStrId() {
        return String.valueOf(SpringContextUtil.getSnowflake().nextId());
    }

    public static String genUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}


