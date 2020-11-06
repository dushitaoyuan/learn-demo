package com.ncs.image.dhash;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 差异哈希算法
 */
public class DHashUtil {
    static int width = 8, height = 8;
    /**
     * 计算dHash方法
     */
    public static String getDHash(String file) {
        //读取文件
        BufferedImage srcImage;
        try {
            srcImage = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //文件转成8*8像素，为算法比较通用的长宽
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        int[][] grayPix = new int[width][height];
        StringBuffer figure = new StringBuffer();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //图片灰度化
                int rgb = buffImg.getRGB(x, y);
                int r = rgb >> 16 & 0xff;
                int g = rgb >> 8 & 0xff;
                int b = rgb & 0xff;
                int gray = (r * 30 + g * 59 + b * 11) / 100;
                grayPix[x][y] = gray;
                //开始计算dHash 总共有9*8像素 每行相对有8个差异值 总共有 8*8=64 个
                if (x != 0) {
                    long bit = grayPix[x - 1][y] > grayPix[x][y] ? 1 : 0;
                    figure.append(bit);
                }
            }
        }

        return figure.toString();
    }


}