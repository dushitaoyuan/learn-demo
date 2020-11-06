package com.ncs.image.util;


import java.math.BigDecimal;

/**
 * @author dushitaoyuan
 * @date 2020/8/20
 */
public class HammingDistanceUtil {

    public static int getHammingDistance(String str1, String str2) {
        int distance;
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    public static double hamMingDistanceSimilarity(double maxLen, double distance) {
        double similarity = (maxLen - distance) / maxLen;
        return new BigDecimal(similarity).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
