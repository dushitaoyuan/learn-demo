package com.ncs.image;

import com.ncs.image.dhash.DHashUtil;
import com.ncs.image.phash.OpencvImageHashUtil;
import com.ncs.image.phash.jdk.Fingerprint;
import com.ncs.image.util.HammingDistanceUtil;

import java.util.UUID;

/**
 * @author dushitaoyuan
 * @date 2020/8/20
 */
public class SpeedTest {
    public static String baseDir = "D:\\opencv\\image\\";

    public static void main(String[] args) {
        timeSay("opencvPHash",SpeedTest::opencvPHash);
        timeSay("dhash",SpeedTest::opencvPHash2);
        timeSay("dhash2",SpeedTest::dhash);
        timeSay("jdkPhash",SpeedTest::dhash2);
    }

    public static void opencvPHash() {
        String pHash1 = null, pHash2 = null;
        pHash1 = OpencvImageHashUtil.pHash(baseDir + "person.jpg");
        System.out.println("基础图 phash: " + pHash1);
        for (int i = 1; i <= 11; i++) {
            pHash2 = OpencvImageHashUtil.pHash(baseDir + "person" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的phash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }
    }
    public static void opencvPHash2() {

        String pHash1 = null, pHash2 = null;
        pHash1 = Fingerprint.getFingerprintPhash(baseDir + "yifei/gaoyuanyuan.jpg");
        System.out.println("基础图 phash: " + pHash1);
        for (int i = 1; i <= 4; i++) {
            pHash2 = Fingerprint.getFingerprintPhash(baseDir + "yifei/liuyifei" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的phash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }

        pHash1 = OpencvImageHashUtil.dHash(baseDir + "yifei/gaoyuanyuan.jpg");
        System.out.println("基础图 phash: " + pHash1);
        for (int i = 1; i <= 4; i++) {
            pHash2 = OpencvImageHashUtil.dHash(baseDir + "yifei/liuyifei" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的phash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }
    }

    public static void dhash() {
        String pHash1 = null, pHash2 = null;
        pHash1 = DHashUtil.getDHash(baseDir + "person.jpg");
        System.out.println("基础图 dhash: " + pHash1);
        for (int i = 1; i <= 11; i++) {
            pHash2 = DHashUtil.getDHash(baseDir + "person" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的dhash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }
    }
    public static void dhash2() {
        String pHash1 = null, pHash2 = null;
        pHash1 = OpencvImageHashUtil.dHash(baseDir + "person.jpg");
        System.out.println("基础图 dhash: " + pHash1);
        for (int i = 1; i <= 11; i++) {
            pHash2 = OpencvImageHashUtil.dHash(baseDir + "person" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的dhash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }
    }
    public static void jdkPhash() {
        String pHash1 = null, pHash2 = null;
        pHash1 = Fingerprint.getFingerprintPhash(baseDir + "person.jpg");
        System.out.println("基础图 phash: " + pHash1);
        for (int i = 1; i <= 11; i++) {
            pHash2 = Fingerprint.getFingerprintPhash(baseDir + "person" + i + ".jpg");
            int distance = HammingDistanceUtil.getHammingDistance(pHash1, pHash2);
            System.out.println("图片" + i + "的phash 为" + pHash2 + "\t图片" + i + "和基础图汉明距离为" + distance + "相似度为：" + HammingDistanceUtil.hamMingDistanceSimilarity(64, distance));
        }
    }

    public static void timeSay(String name, Runnable runnable) {
        Long start = System.currentTimeMillis();
        runnable.run();
        Long end = System.currentTimeMillis();
        System.out.println(name + "\t 执行耗时:\t" + (end - start));
    }
}
