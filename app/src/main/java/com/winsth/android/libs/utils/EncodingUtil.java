package com.winsth.android.libs.utils;

import java.io.UnsupportedEncodingException;

public class EncodingUtil {
    /**
     * 将字符编码转换成US-ASCII码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toASCII(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.US_ASCII);
    }

    /**
     * 将字符编码转换成ISO-8859-1码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toISO_8859_1(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.ISO_8859_1);
    }

    /**
     * 将字符编码转换成UTF-8码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toUTF_8(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.UTF_8);
    }

    /**
     * 将字符编码转换成UTF-16BE码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toUTF_16BE(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.UTF_16BE);
    }

    /**
     * 将字符编码转换成UTF-16LE码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toUTF_16LE(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.UTF_16LE);
    }

    /**
     * 将字符编码转换成UTF-16码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toUTF_16(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.UTF_16);
    }

    /**
     * 将字符编码转换成GBK码
     *
     * @param originalString 要转换编码的字符串
     * @return 返回转换成目标编码的字符串
     */
    public static String toGBK(String originalString) {
        return changeCharset(originalString, ConfigUtil.TextCode.GBK);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param originalString 待转换编码的字符串
     * @param newCharset     目标编码
     * @return 返回转换成目标编码的字符串
     */
    public static String changeCharset(String originalString, String newCharset) {
        if (originalString != null) {
            byte[] bs = originalString.getBytes();
            try {
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param originalString 待转换编码的字符串
     * @param oldCharset     原编码
     * @param newCharset     目标编码
     * @return 返回转换成目标编码的字符串
     */
    public static String changeCharset(String originalString, String oldCharset, String newCharset) {
        if (originalString != null) {
            byte[] bs;
            try {
                bs = originalString.getBytes(oldCharset);
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
