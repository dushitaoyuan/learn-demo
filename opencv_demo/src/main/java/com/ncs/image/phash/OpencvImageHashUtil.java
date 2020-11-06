package com.ncs.image.phash;

import com.ncs.image.dhash.DHashUtil;
import com.ncs.image.phash.jdk.Fingerprint;
import com.ncs.image.util.HammingDistanceUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.math.BigInteger;


/**
 * 安装 opencv
 * <p>
 * -Djava.library.path=D:\opencv\opencv\build\java\x64
 */
public class OpencvImageHashUtil {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    static int width = 8, height = 8;

    public static String pHash(String file) {
        /**
         * 1 尺寸缩小
         * 2. 灰度处理
         * 3. 计算均值
         * 4. 均值比较得出hash
         */
        Mat srcMat = Imgcodecs.imread(file);
        Mat dstMat = new Mat();
        //1.缩小尺寸，8*8
        Imgproc.resize(srcMat, dstMat, new Size(width, height), 0, 0, Imgproc.INTER_CUBIC);
        //2.灰度处理(只保留明暗)
        Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_BGR2GRAY);

        //3.计算均值
        byte[] pixArray = new byte[((int) dstMat.total() * dstMat.channels())];

        dstMat.get(0, 0, pixArray);
        int avg = 0;
        for (int i = 0; i < height; i++) {
            int rowStart = i * width;
            for (int j = 0; j < width; j++) {
                avg += pixArray[rowStart + j];
            }
        }
        avg /= width * height;
        //4.计算hash
        StringBuilder pHash = new StringBuilder();
        for (int i = 0; i < width * height; i++) {
            if (pixArray[i] >= avg) {
                pHash.append(1);
            } else {
                pHash.append(0);
            }
        }
        return pHash.toString();
    }

    /**
     *
     */
    public static String dHash(String file) {
        /**
         * 1  尺寸缩小
         * 2. 灰度处理
         * 3. 计算均值
         * 4. 均值比较得出hash
         */
        Mat srcMat = Imgcodecs.imread(file);
        Mat dstMat = new Mat();
        //1.缩小尺寸，8*8
        Imgproc.resize(srcMat, dstMat, new Size(width, height), 0, 0, Imgproc.INTER_CUBIC);
        //2.灰度处理(只保留明暗)
        Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_BGR2GRAY);
        //3.差值计算
        byte[] pixArray = new byte[((int) dstMat.total() * dstMat.channels())];
        dstMat.get(0, 0, pixArray);
        // 4. dhash 计算 差值
        StringBuffer dHash = new StringBuffer();
        for (int i = 0; i < height; i++) {
            int rowStart = i * width;
            for (int j = 0; j < width; j++) {
                if (j != 0) {
                    int pixIndex = rowStart + j;
                    dHash.append(pixArray[pixIndex - 1] > pixArray[pixIndex] ? 1 : 0);
                }

            }
        }
        return dHash.toString();
    }


    public static void main(String[] args) {
        String img1 = "D:\\opencv\\image\\person.jpg";
        String img2 = "D:\\opencv\\image\\person10.jpg";

        String dHash1 = dHash(img1);
        String dHash2 = dHash(img2);
        String pHash1 = pHash(img1);
        String pHash2 = pHash(img2);

        String dHash3 = DHashUtil.getDHash(img1);
        String dHash4 = DHashUtil.getDHash(img2);

        String phash3 = Fingerprint.getFingerprintPhash(img1);
        String phash4 = Fingerprint.getFingerprintPhash(img2);

        System.out.println("opencv dhash:\t" + HammingDistanceUtil.getHammingDistance(dHash1, dHash2));
        System.out.println("opencv phash:\t" + HammingDistanceUtil.getHammingDistance(pHash1, pHash2));
        System.out.println("jdk dhash:\t" + HammingDistanceUtil.getHammingDistance(dHash3, dHash4));
        System.out.println("jdk phash:\t" + HammingDistanceUtil.getHammingDistance(phash3, phash4));

        System.out.println("opencv dHash: \t" + dHash1);
        System.out.println("jdk dHash:    \t" + dHash3);

        System.out.println("opencv pHash: \t" + pHash1);
        System.out.println("jdkpHash    : \t" + phash3);


    }
}