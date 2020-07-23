package com.taoyuanx.demo;

import com.taoyuanx.demo.entity.ObjectValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dushitaoyuan
 * @date 2020/7/23
 *
 *
 * 此程序 不断向map新增对象,但是有map的强引用所以垃圾回收器不会回收value
 * 另一个线程 每隔一秒打印一行日志,当程序需运行一段时间后,jvm会进行垃圾回收,但是由于对象未被释放所以无法回收对象,可通过
 * 观察thread的输出暂停来观察到 jvm垃圾回收时的stop word 现象
 */
public class JvmDemo01 {
    private static Random random = new Random();
    private static final Map<String, ObjectValue> cacheMap = new HashMap<>();
    private static AtomicLong count = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("thread print \t" + dateTimeFormatter.format(LocalDateTime.now()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true) {
            newBatch();
            Thread.sleep(20000);
        }
    }


    private static void newBatch() {
        int limit = 1000000;
        ObjectValue value = null;
        for (int i = 0; i < limit; i++) {
            value = newObjectValue();
            cacheMap.putIfAbsent(value.getId(), value);
        }
        System.out.println("batch finshed   " + count.get());

    }

    private static ObjectValue newObjectValue() {
        ObjectValue value = new ObjectValue();
        value.setAge(random.nextInt(100));
        value.setName("taoyuanx" + random.nextInt(1000));
        value.setId(UUID.randomUUID().toString());
        count.getAndIncrement();
        return value;
    }
}
