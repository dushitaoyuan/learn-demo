package com.taoyuanx.demo.util;

import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2020/12/22
 */
public class ThreadUtil {

    public static void sleep(long time, TimeUnit timeUnit) {
        try {
            //线程休眠2秒
            Thread.sleep(timeUnit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
