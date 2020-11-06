package com.winsth.android.libs.utils;

import java.util.Random;

public class RandomUtil {
    private RandomUtil() {
    }

    /**
     * 获取指定长度的字符串（字母数字混合）
     *
     * @param length 指定长度
     * @return 返回字符串
     */
    public static String getRandomString(int length) {
        String seed = "abcdefghijkmlnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(seed.length());
                builder.append(seed.charAt(index));
            }
        }

        return builder.toString();
    }

    /**
     * 获取指定长度的字符串（数字字符串）
     *
     * @param length 指定长度
     * @return 返回字符串
     */
    public static String getRandomNumber(int length) {
        String seed = "0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(seed.length());
                builder.append(seed.charAt(index));
            }
        }

        return builder.toString();
    }
}
