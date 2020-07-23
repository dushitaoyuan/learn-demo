package com.taoyuanx.demo;

import com.taoyuanx.demo.entity.LargeString;
import com.taoyuanx.demo.entity.ObjectValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dushitaoyuan
 * @date 2020/7/23
 * <p>
 * <p>
 * 此程序 模拟内存溢出
 */
public class JvmOutputMemoryErrorDemo {

    public static void main(String[] args) throws InterruptedException {
        String str = "";
        List<LargeString> list = new ArrayList<>();
        long i = 0;
        while (true) {
            str += i;
            list.add(new LargeString(str));
            System.out.println("+++");
            i++;
        }
    }


}
